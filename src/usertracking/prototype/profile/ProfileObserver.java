package usertracking.prototype.profile;

import java.lang.invoke.SwitchPoint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.vecmath.Vector3d;

import org.OpenNI.GeneralException;
import org.OpenNI.Plane3D;
import org.OpenNI.Point3D;
import org.OpenNI.SceneAnalyzer;
import org.OpenNI.SkeletonJoint;
import org.OpenNI.SkeletonJointPosition;
import org.OpenNI.StatusException;

import usertracking.prototype.classes.SimpleTracker;
import usertracking.prototype.kmeans.JointCluster;
import usertracking.prototype.kmeans.JointVector;
import usertracking.prototype.kmeans.ProfileKMeans;

public abstract class ProfileObserver implements Observer {
	private SimpleTracker tracker;
	/*joints*/
	SkeletonJointPosition headPosition;

	SkeletonJointPosition neckPosition;
	SkeletonJointPosition torsoPosition;

	SkeletonJointPosition leftShoulder;
	SkeletonJointPosition rightShoulder;

	SkeletonJointPosition leftHip;
	SkeletonJointPosition rightHip;

	SkeletonJointPosition leftKnee;
	SkeletonJointPosition rightKnee;

	SkeletonJointPosition leftElbow;
	SkeletonJointPosition rightElbow;

	SkeletonJointPosition lehtHand;
	SkeletonJointPosition rightHand;

	SkeletonJointPosition rightFoot;
	SkeletonJointPosition leftFoot;
	
	
	HashMap<Integer, Integer[][]> userProfileScore;
	
	public SimpleTracker getTracker(){
		return tracker;
	}
	
	private void initJointData(int uid) throws StatusException {
		headPosition = tracker.skeletonCap.getSkeletonJointPosition(uid,
				SkeletonJoint.HEAD);

		neckPosition = tracker.skeletonCap.getSkeletonJointPosition(uid,
				SkeletonJoint.NECK);
		torsoPosition = tracker.skeletonCap.getSkeletonJointPosition(uid,
				SkeletonJoint.TORSO);
		leftShoulder = tracker.skeletonCap.getSkeletonJointPosition(uid,
				SkeletonJoint.LEFT_SHOULDER);
		rightShoulder = tracker.skeletonCap.getSkeletonJointPosition(uid,
				SkeletonJoint.RIGHT_SHOULDER);

		leftHip = tracker.skeletonCap.getSkeletonJointPosition(uid,
				SkeletonJoint.LEFT_HIP);
		rightHip = tracker.skeletonCap.getSkeletonJointPosition(uid,
				SkeletonJoint.RIGHT_HIP);

		leftKnee = tracker.skeletonCap.getSkeletonJointPosition(uid,
				SkeletonJoint.LEFT_KNEE);
		rightKnee = tracker.skeletonCap.getSkeletonJointPosition(uid,
				SkeletonJoint.RIGHT_KNEE);

		leftElbow = tracker.skeletonCap.getSkeletonJointPosition(uid,
				SkeletonJoint.LEFT_ELBOW);
		rightElbow = tracker.skeletonCap.getSkeletonJointPosition(uid,
				SkeletonJoint.RIGHT_ELBOW);

		lehtHand = tracker.skeletonCap.getSkeletonJointPosition(uid,
				SkeletonJoint.LEFT_HAND);
		rightHand = tracker.skeletonCap.getSkeletonJointPosition(uid,
				SkeletonJoint.RIGHT_HAND);

		rightFoot = tracker.skeletonCap.getSkeletonJointPosition(uid,
				SkeletonJoint.RIGHT_FOOT);
		leftFoot = tracker.skeletonCap.getSkeletonJointPosition(uid,
				SkeletonJoint.LEFT_FOOT);
	}
	 
	public ProfileObserver(SimpleTracker _tracker) {
		this.tracker = _tracker;
		initProfileScore();
	}
	
	
	private void initProfileScore(){
		int profilesCount = this.getTracker().getProfileMeans().size();
		
		Integer[][] hitArray = new Integer[profilesCount][1];
		for(int i = 0; i < hitArray.length; i++ ){
			hitArray[i][0] = 0;
		}
		
		userProfileScore = new HashMap<Integer, Integer[][]>();
		
		for(int i=0; i<8;i++){
			
			userProfileScore.put(i, hitArray);
		}
	}
	
	private void updateProfileScore(int uid, int profileIndex){
		Integer[][] buffArray = userProfileScore.get(uid);
		int  buff =  buffArray[profileIndex][0];
		buff++;
		
		buffArray[profileIndex][0] = buff;
		userProfileScore.put(uid, buffArray);
	}
	
	private int getTopProfileScore(int uid, int delta){
		Integer[][] buffArray = userProfileScore.get(uid);
		int topCountIndex = getTopCountIndex(buffArray);
		int topHitCount = buffArray[topCountIndex][0];
		
		int diff = Integer.MIN_VALUE;
		
		for (int i=0; i < buffArray.length;i++){
			if(i==topCountIndex)
				continue;
			
			diff = topHitCount - buffArray[i][0];
		}
		
		if (diff > delta)
			return topCountIndex;
		
		return -1;
	}
	
	private int getTopCountIndex(Integer[][] hits) {
		int swap = Integer.MIN_VALUE;
		int maxIndex = Integer.MIN_VALUE;
		
		for(int i=0; i < hits.length; i++){
			int buff = hits[i][0];
			
			if(buff < swap)
				continue;
			
			swap = buff;
			maxIndex = i;
		}
		return maxIndex;
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		try {
			
			int buffFrameId = -1;
			int[] users = tracker.userGen.getUsers();
			for (int i = 0; i < users.length; ++i) {
				if (tracker.skeletonCap.isSkeletonTracking(users[i])) {
					
					initJointData(users[i]);

					int frameId = tracker.depthGen.getFrameID();

					if (torsoPosition.getConfidence() == 1.0
							&& headPosition.getConfidence() == 1.0
							&& rightKnee.getConfidence() == 1.0
							&& leftKnee.getConfidence() == 1.0
							&& rightHip.getConfidence() == 1.0
							&& leftHip.getConfidence() == 1.0
							&& rightFoot.getConfidence() == 1.0
							&& leftFoot.getConfidence() == 1.0
							&& leftElbow.getConfidence() == 1.0
							&& rightElbow.getConfidence() == 1.0
							&& buffFrameId != frameId) {

						Point3D torsoPos = torsoPosition.getPosition();

						Point3D neckPos = neckPosition.getPosition();
						Point3D headPos = headPosition.getPosition();
						Point3D rightHipPos = rightHip.getPosition();

						Point3D leftHipPos = leftHip.getPosition();
						Point3D rightKneePos = rightKnee.getPosition();
						Point3D leftKneePos = leftKnee.getPosition();

						Point3D rightFootPos = rightFoot.getPosition();
						Point3D leftFootPos=leftFoot.getPosition();
						
						Point3D rightSholderPos = rightShoulder.getPosition();
						Point3D leftSholderPos = leftShoulder.getPosition();

						Point3D leftElbowPos = leftElbow.getPosition();
						Point3D rightElbowPos = rightElbow.getPosition();

						Point3D leftHandPos = lehtHand.getPosition();
						Point3D rightHandPos = rightHand.getPosition();

						// vectors
						Vector3d vShoulders = ProfileMath.getVector3d(
								leftSholderPos, rightSholderPos);
						Vector3d vHips = ProfileMath.getVector3d(leftHipPos,
								rightHipPos);

						Vector3d vHeadNeck = ProfileMath.getVector3d(neckPos,
								headPos);
						Vector3d vHeadTorso = ProfileMath.getVector3d(headPos,
								torsoPos);
						Vector3d vNeckTorso = ProfileMath.getVector3d(neckPos,
								torsoPos);

						// left
						Vector3d vLeftShoulderNeck = ProfileMath.getVector3d(
								neckPos, leftSholderPos);
						Vector3d vLeftTorsoShoulder = ProfileMath.getVector3d(
								torsoPos, leftSholderPos);

						Vector3d vLeftElbowShoulder = ProfileMath.getVector3d(
								leftElbowPos, leftSholderPos);
						Vector3d vLeftShoulderHand = ProfileMath.getVector3d(
								leftSholderPos, leftHandPos);

						Vector3d vLeftTorsoHip = ProfileMath.getVector3d(
								torsoPos, leftHipPos);
						Vector3d vLeftSholderHip = ProfileMath.getVector3d(
								leftSholderPos, leftHipPos);

						// right
						Vector3d vRightSholderNeck = ProfileMath.getVector3d(
								neckPos, rightSholderPos);
						Vector3d vRightTorsoShoulder = ProfileMath.getVector3d(
								torsoPos, rightSholderPos);

						Vector3d vRightElbowShoulder = ProfileMath.getVector3d(
								leftElbowPos, rightSholderPos);
						Vector3d vRightShoulderHand = ProfileMath.getVector3d(
								leftSholderPos, rightHandPos);

						Vector3d vRightTorsoHip = ProfileMath.getVector3d(
								torsoPos, rightHipPos);
						Vector3d vRightSholderHip = ProfileMath.getVector3d(
								rightSholderPos, rightHipPos);
						Vector3d vDiagonal = ProfileMath.getVector3d(
								rightSholderPos, leftHipPos);
						
						Vector3d vRightHipKnee = ProfileMath.getVector3d(rightHipPos, rightKneePos);
						Vector3d vLeftHipKnee = ProfileMath.getVector3d(leftHipPos, leftKneePos);
						
						Vector3d vRightKneeFoot = ProfileMath.getVector3d(rightKneePos, rightFootPos);
						Vector3d vLeftKneeFoot = ProfileMath.getVector3d(leftKneePos, leftFootPos);

						double shoulders = vShoulders.length();
						double hips = vHips.length();
						double neck = vHeadNeck.length();
						double neckTorso = vNeckTorso.length();
						double headTorso = vHeadTorso.length();

						double leftShoulderNeck = vLeftShoulderNeck.length();
						double leftTorsoShoulder = vLeftTorsoShoulder.length();
						double leftElbowShoulder = vLeftElbowShoulder.length();

						double leftShoulderHand = vLeftShoulderHand.length();
						double leftTorsoHip = vLeftTorsoHip.length();

						double rightShoulderNeck = vRightSholderNeck.length();
						double rightTorsoShoulder = vRightTorsoShoulder
								.length();
						double rightElbowShoulder = vRightElbowShoulder
								.length();

						double rightShoulderHand = vRightShoulderHand.length();
						double rightTorsoHip = vRightTorsoHip.length();

						double anglLeftSh_leftElbow = ProfileMath
								.getVectorAngleDeg(vLeftShoulderNeck,
										vLeftElbowShoulder);
						double anglLeftSh_torso = ProfileMath
								.getVectorAngleDeg(vLeftShoulderNeck,
										vLeftTorsoShoulder);
						double anglNeck_rightShouler = ProfileMath
								.getVectorAngleDeg(vHeadNeck, vRightSholderNeck);

						double anglLeftHip_RightHip = ProfileMath
								.getVectorAngleDeg(vLeftTorsoHip,
										vRightTorsoHip);
						double anglLeftSh_leftHip = ProfileMath
								.getVectorAngleDeg(vLeftSholderHip,
										vLeftShoulderNeck);
						double anglRightSh_rightHip = ProfileMath
								.getVectorAngleDeg(vRightSholderHip,
										vRightSholderNeck);

						Point3D TC = ProfileMath.getTriangleCentroid(torsoPos,
								leftSholderPos, rightSholderPos);
						Point3D TH = ProfileMath.getTriangleCentroid(torsoPos,
								rightHipPos, leftHipPos);

						Vector3d cvUperToHips = ProfileMath.getVector3d(TC, TH);

						Point3D TLSLS = ProfileMath.getTriangleCentroid(
								torsoPos, leftSholderPos, leftHipPos);
						Point3D TRSRH = ProfileMath.getTriangleCentroid(
								torsoPos, rightSholderPos, rightHipPos);
						Vector3d cvLeftRighShouldersToHps = ProfileMath
								.getVector3d(TLSLS, TRSRH);
						
						if (tracker.isInProfilingMode()) {
							int meansCount = tracker.getProfileMeans().size();
							
							List<Double> distances = new ArrayList<Double>(meansCount);
							
							JointVector jv = new JointVector(neckTorso,
									shoulders, hips, vDiagonal.length(),
									vRightSholderHip.length(),
									cvUperToHips.length(),
									cvLeftRighShouldersToHps.length(),
									anglLeftSh_leftHip, anglLeftHip_RightHip,
									0d, 0d, 0d);
							
							for (int k = 0; k <meansCount; k++) {
								double dd = getProfileDistance(jv,k);
								distances.add(dd);
								//System.out.println(dd);
							}
							
							int minIndex = distances.indexOf(Collections.min(distances));
							updateProfileScore(users[i], minIndex);
							int topScoreProfile = getTopProfileScore(users[i], 300);
							
							
							
							DummyProfile recognizedProfile = tracker.getProfileMeans().get(minIndex);
							
							//System.out.println(topScoreProfile + ", " + minIndex + ", " + recognizedProfile.profileMean);
							
							if(minIndex == topScoreProfile){
								tracker.getRecognizedProfiles().put(users[i], recognizedProfile);
								System.out.println(recognizedProfile.profileMean.toString());
							}
							//System.out.println(Collections.min(distances));
							
							//tracker.setRecognizedUser(users[i],0d);
						}

					}

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getLocalizedMessage());
		}
	}
	public abstract Double getProfileDistance(JointVector jv, Integer profileMeanIndex);
}
