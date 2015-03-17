package usertracking.prototype.profile;

import java.util.ArrayList;
import java.util.Collections;
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

public class ProfileKmeansObserver implements Observer {
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
	
	public ProfileKmeansObserver(SimpleTracker _tracker) {
		this.tracker = _tracker;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		try {
			int buffFrameId = -1;
			int[] users = tracker.userGen.getUsers();
			for (int i = 0; i < users.length; ++i) {
				if (tracker.skeletonCap.isSkeletonTracking(users[i])
						&& tracker.isRecognitionRequestedForUser(users[i])
						&& !tracker.isUserRecognized(users[i])) {

					SkeletonJointPosition headPosition = tracker.skeletonCap
							.getSkeletonJointPosition(users[i],
									SkeletonJoint.HEAD);

					SkeletonJointPosition neckPosition = tracker.skeletonCap
							.getSkeletonJointPosition(users[i],
									SkeletonJoint.NECK);
					SkeletonJointPosition torsoPosition = tracker.skeletonCap
							.getSkeletonJointPosition(users[i],
									SkeletonJoint.TORSO);

					SkeletonJointPosition leftShoulder = tracker.skeletonCap
							.getSkeletonJointPosition(users[i],
									SkeletonJoint.LEFT_SHOULDER);
					SkeletonJointPosition rightShoulder = tracker.skeletonCap
							.getSkeletonJointPosition(users[i],
									SkeletonJoint.RIGHT_SHOULDER);

					SkeletonJointPosition leftHip = tracker.skeletonCap
							.getSkeletonJointPosition(users[i],
									SkeletonJoint.LEFT_HIP);
					SkeletonJointPosition rightHip = tracker.skeletonCap
							.getSkeletonJointPosition(users[i],
									SkeletonJoint.RIGHT_HIP);

					SkeletonJointPosition leftKnee = tracker.skeletonCap
							.getSkeletonJointPosition(users[i],
									SkeletonJoint.LEFT_KNEE);
					SkeletonJointPosition rightKnee = tracker.skeletonCap
							.getSkeletonJointPosition(users[i],
									SkeletonJoint.RIGHT_KNEE);

					SkeletonJointPosition leftElbow = tracker.skeletonCap
							.getSkeletonJointPosition(users[i],
									SkeletonJoint.LEFT_ELBOW);
					SkeletonJointPosition rightElbow = tracker.skeletonCap
							.getSkeletonJointPosition(users[i],
									SkeletonJoint.RIGHT_ELBOW);

					SkeletonJointPosition lehtHand = tracker.skeletonCap
							.getSkeletonJointPosition(users[i],
									SkeletonJoint.LEFT_HAND);
					SkeletonJointPosition rightHand = tracker.skeletonCap
							.getSkeletonJointPosition(users[i],
									SkeletonJoint.RIGHT_HAND);

					SkeletonJointPosition rightFoot = tracker.skeletonCap
							.getSkeletonJointPosition(users[i],
									SkeletonJoint.RIGHT_FOOT);
					SkeletonJointPosition leftFoot = tracker.skeletonCap
							.getSkeletonJointPosition(users[i],
									SkeletonJoint.LEFT_FOOT);

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
						
						

						if (tracker.isInRecordingMode()) {
//							System.out.println(users[i] + ", " + neckTorso
//									+ ", " + shoulders + ", " + hips + ", "
//									+ vDiagonal.length() + ", "
//									+ vRightSholderHip.length() + ", "
//									+ cvUperToHips.length() + ", "
//									+ cvLeftRighShouldersToHps.length() + ", "
//									+ anglLeftSh_leftHip + ", "
//									+ anglLeftHip_RightHip);
							
							 System.out.println(users[i] + ", " + vRightHipKnee.length() + ", " + vLeftHipKnee.length() +
									 ", " + vRightKneeFoot.length() + ", " + vLeftKneeFoot.length() );
							// leftElbowShoulder + ", " + anglLeftSh_leftElbow +
							// ", " + leftTorsoShoulder + ", " +
							// leftShoulderNeck);

							// System.out.println(users[i] + ", " +
							// torsoPos.getX() + ", " + torsoPos.getY() + ", " +
							// torsoPos.getZ());
							// System.out.println(users[i] + ", " +
							// vLeftShoulderNeck + ", " + vLeftElbowShoulder);
							// System.out.println(users[i] + ", " + TC_TH + ", "
							// + shoulders + ", " + TLSLS_TRSRH);
							// System.out.println(users[i] + ", " + torsoLS +
							// ", " + torsoRS + ", " + shoulders + ", " +
							// rightKneeToHip + ", " + leftKneeToHip);
						}

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
							DummyProfile recognizedProfile = tracker.getProfileMeans().get(minIndex);
							tracker.getRecognizedProfiles().put(users[i], recognizedProfile);
							//System.out.println(recognizedProfile.profileMean.toString());
							System.out.println(Collections.min(distances));
							
							//tracker.setRecognizedUser(users[i],0d);
						
						}

					}

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public Double getProfileDistance(JointVector jv, Integer profileMeanIndex){
		return tracker.getProfileMeans().get(profileMeanIndex).profileMean.getSquareOfDistance(jv);
	}
}
