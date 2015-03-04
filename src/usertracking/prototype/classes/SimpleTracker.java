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
	
	private boolean isInProfilingMode;
	private boolean isInRecordingMode;

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
			
			userGen.getUserReenterEvent().addObserver(new UserReenterObserver());
			
			
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

	public boolean isInProfilingMode() {
		return isInProfilingMode;
	}

	public void setInProfilingMode(boolean isInProfilingMode) {
		this.isInProfilingMode = isInProfilingMode;
	}

	public boolean isInRecordingMode() {
		return isInRecordingMode;
	}

	public void setInRecordingMode(boolean isInRecordingMode) {
		this.isInRecordingMode = isInRecordingMode;
	}
	
	class UserReenterObserver implements IObserver<UserEventArgs> {
		@Override
		public void update(IObservable<UserEventArgs> observable,
				UserEventArgs args) {
			System.out.println("Reentering user " + args.getId());
					
			try {
				skeletonCap.requestSkeletonCalibration(args.getId());
			} catch (StatusException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}}
	
	class UserExitObserver implements IObserver<UserEventArgs> {
		@Override
		public void update(IObservable<UserEventArgs> observable,
				UserEventArgs args) {
			System.out.println("Exiting user " + args.getId());
			profiledUsers.remove((Integer)args.getId());
			recognizedUsers.remove((Integer)args.getId());
			
			try {
				skeletonCap.clearSkeletonCalibrationData(args.getId());
				skeletonCap.stopTracking(args.getId());
				joints.remove((Integer)args.getId());
			} catch (StatusException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

			System.out.println("Calibration complete: " + args.getStatus());
			try {
				if (args.getStatus() == CalibrationProgressStatus.OK) {

					System.out.println("starting tracking " + args.getUser());
					
					skeletonCap.startTracking(args.getUser());
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
		public  double getLenght(Point3D joint1, Point3D joint2) {
			return Math.sqrt(Math.pow(joint1.getX() - joint2.getX(), 2)
					+ Math.pow(joint1.getY() - joint2.getY(), 2)
					+ Math.pow(joint1.getZ() - joint2.getZ(), 2));
		}
		
		
		public  Point3D getTriangleCentroid(Point3D joint1, Point3D joint2,
				Point3D joint3) {
			Point3D retPoint = new Point3D();
			
			float X = (joint1.getX() + joint2.getX() + joint3.getX()) / 3;
			float Y = (joint1.getY() + joint2.getY() + joint3.getY()) / 3;
			float Z = (joint1.getZ() + joint2.getZ() + joint3.getZ()) / 3;

			retPoint.setPoint(X, Y, Z);
			return retPoint;
		}
		
		@Override
		public void update(IObservable<PoseDetectionEventArgs> observable,
				PoseDetectionEventArgs args) {
			
			 try {
				 
				 System.out.println("Pose " + args.getPose() + " detected for "
						 + args.getUser());
						 profiledUsers.add(args.getUser());
						 
						 
						 /****/
						 HashMap<SkeletonJoint, SkeletonJointPosition> dict = joints.get(args.getUser());
							if (dict.size() > 0) {

								SkeletonJointPosition headPosition = dict
										.get(SkeletonJoint.HEAD);
								SkeletonJointPosition neckPosition = dict
										.get(SkeletonJoint.NECK);
								SkeletonJointPosition torsoPosition = dict
										.get(SkeletonJoint.TORSO);

								SkeletonJointPosition leftShoulder = dict
										.get(SkeletonJoint.LEFT_SHOULDER);
								SkeletonJointPosition rightShoulder = dict
										.get(SkeletonJoint.RIGHT_SHOULDER);

								SkeletonJointPosition leftHip = dict
										.get(SkeletonJoint.LEFT_HIP);
								SkeletonJointPosition rightHip = dict
										.get(SkeletonJoint.RIGHT_HIP);

								SkeletonJointPosition leftKnee = dict
										.get(SkeletonJoint.LEFT_KNEE);
								SkeletonJointPosition rightKnee = dict
										.get(SkeletonJoint.RIGHT_KNEE);

								SkeletonJointPosition leftElbow = dict
										.get(SkeletonJoint.LEFT_ELBOW);
								SkeletonJointPosition rightElbow = dict
										.get(SkeletonJoint.RIGHT_ELBOW);

								SkeletonJointPosition leftCollar = dict
										.get(SkeletonJoint.LEFT_COLLAR);
								SkeletonJointPosition rightCollar = dict
										.get(SkeletonJoint.LEFT_COLLAR);

								SkeletonJointPosition leftAnkle = dict
										.get(SkeletonJoint.LEFT_ANKLE);
								SkeletonJointPosition rightAnkle = dict
										.get(SkeletonJoint.RIGHT_ANKLE);

								SkeletonJointPosition lehtHand = dict
										.get(SkeletonJoint.LEFT_HAND);
								SkeletonJointPosition rightHand = dict
										.get(SkeletonJoint.RIGHT_HAND);
								
								SkeletonJointPosition rightFoot = dict
										.get(SkeletonJoint.RIGHT_FOOT);

								List<SkeletonJointPosition> interestingJoints = new LinkedList<SkeletonJointPosition>();
								
								
								int frameId = depthGen.getFrameID();
								int buffFrameId = -1;
								
								if (torsoPosition.getConfidence() == 1.0 && headPosition.getConfidence() == 1.0 && rightFoot.getConfidence() == 1.0 && leftElbow.getConfidence()== 1.0
										&& rightElbow.getConfidence()== 1.0 && buffFrameId != frameId) {
									 
									Point3D torsoPos = depthGen
											.convertProjectiveToRealWorld(torsoPosition
													.getPosition());

									Point3D neckPos = depthGen
											.convertProjectiveToRealWorld(neckPosition
													.getPosition());
									Point3D headPos = depthGen
											.convertProjectiveToRealWorld(headPosition
													.getPosition());
										
									
									Point3D rightHipPos = depthGen
											.convertProjectiveToRealWorld(rightHip
													.getPosition());

									Point3D leftHipPos = depthGen
											.convertProjectiveToRealWorld(leftHip
													.getPosition());
									
									Point3D rightKneePos = depthGen
											.convertProjectiveToRealWorld(rightKnee
													.getPosition());
									Point3D leftKneePos = depthGen
											.convertProjectiveToRealWorld(leftKnee
													.getPosition());
			
									Point3D rightFootPos = depthGen
											.convertProjectiveToRealWorld(rightFoot
													.getPosition());
			
									Point3D rightShoulderPos = depthGen
											.convertProjectiveToRealWorld(rightShoulder
													.getPosition());

									Point3D leftShoulderPos = depthGen
											.convertProjectiveToRealWorld(leftShoulder
													.getPosition());
									
									Point3D leftElbowPos = depthGen
											.convertProjectiveToRealWorld(leftElbow
													.getPosition());
									
									Point3D rightElbowPos = depthGen
											.convertProjectiveToRealWorld(rightElbow
													.getPosition());
									
									Point3D leftHandPos = depthGen
											.convertProjectiveToRealWorld(lehtHand
													.getPosition());
									
									Point3D rightHandPos = depthGen
											.convertProjectiveToRealWorld(rightHand
													.getPosition());
									
									double shoulders = getLenght(rightShoulderPos, leftShoulderPos);
									double torsoLS = getLenght(torsoPos, leftShoulderPos);
									double torsoRS = getLenght(torsoPos, rightShoulderPos);
									
									double leftShoulderToElbow = getLenght(leftShoulderPos, leftElbowPos);
									double lefttShoulderToHand = getLenght(leftShoulderPos, leftHandPos);
									double leftHandToShoulder = getLenght(leftShoulderPos, leftHandPos);
									
									double rightShoulderToElbow = getLenght(rightShoulderPos, rightElbowPos);
									double rightShoulderToHand = getLenght(rightShoulderPos, rightHandPos);
									double rightHandToShoulder = getLenght(rightShoulderPos, rightHandPos);
									
									double rightKneeToHip = getLenght(rightHipPos, rightKneePos);
									double leftKneeToHip = getLenght(leftHipPos, leftKneePos);
									
									Point3D TC = getTriangleCentroid(torsoPos,leftShoulderPos,rightShoulderPos);
									Point3D LSHC = getTriangleCentroid(leftShoulderPos,leftElbowPos,leftHandPos);
									Point3D RSHC = getTriangleCentroid(rightShoulderPos,rightElbowPos,rightHandPos);
									
									Point3D TLSLS = getTriangleCentroid(torsoPos,leftShoulderPos,leftHipPos);
									Point3D TRSRH = getTriangleCentroid(torsoPos,rightShoulderPos,rightHipPos);
									

									Point3D TH = getTriangleCentroid(torsoPos,rightHipPos,leftHipPos);
									
									double TC_TH = getLenght(TC, TH);
									double TLSLS_TRSRH = getLenght(TLSLS, TRSRH);
									
									
									
									double TC_LSHC = getLenght(TC, LSHC);
									double TC_RSHC = getLenght(TC, RSHC);
									double LSHC_RSHC = getLenght(LSHC,RSHC);
									
									
										System.out.println(args.getUser() + ", " + TC_TH + ", " + shoulders + ", " + TLSLS_TRSRH);
										//System.out.println(users[i] + ", " + torsoLS + ", " + torsoRS + ", " + shoulders + ", " +  rightKneeToHip + ", " + leftKneeToHip);
									

							}
						 
			
						 
						 /***/
						 
				//poseDetectionCap.stopPoseDetection(args.getUser());
								}}
			 catch (StatusException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			//try {
//				
//				skeletonCap.requestSkeletonCalibration(args.getUser(), true);
//			} catch (StatusException e) {
//				e.printStackTrace();
//			}
		}	
	}
}
