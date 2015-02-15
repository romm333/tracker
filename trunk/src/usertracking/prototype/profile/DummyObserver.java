package usertracking.prototype.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.OpenNI.SkeletonJoint;
import org.OpenNI.SkeletonJointPosition;
import org.OpenNI.StatusException;

import failures.UserProfileBase;
import usertracking.prototype.classes.SimpleTracker;

public class DummyObserver implements Observer {
	private SimpleTracker tracker;
	private long start_time = System.currentTimeMillis();
	private long time_diff = 0;

	public DummyObserver(SimpleTracker _tracker) {
		this.tracker = _tracker;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		try {

			int[] users = tracker.userGen.getUsers();
			for (int i = 0; i < users.length; ++i) {
				if (tracker.skeletonCap.isSkeletonTracking(users[i])) {
					HashMap<SkeletonJoint, SkeletonJointPosition> dict = tracker
							.getJoints().get(users[i]);
					if (dict.size() > 0) {

						SkeletonJointPosition headPosition = dict
								.get(SkeletonJoint.HEAD);
						SkeletonJointPosition neckPosition = dict
								.get(SkeletonJoint.NECK);
						SkeletonJointPosition torsoPosition = dict
								.get(SkeletonJoint.TORSO);
//						SkeletonJointPosition waist = dict
//								.get(SkeletonJoint.WAIST);

						SkeletonJointPosition leftShoulder = dict
								.get(SkeletonJoint.LEFT_SHOULDER);
						SkeletonJointPosition rightShoulder = dict
								.get(SkeletonJoint.RIGHT_SHOULDER);

						SkeletonJointPosition leftHeap = dict
								.get(SkeletonJoint.LEFT_HIP);
						SkeletonJointPosition rightHeap = dict
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

						List<SkeletonJointPosition> interestingJoints = new LinkedList<SkeletonJointPosition>();
						
						interestingJoints.add(headPosition);
						interestingJoints.add(neckPosition);
						interestingJoints.add(torsoPosition);
						//interestingJoints.add(waist);

						interestingJoints.add(leftShoulder);
						interestingJoints.add(rightShoulder);

						interestingJoints.add(leftHeap);
						interestingJoints.add(rightHeap);

						interestingJoints.add(leftKnee);
						interestingJoints.add(rightKnee);

						interestingJoints.add(leftElbow);
						interestingJoints.add(rightElbow);
|
						interestingJoints.add(lehtHand);
						interestingJoints.add(rightHand);

						if (headPosition.getConfidence() > 0
								&& neckPosition.getConfidence() > 0
								&& torsoPosition.getConfidence() > 0
								//&& waist.getConfidence() > 0
								&& leftShoulder.getConfidence() > 0
								&& rightShoulder.getConfidence() > 0 
								&& leftHeap.getConfidence() > 0
								&& rightHeap.getConfidence() > 0 
								&& leftKnee.getConfidence() > 0
								&& rightKnee.getConfidence() > 0 
								&& leftElbow.getConfidence() > 0
								&& rightElbow.getConfidence() > 0 
								//&& leftCollar.getConfidence() > 0
								//&& rightCollar.getConfidence() > 0 
								//&& leftAnkle.getConfidence() > 0
								//&& rightAnkle.getConfidence() > 0 
								&& lehtHand.getConfidence() > 0
								&& rightHand.getConfidence() > 0) {

							String tt = getPositionString(interestingJoints);

							System.out.println(tt);
						}

					}
				}
			}

		} catch (StatusException e) {

			e.printStackTrace();
		}
	}

	private String getPositionString(SkeletonJointPosition pos) {

		String ret = String.valueOf(pos.getConfidence()) + ","
				+ String.valueOf(pos.getPosition().getX()) + ","
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
