package usertracking.prototype.classes;

import org.OpenNI.*;

import usertracking.prototype.kmeans.JointCluster;
import usertracking.prototype.kmeans.ProfileKMeans;
import usertracking.prototype.profile.DummyProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
 
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
	
	public HashMap<Integer,DummyProfile> getRecognizedProfiles() {
		return recognizedProfiles;
	}

	public void setRecognizedProfiles(HashMap<Integer,DummyProfile> recognizedProfiles) {
		this.recognizedProfiles = recognizedProfiles;
	}

	public HashMap<Integer, HashMap<SkeletonJoint, SkeletonJointPosition>> getJoints() {
		return joints;
	}

	public int width;
	public int height;

	private List<Observer> attachedObservers = new LinkedList<Observer>();
	
	Set<Integer> profiledUsers;
	HashMap<Integer, Double> recognizedUsers;
	
	private boolean isInProfilingMode;
	private boolean isInRecordingMode;
	
	private ProfileKMeans profileData;
	private List<JointCluster> jointClusters;
	
	private List<DummyProfile> profileMeans;
	private HashMap<Integer,DummyProfile> recognizedProfiles;
	
	public SimpleTracker() {
		try {
			scriptNode = new OutArg<ScriptNode>();
			context = Context.createFromXmlFile(SAMPLE_XML_FILE, scriptNode);
			depthGen = DepthGenerator.create(context);
			
			DepthMetaData depthMD = depthGen.getMetaData();
			width = depthMD.getFullXRes();
			height = depthMD.getFullYRes();

			userGen = UserGenerator.create(context);
			skeletonCap = userGen.getSkeletonCapability();
			poseDetectionCap = userGen.getPoseDetectionCapability();
			
			profiledUsers = new HashSet<Integer>();
			recognizedUsers = new HashMap<Integer, Double>();
			recognizedProfiles = new HashMap<Integer, DummyProfile>();
			
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
	
	public void loadProfileData(int clusterCount){
		profileData = new ProfileKMeans("profiles/1.csv", clusterCount);
		jointClusters = profileData.getJointsClusters();
		
		profileMeans = new ArrayList<DummyProfile>();
		
		for (int i = 0; i < profileData.k; i++) {
			System.out.println("Cluster " + i + ": "
					+ jointClusters.get(i).getCentroid() + " " + jointClusters.get(i).getJointVectors().size());
			DummyProfile oneProfile = new DummyProfile();
			oneProfile.ID = i;
			oneProfile.Name = "Profile" + String.valueOf(i);
			oneProfile.profileMean = jointClusters.get(i).getCentroid();
			profileMeans.add(oneProfile);
		}
	}
	
	public ProfileKMeans getProfileData(){
		return profileData;
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

	public void updateDepth() {
		try {
			context.waitAnyUpdateAll();
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
	
	public List<DummyProfile> getProfileMeans() {
		return profileMeans;
	}

	class UserReenterObserver implements IObserver<UserEventArgs> {
		@Override
		public void update(IObservable<UserEventArgs> observable,
				UserEventArgs args) {
			System.out.println("Reentering user " + args.getId());
		}
	}
	
	class UserExitObserver implements IObserver<UserEventArgs> {
		@Override
		public void update(IObservable<UserEventArgs> observable,
				UserEventArgs args) {
			System.out.println("Exiting user " + args.getId());
			profiledUsers.remove((Integer)args.getId());
			profiledUsers.clear();
			recognizedUsers.remove(args.getId());
			recognizedProfiles.remove(args.getId());
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
			recognizedUsers.remove(args.getId());
			recognizedProfiles.remove(args.getId());
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
		@Override
		public void update(IObservable<PoseDetectionEventArgs> observable,
				PoseDetectionEventArgs args) {
			
			 try {
				 
				 System.out.println("Pose " + args.getPose() + " detected for "
						 + args.getUser());
						 profiledUsers.add(args.getUser());
						 
						 
						 /****/
												 
						 /***/
						 
				//poseDetectionCap.stopPoseDetection(args.getUser());
								}
			 catch (Exception e) {
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
