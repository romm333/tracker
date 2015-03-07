package usertracking.prototype.profile;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

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

public class DummyObserver implements Observer {
	private SimpleTracker tracker;
	
	private ProfileKMeans profileData;
	private List<JointCluster> user1Joints;
	
	
	
	public DummyObserver(SimpleTracker _tracker) {
		this.tracker = _tracker;
		profileData = new ProfileKMeans("profiles/1.csv", 4);
		user1Joints = profileData.getJointsClusters();
		
		for (int i = 0; i < profileData.k; i++) {
			System.out.println("Cluster " + i + ": "
					+ user1Joints.get(i).getCentroid() + " " + user1Joints.get(i).getJointVectors().size());
		}
		
	}
	
	
 
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		try {
			int buffFrameId = -1;
			int[] users = tracker.userGen.getUsers();
			for (int i = 0; i < users.length; ++i) {
				if (tracker.skeletonCap.isSkeletonTracking(users[i]) && tracker.isRecognitionRequestedForUser(users[i]) && !tracker.isUserRecognized(users[i])) {
						
						SkeletonJointPosition headPosition = tracker.skeletonCap.getSkeletonJointPosition(users[i],SkeletonJoint.HEAD);
								
						SkeletonJointPosition neckPosition = tracker.skeletonCap.getSkeletonJointPosition(users[i],SkeletonJoint.NECK);
						SkeletonJointPosition torsoPosition = tracker.skeletonCap.getSkeletonJointPosition(users[i],SkeletonJoint.TORSO);

						SkeletonJointPosition leftShoulder = tracker.skeletonCap.getSkeletonJointPosition(users[i],SkeletonJoint.LEFT_SHOULDER);
						SkeletonJointPosition rightShoulder = tracker.skeletonCap.getSkeletonJointPosition(users[i],SkeletonJoint.RIGHT_SHOULDER);

						SkeletonJointPosition leftHip = tracker.skeletonCap.getSkeletonJointPosition(users[i],SkeletonJoint.LEFT_HIP);
						SkeletonJointPosition rightHip = tracker.skeletonCap.getSkeletonJointPosition(users[i],SkeletonJoint.RIGHT_HIP);

						SkeletonJointPosition leftKnee = tracker.skeletonCap.getSkeletonJointPosition(users[i],SkeletonJoint.LEFT_KNEE);
						SkeletonJointPosition rightKnee = tracker.skeletonCap.getSkeletonJointPosition(users[i],SkeletonJoint.RIGHT_KNEE);

						SkeletonJointPosition leftElbow = tracker.skeletonCap.getSkeletonJointPosition(users[i],SkeletonJoint.LEFT_ELBOW);
						SkeletonJointPosition rightElbow = tracker.skeletonCap.getSkeletonJointPosition(users[i],SkeletonJoint.RIGHT_ELBOW);

						SkeletonJointPosition lehtHand = tracker.skeletonCap.getSkeletonJointPosition(users[i],SkeletonJoint.LEFT_HAND);
						SkeletonJointPosition rightHand = tracker.skeletonCap.getSkeletonJointPosition(users[i],SkeletonJoint.RIGHT_HAND);
						
						SkeletonJointPosition rightFoot = tracker.skeletonCap.getSkeletonJointPosition(users[i],SkeletonJoint.RIGHT_FOOT);
						SkeletonJointPosition leftFoot = tracker.skeletonCap.getSkeletonJointPosition(users[i],SkeletonJoint.LEFT_FOOT);
						
												
						int frameId = tracker.depthGen.getFrameID();
						
						
						if (torsoPosition.getConfidence() == 1.0 && headPosition.getConfidence() == 1.0 && rightFoot.getConfidence() == 1.0 && leftElbow.getConfidence()== 1.0
								&& rightElbow.getConfidence()== 1.0 && buffFrameId != frameId) {
							 
							Point3D torsoPos = torsoPosition.getPosition();

							Point3D neckPos = neckPosition.getPosition();
							Point3D headPos = headPosition.getPosition();
							Point3D rightHipPos = rightHip.getPosition();

							Point3D leftHipPos = leftHip.getPosition();
							Point3D rightKneePos = rightKnee.getPosition();
							Point3D leftKneePos =leftKnee.getPosition();
	
							Point3D rightFootPos = rightFoot.getPosition();
							Point3D rightShoulderPos = rightShoulder.getPosition();
							Point3D leftShoulderPos = leftShoulder.getPosition();
							
							Point3D leftElbowPos = leftElbow.getPosition();
							Point3D rightElbowPos = rightElbow.getPosition();
							
							Point3D leftHandPos = lehtHand.getPosition();
							Point3D rightHandPos = rightHand.getPosition();
							
							//vectors 
							Vector3d vShoulders = ProfileMath.getVector3d(leftShoulderPos,rightShoulderPos);
							Vector3d vHips = ProfileMath.getVector3d(leftHipPos,rightHipPos);
							
							Vector3d vHeadNeck = ProfileMath.getVector3d(neckPos,headPos);
							Vector3d vHeadTorso = ProfileMath.getVector3d(headPos,torsoPos);
							Vector3d vNeckTorso = ProfileMath.getVector3d(neckPos,torsoPos);
							
							
							//left 
							Vector3d vLeftShoulderNeck = ProfileMath.getVector3d(neckPos, leftShoulderPos);
							Vector3d vLeftTorsoShoulder = ProfileMath.getVector3d(torsoPos, leftShoulderPos);
							
							Vector3d vLeftElbowShoulder = ProfileMath.getVector3d(leftElbowPos, leftShoulderPos);
							Vector3d vLeftShoulderHand = ProfileMath.getVector3d(leftShoulderPos, leftHandPos);
							
							Vector3d vLeftTorsoHip = ProfileMath.getVector3d(torsoPos, leftHipPos);
							Vector3d vLeftSholderHip = ProfileMath.getVector3d(leftShoulderPos, leftHipPos);
							
							//right 
							Vector3d vRightShoulderNeck = ProfileMath.getVector3d(neckPos, rightShoulderPos);
							Vector3d vRightTorsoShoulder = ProfileMath.getVector3d(torsoPos, rightShoulderPos);
							
							Vector3d vRightElbowShoulder = ProfileMath.getVector3d(leftElbowPos, rightShoulderPos);
							Vector3d vRightShoulderHand = ProfileMath.getVector3d(leftShoulderPos, rightHandPos);
							
							Vector3d vRightTorsoHip = ProfileMath.getVector3d(torsoPos, rightHipPos);
							Vector3d vRightSholderHip = ProfileMath.getVector3d(rightShoulderPos, rightHipPos);
							
							double shoulders = vShoulders.length();
							double neck = vHeadNeck.length();
							double neckTorso = vNeckTorso.length();
							double headTorso = vHeadTorso.length();
							
							
							double leftShoulderNeck = vLeftShoulderNeck.length(); 
							double leftTorsoShoulder = vLeftTorsoShoulder.length();
							double leftElbowShoulder = vLeftElbowShoulder.length();
							
							double leftShoulderHand = vLeftShoulderHand.length();
							double leftTorsoHip = vLeftTorsoHip.length();
							
							double rightShoulderNeck = vRightShoulderNeck.length();
							double rightTorsoShoulder = vRightTorsoShoulder.length();
							double rightElbowShoulder = vRightElbowShoulder.length();
							
							double rightShoulderHand = vRightShoulderHand.length();
							double rightTorsoHip = vRightTorsoHip.length();
							
							double anglLeftSh_leftElbow = ProfileMath.getVectorAngleDeg(vLeftShoulderNeck, vLeftElbowShoulder);
							double anglLeftSh_torso = ProfileMath.getVectorAngleDeg(vLeftShoulderNeck, vLeftTorsoShoulder);
							double anglNeck_rightShouler = ProfileMath.getVectorAngleDeg(vHeadNeck, vRightShoulderNeck);
							
							double anglLeftHip_RightHip = ProfileMath.getVectorAngleDeg(vLeftTorsoHip, vRightTorsoHip);
							double anglLeftSh_leftHip = ProfileMath.getVectorAngleDeg(vLeftShoulderNeck, vLeftSholderHip);
							double anglRightSh_rightHip = ProfileMath.getVectorAngleDeg(vRightShoulderNeck, vRightShoulderNeck);
							
							
							
//							double torsoLS = getLenght(torsoPos, leftShoulderPos);
//							double torsoRS = getLenght(torsoPos, rightShoulderPos);
//							
//							double leftShoulderToElbow = getLenght(leftShoulderPos, leftElbowPos);
//							double lefttShoulderToHand = getLenght(leftShoulderPos, leftHandPos);
//							double leftHandToShoulder = getLenght(leftShoulderPos, leftHandPos);
//							
//							double rightShoulderToElbow = getLenght(rightShoulderPos, rightElbowPos);
//							double rightShoulderToHand = getLenght(rightShoulderPos, rightHandPos);
//							double rightHandToShoulder = getLenght(rightShoulderPos, rightHandPos);
//							
//							double rightKneeToHip = getLenght(rightHipPos, rightKneePos);
//							double leftKneeToHip = getLenght(leftHipPos, leftKneePos);
//							
//							Point3D TC = ProfileMath.getTriangleCentroid(torsoPos,leftShoulderPos,rightShoulderPos);
//							Point3D LSHC = ProfileMath.getTriangleCentroid(leftShoulderPos,leftElbowPos,leftHandPos);
//							Point3D RSHC = ProfileMath.getTriangleCentroid(rightShoulderPos,rightElbowPos,rightHandPos);
//							
//							Point3D TLSLS = ProfileMath.getTriangleCentroid(torsoPos,leftShoulderPos,leftHipPos);
//							Point3D TRSRH = ProfileMath.getTriangleCentroid(torsoPos,rightShoulderPos,rightHipPos);
//							
//
//							Point3D TH = ProfileMath.getTriangleCentroid(torsoPos,rightHipPos,leftHipPos);
//							
//							double TC_TH = getLenght(TC, TH);
//							double TLSLS_TRSRH = getLenght(TLSLS, TRSRH);
//							
//							double TC_LSHC = getLenght(TC, LSHC);
//							double TC_RSHC = getLenght(TC, RSHC);
//							double LSHC_RSHC = getLenght(LSHC,RSHC);
							
							
							if (tracker.isInRecordingMode()) {
								System.out.println(users[i] + ", " + neck + ", " + neckTorso + ", " + headTorso + ", " + shoulders + ", " + anglNeck_rightShouler + ", " + vLeftSholderHip.length() + ", " + vRightSholderHip.length()); 
								
								
								//System.out.println(users[i] + ", " + leftElbowShoulder + ", " + anglLeftSh_leftElbow + ", " + leftTorsoShoulder + ", " + leftShoulderNeck);
								
								//System.out.println(users[i] + ", " + torsoPos.getX() + ", " + torsoPos.getY() + ", " + torsoPos.getZ());
								//System.out.println(users[i] + ", " + vLeftShoulderNeck + ", " + vLeftElbowShoulder);
								//System.out.println(users[i] + ", " + TC_TH + ", " + shoulders + ", " + TLSLS_TRSRH);
								//System.out.println(users[i] + ", " + torsoLS + ", " + torsoRS + ", " + shoulders + ", " +  rightKneeToHip + ", " + leftKneeToHip);
							}
							
//							if (tracker.isInProfilingMode()) {
//								double[] distances = new double[4];
//								JointVector jv = new JointVector(TC_TH,
//										shoulders, TLSLS_TRSRH, TLSLS_TRSRH);
//
//								for (int k = 0; k < profileData.k; k++) {
//									double dd = user1Joints.get(k)
//											.getCentroid()
//											.getSquareOfDistance(jv);
//									distances[k] = dd;
//									System.out.println(dd + ", " + user1Joints.get(k)
//											.getCentroid());
//									System.out.println(users[i] + ", " + TC_TH + ", " + shoulders + ", " + TLSLS_TRSRH);
//								}
//
//								double minDistance = Math.min(Math.min(
//										distances[0], distances[1]),
//										Math.min(distances[2],distances[3]));
//
//								int profileIndex = -1;
//								for (int j = 0; j < 4; j++) {
//									if (distances[j] == minDistance) {
//										profileIndex = j;
//									}
//								}
//
//								System.out
//										.println("Recognized user distance to profile centroid:"
//												+ minDistance
//												+ ", "
//												+ user1Joints.get(
//														profileIndex)
//														.getCentroid());
//								tracker.setRecognizedUser(users[i],
//										minDistance);
//							}
//								System.out.println(theFloor.getPoint().getZ()
//										+ ", " + theFloor.getPoint().getY()
//										+ ", " + torsoPos.getY() + ", " + headPos.getY());
							
							//System.out.println(users[i] + ", " + theFloor.getPoint().getY() + ", " + theFloor.getNormal().getX() + ", " + theFloor.getNormal().getX() +", "+ headPos.getY() + ", " + theFloor.getNormal().getZ() + ", " + neckPos.getY() + ", " + torsoPos.getY() + ", " + rightHipPos.getY()+ ", " + rightFootPos.getY());
							
							buffFrameId = frameId;
						}
//						if (headPosition.getConfidence() == 1.0
//								&& torsoPosition.getConfidence()== 1.0
//								&& leftShoulder.getConfidence()== 1.0
//								&& rightShoulder.getConfidence()== 1.0 
//								&& leftHip.getConfidence()== 1.0
//								&& rightHip.getConfidence()== 1.0 
//								&& leftKnee.getConfidence()== 1.0
//								&& rightKnee.getConfidence()== 1.0 
//								&& leftElbow.getConfidence()== 1.0
//								&& rightElbow.getConfidence()== 1.0
//								&& (Math.sqrt(Math.pow((leftShoulderPos.getZ() - rightShoulderPos.getZ()),2))<=5)
//								) {
//
//							
//							double shouldersLength = getLenght(leftShoulderPos, rightShoulderPos);
//							double hipLength = getLenght(leftHip.getPosition(), rightShoulder.getPosition());
//							
//							
//							
//							//System.out.println(shouldersLength + ", " + hipLength);
//						}

					}
				}

		} catch (StatusException e) {

			e.printStackTrace();
		}
	}
	}
