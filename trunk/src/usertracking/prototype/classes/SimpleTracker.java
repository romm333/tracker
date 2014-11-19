package usertracking.prototype.classes;

import org.OpenNI.*;

import java.awt.Color;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimpleTracker {
	private final String SAMPLE_XML_FILE = "../Data/SamplesConfig.xml";

	private OutArg<ScriptNode> scriptNode;
	private Context context;
	public DepthGenerator depthGen;
	public UserGenerator userGen;
	public SkeletonCapability skeletonCap;
	public PoseDetectionCapability poseDetectionCap;
	public String calibPose = null;
	HashMap<Integer, HashMap<SkeletonJoint, SkeletonJointPosition>> joints;

	private byte[] imgbytes;
	private float histogram[];
	public DepthMetaData depthMD;

	public static String calibrationState = "";

	public static String userState = "";

	public static String trakingState = "";

	public HashMap<Integer, HashMap<SkeletonJoint, SkeletonJointPosition>> getJoints() {
		return joints;
	}

	public int width;
	public int height;

	public UserProfiler userProfiler;
	HashMap<Integer, UserProfile> matchingUserProfiles;

	public UserProfile getMatchingUserProfile(int uid) {
		return matchingUserProfiles.get(uid);
	}

	public SimpleTracker() {
		try {
			scriptNode = new OutArg<ScriptNode>();
			context = Context.createFromXmlFile(SAMPLE_XML_FILE, scriptNode);

			depthGen = DepthGenerator.create(context);
			DepthMetaData depthMD = depthGen.getMetaData();

			histogram = new float[10000];
			width = depthMD.getFullXRes();
			height = depthMD.getFullYRes();

			imgbytes = new byte[width * height * 3];
			histogram = new float[10000];

			userGen = UserGenerator.create(context);
			skeletonCap = userGen.getSkeletonCapability();

			poseDetectionCap = userGen.getPoseDetectionCapability();

			userGen.getNewUserEvent().addObserver(new NewUserObserver());
			userGen.getLostUserEvent().addObserver(new LostUserObserver());
			skeletonCap.getCalibrationCompleteEvent().addObserver(
					new CalibrationCompleteObserver());
			poseDetectionCap.getPoseDetectedEvent().addObserver(
					new PoseDetectedObserver());

			calibPose = skeletonCap.getSkeletonCalibrationPose();
			joints = new HashMap<Integer, HashMap<SkeletonJoint, SkeletonJointPosition>>();

			skeletonCap.setSkeletonProfile(SkeletonProfile.ALL);

			matchingUserProfiles = new HashMap<Integer, UserProfile>();
			userProfiler = new UserProfiler();

			context.startGeneratingAll();

		} catch (GeneralException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public SkeletonJointPosition getJointPosition(int user, SkeletonJoint joint)
			throws StatusException {
		SkeletonJointPosition pos = skeletonCap.getSkeletonJointPosition(user,
				joint);
		if (pos.getPosition().getZ() != 0) {
			return new SkeletonJointPosition(
					depthGen.convertRealWorldToProjective(pos.getPosition()),
					pos.getConfidence());
		}

		return pos;
	}

	private void calcHist(ShortBuffer depth) {
		// reset
		for (int i = 0; i < histogram.length; ++i)
			histogram[i] = 0;

		depth.rewind();

		int points = 0;
		while (depth.remaining() > 0) {
			short depthVal = depth.get();
			if (depthVal != 0) {
				histogram[depthVal]++;
				points++;
			}
		}

		for (int i = 1; i < histogram.length; i++) {
			histogram[i] += histogram[i - 1];
		}

		if (points > 0) {
			for (int i = 1; i < histogram.length; i++) {
				histogram[i] = 1.0f - (histogram[i] / (float) points);
			}
		}
	}

	Color colors[] = { Color.RED, Color.BLUE, Color.CYAN, Color.GREEN,
			Color.MAGENTA, Color.PINK, Color.YELLOW, Color.WHITE };

	public void updateDepth() {
		try {

			context.waitAnyUpdateAll();

			DepthMetaData depthMD = depthGen.getMetaData();
			SceneMetaData sceneMD = userGen.getUserPixels(0);

			ShortBuffer scene = sceneMD.getData().createShortBuffer();
			ShortBuffer depth = depthMD.getData().createShortBuffer();
			calcHist(depth);
			depth.rewind();

			while (depth.remaining() > 0) {
				int pos = depth.position();
				short pixel = depth.get();
				short user = scene.get();

				imgbytes[3 * pos] = 0;
				imgbytes[3 * pos + 1] = 0;
				imgbytes[3 * pos + 2] = 0;

				if (pixel != 0) {
					int colorID = user % (colors.length - 1);
					if (user == 0) {
						colorID = colors.length - 1;
					}
					if (pixel != 0) {
						float histValue = histogram[pixel];
						imgbytes[3 * pos] = (byte) (histValue * colors[colorID]
								.getRed());
						imgbytes[3 * pos + 1] = (byte) (histValue * colors[colorID]
								.getGreen());
						imgbytes[3 * pos + 2] = (byte) (histValue * colors[colorID]
								.getBlue());
					}
				}
			}
		} catch (GeneralException e) {
			e.printStackTrace();
		}
	}

	class NewUserObserver implements IObserver<UserEventArgs> {
		@Override
		public void update(IObservable<UserEventArgs> observable,
				UserEventArgs args) {
			SimpleTracker.userState = "New user " + args.getId();
			System.out.println("New user " + args.getId());

			try {
				if (skeletonCap.needPoseForCalibration()) {
					poseDetectionCap
							.startPoseDetection(calibPose, args.getId());
				} else {
					skeletonCap.requestSkeletonCalibration(args.getId(), true);
				}
			} catch (StatusException e) {
				e.printStackTrace();
			}
		}
	}

	class UserProfileThread extends Thread {
		private int userId;
		private boolean isDone = false;

		public UserProfileThread(int user) {
			userId = user;
		}

		public void run() {

			while (!isDone) {
				if (skeletonCap.isSkeletonTracking(userId)) {
					try {
						Thread.sleep(5000);

						Point3D torso = getJointPosition(
								userId, SkeletonJoint.TORSO).getPosition();

						Point3D neck = getJointPosition(
								userId, SkeletonJoint.NECK).getPosition();
						Point3D rightShoulder = getJointPosition(userId,
										SkeletonJoint.RIGHT_SHOULDER)
								.getPosition();
						Point3D leftShoulder = getJointPosition(userId,
										SkeletonJoint.LEFT_SHOULDER)
								.getPosition();
						Point3D head = getJointPosition(
								userId, SkeletonJoint.HEAD).getPosition();

						List<Point3D> profileJoints = new ArrayList<Point3D>();
						profileJoints.add(torso);
						profileJoints.add(leftShoulder);
						profileJoints.add(rightShoulder);
						profileJoints.add(neck);
						profileJoints.add(head);

						UserProfile profile = new UserProfile();
						profile.addProfileJoints(profileJoints);
						profile.calculateVectorLength();

						profile.setProfileName("existingUser_" + userId);

						UserProfile oneExistingProfile = userProfiler
								.getSimilarProfile(profile);

						if (oneExistingProfile == null) {
							userProfiler.insertProfile(profile);
						} else {
							profile = oneExistingProfile;
						}

						matchingUserProfiles.put(userId, profile);

						System.out.println("profile inserted");

						isDone = true;

					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}
	}

	class LostUserObserver implements IObserver<UserEventArgs> {
		@Override
		public void update(IObservable<UserEventArgs> observable,
				UserEventArgs args) {
			SimpleTracker.userState = "Lost user" + args.getId();
			System.out.println("Lost user " + args.getId());
			joints.remove(args.getId());

			if (matchingUserProfiles.containsKey(args.getId()))
				matchingUserProfiles.remove(args.getId());
		}
	}

	class CalibrationCompleteObserver implements
			IObserver<CalibrationProgressEventArgs> {
		@Override
		public void update(
				IObservable<CalibrationProgressEventArgs> observable,
				CalibrationProgressEventArgs args) {
			SimpleTracker.calibrationState = "Calibration complete: "
					+ args.getStatus();
			System.out.println("Calibration complete: " + args.getStatus());
			try {
				if (args.getStatus() == CalibrationProgressStatus.OK) {
					SimpleTracker.trakingState = "starting tracking "
							+ args.getUser();
					System.out.println("starting tracking " + args.getUser());
					skeletonCap.startTracking(args.getUser());
					joints.put(new Integer(args.getUser()),
							new HashMap<SkeletonJoint, SkeletonJointPosition>());

					UserProfileThread upThread = new UserProfileThread(
							args.getUser());
					upThread.start();

				} else if (args.getStatus() != CalibrationProgressStatus.MANUAL_ABORT) {
					if (skeletonCap.needPoseForCalibration()) {
						poseDetectionCap.startPoseDetection(calibPose,
								args.getUser());
					} else {
						skeletonCap.requestSkeletonCalibration(args.getUser(),
								true);
					}
				}
			} catch (StatusException e) {
				e.printStackTrace();
			}
		}
	}

	class PoseDetectedObserver implements IObserver<PoseDetectionEventArgs> {
		@Override
		public void update(IObservable<PoseDetectionEventArgs> observable,
				PoseDetectionEventArgs args) {
			System.out.println("Pose " + args.getPose() + " detected for "
					+ args.getUser());
			try {
				poseDetectionCap.stopPoseDetection(args.getUser());
				skeletonCap.requestSkeletonCalibration(args.getUser(), true);
			} catch (StatusException e) {
				e.printStackTrace();
			}
		}
	}
}
