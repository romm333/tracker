package usertracking.prototype.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.OpenNI.Point3D;
import org.OpenNI.SkeletonJoint;
import org.OpenNI.SkeletonJointPosition;
import org.OpenNI.StatusException;

import usertracking.prototype.classes.DataLogger;
import usertracking.prototype.classes.SimpleTracker;
import usertracking.prototype.classes.UserProfile;
import usertracking.prototype.classes.UserProfiler;

public class SkeletonTopFacade extends Component {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SimpleTracker tracker;

	private boolean printID = true;
	private boolean printState = true;

	public SkeletonTopFacade(SimpleTracker _traker) {
		this.tracker = _traker;
		this.setBackground(Color.black);
	}

	private float prevPixels = 0;
	private float pixResult = 0;

	@Override
	public void paint(Graphics g) {
		drawSkeleton(g);
	}

	Color colors[] = { Color.RED, Color.BLUE, Color.CYAN, Color.GREEN,
			Color.MAGENTA, Color.PINK, Color.YELLOW, Color.WHITE };

	void drawSkeleton(Graphics g) {
		int[] users;

		try {
			users = tracker.userGen.getUsers();

			for (int i = 0; i < users.length; ++i) {
				Color c = colors[users[i] % colors.length];
				c = new Color(255 - c.getRed(), 255 - c.getGreen(),
						255 - c.getBlue());

				g.setColor(c);
				if (tracker.skeletonCap.isSkeletonTracking(users[i])) {
					g.fillRect(0, 75, 5, 50);

					drawSkeleton(g, users[i]);
				}

				if (printID) {

					Point3D com = tracker.depthGen
							.convertRealWorldToProjective(tracker.userGen
									.getUserCoM(users[i]));
					String label = null;
					if (!printState) {
						label = new String("" + users[i]);
					} else if (tracker.skeletonCap.isSkeletonTracking(users[i])) {
						// Tracking
						SkeletonJointPosition posRightShoulder = tracker.skeletonCap
								.getSkeletonJointPosition(users[i],
										SkeletonJoint.RIGHT_SHOULDER);
						SkeletonJointPosition posNeck = tracker.skeletonCap
								.getSkeletonJointPosition(users[i],
										SkeletonJoint.NECK);
						SkeletonJointPosition posLeftShoulder = tracker.skeletonCap
								.getSkeletonJointPosition(users[i],
										SkeletonJoint.LEFT_SHOULDER);
						SkeletonJointPosition posTorso = tracker.skeletonCap
								.getSkeletonJointPosition(users[i],
										SkeletonJoint.TORSO);
						SkeletonJointPosition posHead = tracker.skeletonCap
								.getSkeletonJointPosition(users[i],
										SkeletonJoint.HEAD);

						List<Point3D> profileJoints = new LinkedList<Point3D>();

						profileJoints.add(posTorso.getPosition());
						profileJoints.add(posNeck.getPosition());
						profileJoints.add(posLeftShoulder.getPosition());
						profileJoints.add(posRightShoulder.getPosition());
						profileJoints.add(posHead.getPosition());

						double jointLengthSum = tracker.userProfiler
								.getVectorLength(profileJoints);
						label = new String(users[i] + " - Tracking - "
								+ jointLengthSum);

						UserProfile existingProfile = tracker
								.getMatchingUserProfile(users[i]);
						if (existingProfile != null) {
							label = new String(users[i] + " - Tracking - "
									+ existingProfile.getProfileName());
						}
					} else if (tracker.skeletonCap
							.isSkeletonCalibrating(users[i])) {
						// Calibrating
						label = new String(users[i] + " - Calibrating");
					} else {
						// Nothing
						label = new String(users[i] + " - Looking for pose ("
								+ tracker.calibPose + ")");
					}

					g.drawString(label, (int) com.getZ() / 10, (int) 125);

					// float tempPixels = com.getZ();
					//
					// if (tempPixels > prevPixels)
					// pixResult = tempPixels - prevPixels;
					// if (prevPixels > tempPixels)
					// pixResult = prevPixels - tempPixels;
					//
					// double toDraw = Math.floor(pixResult/3);
					//
					// g.drawString("speed:" + toDraw, 200, 180);
					// prevPixels = tempPixels;

				}
			}
		} catch (StatusException e) {

			e.printStackTrace();
		}
	}

	public void getJoint(int user, SkeletonJoint joint) throws StatusException {
		SkeletonJointPosition pos = tracker.skeletonCap
				.getSkeletonJointPosition(user, joint);
		if (pos.getPosition().getZ() != 0) {
			tracker.getJoints()
					.get(user)
					.put(joint,
							new SkeletonJointPosition(tracker.depthGen
									.convertRealWorldToProjective(pos
											.getPosition()), pos
									.getConfidence()));
		} else {
			tracker.getJoints().get(user)
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

		g.fillOval((int) pos1.getX() - 4, (int) pos1.getY() - 4, 8, 8);
		g.fillOval((int) pos2.getX() - 4, (int) pos2.getY() - 4, 8, 8);
		g.drawLine((int) pos1.getX(), (int) pos1.getY(), (int) pos2.getX(),
				(int) pos2.getY());

	}

	public void drawSkeleton(Graphics g, int user) throws StatusException {
		getJoints(user);
		HashMap<SkeletonJoint, SkeletonJointPosition> dict = tracker
				.getJoints().get(new Integer(user));
		drawLine(g, dict, SkeletonJoint.TORSO, SkeletonJoint.NECK);
		drawLine(g, dict, SkeletonJoint.TORSO, SkeletonJoint.HEAD);
		drawLine(g, dict, SkeletonJoint.TORSO, SkeletonJoint.LEFT_SHOULDER);
		drawLine(g, dict, SkeletonJoint.TORSO, SkeletonJoint.RIGHT_SHOULDER);

		// drawLine(g, dict, SkeletonJoint.HEAD, SkeletonJoint.NECK);
		//
		// drawLine(g, dict, SkeletonJoint.LEFT_SHOULDER, SkeletonJoint.TORSO);
		// drawLine(g, dict, SkeletonJoint.RIGHT_SHOULDER, SkeletonJoint.TORSO);
		//
		// drawLine(g, dict, SkeletonJoint.NECK, SkeletonJoint.LEFT_SHOULDER);
		// drawLine(g, dict, SkeletonJoint.LEFT_SHOULDER,
		// SkeletonJoint.LEFT_ELBOW);
		// drawLine(g, dict, SkeletonJoint.LEFT_ELBOW, SkeletonJoint.LEFT_HAND);
		//
		// drawLine(g, dict, SkeletonJoint.NECK, SkeletonJoint.RIGHT_SHOULDER);
		// drawLine(g, dict, SkeletonJoint.RIGHT_SHOULDER,
		// SkeletonJoint.RIGHT_ELBOW);
		// drawLine(g, dict, SkeletonJoint.RIGHT_ELBOW,
		// SkeletonJoint.RIGHT_HAND);
		//
		// drawLine(g, dict, SkeletonJoint.LEFT_HIP, SkeletonJoint.TORSO);
		// drawLine(g, dict, SkeletonJoint.RIGHT_HIP, SkeletonJoint.TORSO);
		// drawLine(g, dict, SkeletonJoint.LEFT_HIP, SkeletonJoint.RIGHT_HIP);
		//
		// drawLine(g, dict, SkeletonJoint.LEFT_HIP, SkeletonJoint.LEFT_KNEE);
		// drawLine(g, dict, SkeletonJoint.LEFT_KNEE, SkeletonJoint.LEFT_FOOT);
		//
		// drawLine(g, dict, SkeletonJoint.RIGHT_HIP, SkeletonJoint.RIGHT_KNEE);
		// drawLine(g, dict, SkeletonJoint.RIGHT_KNEE,
		// SkeletonJoint.RIGHT_FOOT);

	}

}
