package usertracking.prototype.classes;

import org.OpenNI.*;

import usertracking.prototype.gui.utils.DataLogger;
import usertracking.prototype.profile.IUserProfile;
import usertracking.prototype.profile.JointCluster;
import usertracking.prototype.profile.JointVector;
import usertracking.prototype.profile.ProfileCache;
import usertracking.prototype.profile.ProfileDataFromFiles;
import usertracking.prototype.profile.ProfileJointGroup;
import usertracking.prototype.profile.ProfileKMeans;
import usertracking.prototype.profile.UserProfileByJointClusters;
import usertracking.prototype.profile.UserProfileByJoints;
import usertracking.prototype.profile.UserProfileObserver;
//import usertracking.prototype.profile.UserProfileByCentroids;
//import usertracking.prototype.profile.UserProfileByJoints;
import usertracking.prototype.profile.UserProfiler;

import java.awt.Color;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class SimpleTracker extends Observable {
	private final String SAMPLE_XML_FILE = "sensorConfig/SamplesConfig.xml";

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

	private List<Observer> attachedObservers = new LinkedList<Observer>();
	
	public ProfileCache listOfClusters = new ProfileDataFromFiles().loadProfiles();

	public UserProfiler userProfiler;
	HashMap<Integer, UserProfileByJointClusters> matchingUserProfiles;

	public IUserProfile getMatchingUserProfile(int uid) {
		return matchingUserProfiles.get(uid);
	}
	
	public Recorder recorder;
	
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
			userGen.getUserReenterEvent()
					.addObserver(new UserReenterObserver());
			userGen.getUserExitEvent().addObserver(new UserExitObserver());

			userGen.getLostUserEvent().addObserver(new LostUserObserver());

			skeletonCap.getCalibrationCompleteEvent().addObserver(
					new CalibrationCompleteObserver());
			poseDetectionCap.getPoseDetectedEvent().addObserver(
					new PoseDetectedObserver());

			calibPose = skeletonCap.getSkeletonCalibrationPose();
			joints = new HashMap<Integer, HashMap<SkeletonJoint, SkeletonJointPosition>>();

			skeletonCap.setSkeletonProfile(SkeletonProfile.ALL);

			matchingUserProfiles = new HashMap<Integer, UserProfileByJointClusters>();
			userProfiler = new UserProfiler();

			context.startGeneratingAll();
		

		} catch (GeneralException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public synchronized void addObserver(Observer o) {
		attachedObservers.add(o);
	}

	@Override
	public void notifyObservers() {
		for (Observer oneview : attachedObservers) {
			oneview.update(this, null);
		}
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
			
			
			//recorder.Record();

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

			notifyObservers();

		} catch (GeneralException e) {
			e.printStackTrace();
		}
	}

	class UserExitObserver implements IObserver<UserEventArgs> {
		@Override
		public void update(IObservable<UserEventArgs> observable,
				UserEventArgs args) {
			System.out.println("Exiting user " + args.getId());
			userGen.getLostUserEvent();

		}
	}

	class UserReenterObserver implements IObserver<UserEventArgs> {
		@Override
		public void update(IObservable<UserEventArgs> observable,
				UserEventArgs args) {
		}
	}

	class NewUserObserver implements IObserver<UserEventArgs> {
		@Override
		public void update(IObservable<UserEventArgs> observable,
				UserEventArgs args) {

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
		int prevFrameId = Integer.MIN_VALUE;
		
		long start_time = System.currentTimeMillis();
		long time_diff = 0;
		int frameID = Integer.MIN_VALUE;
		
		private List<JointVector> profileJointVectors = new ArrayList<JointVector>();
		
		public void run() {

			while (!isDone) {
				if (skeletonCap.isSkeletonTracking(userId)) {
					try {
						int buffFrameId = userGen.getFrameID();
						UserProfileByJoints profile = new UserProfileByJoints(
								userId, userGen, depthGen);

						String csvString = DataLogger
								.getCoordsAsCSV(profile, ProfileJointGroup.FULL);
						
						long current_time = System.currentTimeMillis();
						time_diff = current_time - start_time;
						
						if (buffFrameId != frameID) {
							String[] params = csvString.split(",");
							double a = Double.parseDouble(params[2]);
									double b = Double.parseDouble(params[4]);
											double c = Double.parseDouble(params[6]);
													double d = Double.parseDouble(params[12]);
							
							JointVector jVector = new JointVector(a, b, c, d);
							//collect new user profile joints
							profileJointVectors.add(jVector);
							if (time_diff > 8000) {
								DataLogger.writeFile(csvString,
										String.valueOf(userId) + ".csv");
								System.out.println(csvString);
							}
							frameID = buffFrameId;
						}
						
						if (time_diff > 20000) {
							isDone = true;
							
							
							ProfileKMeans kMeans = new ProfileKMeans(profileJointVectors, 2);
							List<JointCluster> userJointClusters = kMeans.getJointsClusters();
							
							UserProfileByJointClusters newProfileBuClusters = new UserProfileByJointClusters(userId, userGen, depthGen);
							newProfileBuClusters.setProfileName("EXISTING USER PROFILE" + userId);
							newProfileBuClusters.setProfileJointVectors(userJointClusters);
							
//							
//							for(UserProfileByJointClusters oneProfile : listOfClusters){
//								List<JointCluster> availableProfileClusters = oneProfile.getProfileJointClusters();
//								for(int i = 0; i < availableProfileClusters.size();i++ ){
//									JointVector vector1 = availableProfileClusters.get(i).getCentroid();
//									JointVector vector2 = newProfileBuClusters.getProfileJointClusters().get(i).getCentroid();
//									
//									
//									
//									JointVector result = kMeans.getStandardDeviationVector(vector1, vector2);
//									//System.out.println(oneProfile.getProfileName() + " "+result.a + " " + result.b + " " + result.c + " " + result.d);
//									
//									if(result.a < 1.5 && result.b < 1.5 &&  result.c < 1.5 &&  result.d < 1.5){
//										System.out.println("FOUND " + oneProfile.getProfileName() + " "+result.a + " " + result.b + " " + result.c + " " + result.d);
//										return;
//									}
//								}
//							}
							
							listOfClusters.add(newProfileBuClusters);
							matchingUserProfiles.put(userId, newProfileBuClusters);
							System.out.println("Coordinates Saved");
						}

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

			System.out.println("Lost user " + args.getId());
			joints.remove(args.getId());

			// if (matchingUserProfiles.containsKey(args.getId()))
			// matchingUserProfiles.remove(args.getId());
		}
	}

	class CalibrationCompleteObserver implements
			IObserver<CalibrationProgressEventArgs> {
		@Override
		public void update(
				IObservable<CalibrationProgressEventArgs> observable,
				CalibrationProgressEventArgs args) {

			// System.out.println("Calibration complete: " + args.getStatus());
			try {
				if (args.getStatus() == CalibrationProgressStatus.OK) {

					System.out.println("starting tracking " + args.getUser());
					skeletonCap.startTracking(args.getUser());
					joints.put(new Integer(args.getUser()),
							new HashMap<SkeletonJoint, SkeletonJointPosition>());
				
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
			// System.out.println("Pose " + args.getPose() + " detected for "
			// + args.getUser());
			try {
				poseDetectionCap.stopPoseDetection(args.getUser());
				skeletonCap.requestSkeletonCalibration(args.getUser(), true);
			} catch (StatusException e) {
				e.printStackTrace();
			}
		}
	}
}
