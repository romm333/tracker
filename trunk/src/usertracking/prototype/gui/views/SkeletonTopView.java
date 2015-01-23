package usertracking.prototype.gui.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import org.OpenNI.Point3D;
import org.OpenNI.SkeletonJoint;
import org.OpenNI.SkeletonJointPosition;
import org.OpenNI.SkeletonProfile;
import org.OpenNI.StatusException;

import usertracking.prototype.classes.SimpleTracker;
import usertracking.prototype.gui.utils.SkeletonTopDrawer;

public class SkeletonTopView extends Component implements Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SimpleTracker tracker;
	private SkeletonTopDrawer skeletonDrawer;

	private int width;
	private int height;

	private boolean printID = true;
	private boolean printState = true;

	public SkeletonTopView(SimpleTracker _traker) {
		this.tracker = _traker;
		skeletonDrawer = new SkeletonTopDrawer(_traker);

		width = this.tracker.width;
		height = this.tracker.height;
	}

	public SkeletonTopView(SimpleTracker _traker, int _height, int _widht) {
		this.tracker = _traker;
		skeletonDrawer = new SkeletonTopDrawer(_traker);

		width = _height;
		height = _widht;
	}

	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

	@Override
	public void paint(Graphics g) {
		int x1 = this.getWidth() / 2 - 35;
		int y1 = this.getHeight() / 35;
		g.setColor(Color.BLACK);
		g.fillOval(x1, y1, 70, 8);

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
					// drawSkeleton(g, users[i]);
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
						double Zcoord = com.getZ() / 2 - this.getHeight();

						if (Zcoord < this.getHeight() / 35 + 8)
							Zcoord = this.getHeight() / 35 + 8;

						DecimalFormat df = new DecimalFormat("####0.00");
						label = new String("User " + users[i] + " - Distance: "
								+ df.format(com.getZ() / 1000));
						g.drawString(label, (int) com.getX(), (int) Zcoord - 2);

						skeletonDrawer.drawSkeleton(g, users[i]);

					} else if (tracker.skeletonCap
							.isSkeletonCalibrating(users[i])) {
						// Calibrating
						label = new String(users[i] + " - Calibrating");
					} else {
						// Nothing
						label = new String(users[i] + " - Looking for pose ("
								+ tracker.calibPose + ")");
					}

				}
			}
		} catch (StatusException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		this.repaint();
	}

}
