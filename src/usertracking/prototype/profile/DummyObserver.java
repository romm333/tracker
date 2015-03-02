package usertracking.prototype.profile;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.OpenNI.GeneralException;
import org.OpenNI.Plane3D;
import org.OpenNI.Point3D;
import org.OpenNI.SceneAnalyzer;
import org.OpenNI.SkeletonJoint;
import org.OpenNI.SkeletonJointPosition;
import org.OpenNI.StatusException;

import usertracking.prototype.classes.SimpleTracker;

public class DummyObserver implements Observer {
	private SimpleTracker tracker;
	public DummyObserver(SimpleTracker _tracker) {
		this.tracker = _tracker;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		try {
			int buffFrameId = -1;
			int[] users = tracker.userGen.getUsers();
			for (int i = 0; i < users.length; ++i) {
				if (tracker.skeletonCap.isSkeletonTracking(users[i]) && tracker.isRecognitionRequestedForUser(users[i])) {
					HashMap<SkeletonJoint, SkeletonJointPosition> dict = tracker
							.getJoints().get(users[i]);
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
						
												
						
						//interestingJoints.add(headPosition);
						//interestingJoints.add(neckPosition);
						interestingJoints.add(torsoPosition);
					
						interestingJoints.add(leftShoulder);
						interestingJoints.add(rightShoulder);

						interestingJoints.add(leftHip);
						interestingJoints.add(rightHip);

						interestingJoints.add(leftKnee);
						interestingJoints.add(rightKnee);

						interestingJoints.add(leftElbow);
						interestingJoints.add(rightElbow);

						//interestingJoints.add(lehtHand);
						//interestingJoints.add(rightHand);
						
						int frameId = tracker.depthGen.getFrameID();
						
						
						if (torsoPosition.getConfidence() == 1.0 && headPosition.getConfidence() == 1.0 && rightFoot.getConfidence() == 1.0 && buffFrameId != frameId) {
							 
							Point3D torsoPos = tracker.depthGen
									.convertProjectiveToRealWorld(torsoPosition
											.getPosition());

							Point3D neckPos = tracker.depthGen
									.convertProjectiveToRealWorld(neckPosition
											.getPosition());
							Point3D headPos = tracker.depthGen
									.convertProjectiveToRealWorld(headPosition
											.getPosition());
								
							
							Point3D rightHipPos = tracker.depthGen
									.convertProjectiveToRealWorld(rightHip
											.getPosition());

							Point3D leftHipPos = tracker.depthGen
									.convertProjectiveToRealWorld(leftHip
											.getPosition());
							
							Point3D rightKneePos = tracker.depthGen
									.convertProjectiveToRealWorld(rightKnee
											.getPosition());
	
							Point3D rightFootPos = tracker.depthGen
									.convertProjectiveToRealWorld(rightFoot
											.getPosition());
	
							Point3D rightShoulderPos = tracker.depthGen
									.convertProjectiveToRealWorld(rightShoulder
											.getPosition());

							Point3D leftShoulderPos = tracker.depthGen
									.convertProjectiveToRealWorld(leftShoulder
											.getPosition());
							
							Point3D leftElbowPos = tracker.depthGen
									.convertProjectiveToRealWorld(leftElbow
											.getPosition());
							
							Point3D rightElbowPos = tracker.depthGen
									.convertProjectiveToRealWorld(rightElbow
											.getPosition());
							
							Point3D leftHandPos = tracker.depthGen
									.convertProjectiveToRealWorld(lehtHand
											.getPosition());
							
							Point3D rightHandPos = tracker.depthGen
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
							
							try {
								SceneAnalyzer analyzer = SceneAnalyzer
										.create(tracker.userGen.getContext());
								Plane3D theFloor = analyzer.getFloor();
								
								
								
								System.out.println(users[i] + ", " + TC_TH + ", " + shoulders + ", " + TLSLS_TRSRH);
								
								
								
//								System.out.println(theFloor.getPoint().getZ()
//										+ ", " + theFloor.getPoint().getY()
//										+ ", " + torsoPos.getY() + ", " + headPos.getY());
								
								//System.out.println(users[i] + ", " + theFloor.getPoint().getY() + ", " + theFloor.getNormal().getX() + ", " + theFloor.getNormal().getX() +", "+ headPos.getY() + ", " + theFloor.getNormal().getZ() + ", " + neckPos.getY() + ", " + torsoPos.getY() + ", " + rightHipPos.getY()+ ", " + rightFootPos.getY());
								
								buffFrameId = frameId;

							} catch (GeneralException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
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
			}

		} catch (StatusException e) {

			e.printStackTrace();
		}
	}

	public static double getLenght(Point3D joint1, Point3D joint2) {
		return Math.sqrt(Math.pow(joint1.getX() - joint2.getX(), 2)
				+ Math.pow(joint1.getY() - joint2.getY(), 2)
				+ Math.pow(joint1.getZ() - joint2.getZ(), 2));
	}
	
	
	public static Point3D getTriangleCentroid(Point3D joint1, Point3D joint2,
			Point3D joint3) {
		Point3D retPoint = new Point3D();
		
		float X = (joint1.getX() + joint2.getX() + joint3.getX()) / 3;
		float Y = (joint1.getY() + joint2.getY() + joint3.getY()) / 3;
		float Z = (joint1.getZ() + joint2.getZ() + joint3.getZ()) / 3;

		retPoint.setPoint(X, Y, Z);
		return retPoint;
	}
	
	
	private String getPositionString(SkeletonJointPosition pos) {

		String ret = String.valueOf(pos.getPosition().getX()) + ","
				+ String.valueOf(pos.getPosition().getY()) + ","
				+ String.valueOf(pos.getPosition().getZ());
		return ret;
	}

	private String getPositionString(List<SkeletonJointPosition> positions) {

		String ret = "";
		for (SkeletonJointPosition onePosition : positions) {
			ret += getPositionString(onePosition) + ",";
		}

		return ret;
	}
}
