package usertracking.prototype.classes;

import org.OpenNI.*;

import java.awt.Color;
import java.nio.ShortBuffer;
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
	
	List<Integer> profiledUsers;
	HashMap<Integer, Double> recognizedUsers;

	private byte[] imgbytes;
	private float histogram[];
	public DepthMetaData depthMD;

	public HashMap<Integer, HashMap<SkeletonJoint, SkeletonJointPosition>> getJoints() {
		return joints;
	}

	public int width;
	public int height;

	private List<Observer> attachedObservers = new LinkedList<Observer>();
	
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
			profiledUsers = new LinkedList<Integer>();
			recognizedUsers = new HashMap<Integer, Double>();

			userGen.getNewUserEvent().addObserver(new NewUserObserver());
			
			userGen.getUserExitEvent().addObserver(new UserExitObserver());

			userGen.getLostUserEvent().addObserver(new LostUserObserver());

			skeletonCap.getCalibrationCompleteEvent().addObserver(
					new CalibrationCompleteObserver());
			poseDetectionCap.getPoseDetectedEvent().addObserver(
					new PoseDetectedObserver());
			
			
			calibPose = skeletonCap.getSkeletonCalibrationPose();
			joints = new HashMap<Integer, HashMap<SkeletonJoint, SkeletonJointPosition>>();

			skeletonCap.setSkeletonProfile(SkeletonProfile.ALL);

			context.startGeneratingAll();
		

		} catch (GeneralException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public boolean isRecognitionRequestedForUser(int uid){
		return profiledUsers.contains(uid);
	}
	
	public boolean isUserRecognized(int uid){
		return recognizedUsers.containsKey(uid);
	}
	
	public void setRecognizedUser(int uid, double distance){
		recognizedUsers.put(uid, distance);
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
			profiledUsers.remove((Integer)args.getId());
			recognizedUsers.remove((Integer)args.getId());

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

		class LostUserObserver implements IObserver<UserEventArgs> {
		@Override
		public void update(IObservable<UserEventArgs> observable,
				UserEventArgs args) {
			System.out.println("Lost user " + args.getId());
			joints.remove(args.getId());
			profiledUsers.remove((Integer)args.getId());
			recognizedUsers.remove((Integer)args.getId());
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
					if(!joints.containsKey(args.getUser()))
					joints.put(new Integer(args.getUser()),
							new HashMap<SkeletonJoint, SkeletonJointPosition>());
					
					poseDetectionCap.startPoseDetection("Psi",args.getUser());
					
			
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
			 profiledUsers.add(args.getUser());
			 
			//try {
//				poseDetectionCap.stopPoseDetection(args.getUser());
//				skeletonCap.requestSkeletonCalibration(args.getUser(), true);
//			} catch (StatusException e) {
//				e.printStackTrace();
//			}
		}
	}
}
