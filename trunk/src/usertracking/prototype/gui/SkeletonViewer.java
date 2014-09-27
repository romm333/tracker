package usertracking.prototype.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.HashMap;

import org.OpenNI.Point3D;
import org.OpenNI.SkeletonJoint;
import org.OpenNI.SkeletonJointPosition;
import org.OpenNI.StatusException;

import usertracking.prototype.classes.SimpleTracker;

public class SkeletonViewer extends Component {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SimpleTracker viewer;

	public void setViewer(SimpleTracker _viewer) {
		viewer = _viewer;
	}

	public SkeletonViewer() {
		int width = this.viewer.depthMD.getFullXRes();
		int heigth = this.viewer.depthMD.getFullYRes();
		this.setSize(width, heigth);
	}

	@Override
	public void paint(Graphics g) {
		drawSkeleton(g);
	}

	void drawSkeleton(Graphics g) {
		int[] users;
		try {
			users = viewer.userGen.getUsers();

			for (int i = 0; i < users.length; ++i) {
				Color c = Color.black;
				g.setColor(c);
				if (viewer.skeletonCap.isSkeletonTracking(users[i])) {
					drawSkeleton(g, users[i]);
					System.out.println("drawing");
				}
			}
		} catch (StatusException e) {

			e.printStackTrace();
		}
	}

	public void getJoint(int user, SkeletonJoint joint) throws StatusException {
		SkeletonJointPosition pos = viewer.skeletonCap
				.getSkeletonJointPosition(user, joint);
		if (pos.getPosition().getZ() != 0) {
			viewer.getJoints()
					.get(user)
					.put(joint,
							new SkeletonJointPosition(viewer.depthGen
									.convertRealWorldToProjective(pos
											.getPosition()), pos
									.getConfidence()));
		} else {
			viewer.getJoints().get(user)
					.put(joint, new SkeletonJointPosition(new Point3D(), 0));
		}
	}

	public void getJoints(int user) throws StatusException {
		getJoint(user, SkeletonJoint.HEAD);
		getJoint(user, SkeletonJoint.NECK);

		getJoint(user, SkeletonJoint.LEFT_SHOULDER);
		getJoint(user, SkeletonJoint.LEFT_ELBOW);
		getJoint(user, SkeletonJoint.LEFT_HAND);

		getJoint(user, SkeletonJoint.RIGHT_SHOULDER);
		getJoint(user, SkeletonJoint.RIGHT_ELBOW);
		getJoint(user, SkeletonJoint.RIGHT_HAND);

		getJoint(user, SkeletonJoint.TORSO);

		getJoint(user, SkeletonJoint.LEFT_HIP);
		getJoint(user, SkeletonJoint.LEFT_KNEE);
		getJoint(user, SkeletonJoint.LEFT_FOOT);

		getJoint(user, SkeletonJoint.RIGHT_HIP);
		getJoint(user, SkeletonJoint.RIGHT_KNEE);
		getJoint(user, SkeletonJoint.RIGHT_FOOT);

	}

	void drawLine(Graphics g,
			HashMap<SkeletonJoint, SkeletonJointPosition> jointHash,
			SkeletonJoint joint1, SkeletonJoint joint2) {
		Point3D pos1 = jointHash.get(joint1).getPosition();
		Point3D pos2 = jointHash.get(joint2).getPosition();

		if (jointHash.get(joint1).getConfidence() == 0
				|| jointHash.get(joint2).getConfidence() == 0)
			return;

		g.drawLine((int) pos1.getX(), (int) pos1.getY(), (int) pos2.getX(),
				(int) pos2.getY());
	}

	public void drawSkeleton(Graphics g, int user) throws StatusException {
		getJoints(user);
		HashMap<SkeletonJoint, SkeletonJointPosition> dict = viewer.getJoints()
				.get(new Integer(user));

		drawLine(g, dict, SkeletonJoint.HEAD, SkeletonJoint.NECK);

		drawLine(g, dict, SkeletonJoint.LEFT_SHOULDER, SkeletonJoint.TORSO);
		drawLine(g, dict, SkeletonJoint.RIGHT_SHOULDER, SkeletonJoint.TORSO);

		drawLine(g, dict, SkeletonJoint.NECK, SkeletonJoint.LEFT_SHOULDER);
		drawLine(g, dict, SkeletonJoint.LEFT_SHOULDER, SkeletonJoint.LEFT_ELBOW);
		drawLine(g, dict, SkeletonJoint.LEFT_ELBOW, SkeletonJoint.LEFT_HAND);

		drawLine(g, dict, SkeletonJoint.NECK, SkeletonJoint.RIGHT_SHOULDER);
		drawLine(g, dict, SkeletonJoint.RIGHT_SHOULDER,
				SkeletonJoint.RIGHT_ELBOW);
		drawLine(g, dict, SkeletonJoint.RIGHT_ELBOW, SkeletonJoint.RIGHT_HAND);

		drawLine(g, dict, SkeletonJoint.LEFT_HIP, SkeletonJoint.TORSO);
		drawLine(g, dict, SkeletonJoint.RIGHT_HIP, SkeletonJoint.TORSO);
		drawLine(g, dict, SkeletonJoint.LEFT_HIP, SkeletonJoint.RIGHT_HIP);

		drawLine(g, dict, SkeletonJoint.LEFT_HIP, SkeletonJoint.LEFT_KNEE);
		drawLine(g, dict, SkeletonJoint.LEFT_KNEE, SkeletonJoint.LEFT_FOOT);

		drawLine(g, dict, SkeletonJoint.RIGHT_HIP, SkeletonJoint.RIGHT_KNEE);
		drawLine(g, dict, SkeletonJoint.RIGHT_KNEE, SkeletonJoint.RIGHT_FOOT);

	}

}
