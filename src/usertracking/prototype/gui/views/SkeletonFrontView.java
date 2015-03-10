package usertracking.prototype.gui.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import org.OpenNI.Point3D;
import org.OpenNI.StatusException;

import usertracking.prototype.classes.SimpleTracker;
import usertracking.prototype.gui.utils.SkeletonFrontDrawer;

public class SkeletonFrontView extends Component implements Observer{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SimpleTracker tracker;
	private SkeletonFrontDrawer skeletonDrawer;
	
	private int width;
	private int height;

	private boolean printID = true;
	private boolean printState = true;
	
	public SkeletonFrontView(SimpleTracker _traker) {
		this.tracker = _traker;
		width = this.tracker.width;
		height = this.tracker.height;
		
		this.setSize(270, 460);
		skeletonDrawer = new SkeletonFrontDrawer(_traker);
	}

	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

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
				
				Color c = Color.white;
				
				int recognizedProfileID = -1;
				if (tracker.getRecognizedProfiles().size() > 0) {
					if (tracker.getRecognizedProfiles().containsKey(users[i])) {
						recognizedProfileID = tracker.getRecognizedProfiles()
								.get(users[i]).ID;
						c = colors[recognizedProfileID % colors.length];
						c = new Color(255 - c.getRed(), 255 - c.getGreen(),
								255 - c.getBlue());
					}
				}
				
//				c = colors[users[i] % colors.length];
//				c = new Color(255 - c.getRed(), 255 - c.getGreen(),
//						255 - c.getBlue());

				g.setColor(c);
				//g.setColor(Color.WHITE);
				
				String label = null;
				
				if (tracker.skeletonCap.isSkeletonTracking(users[i])) {
					skeletonDrawer.drawSkeleton(g, users[i]);
				}

				if (printID) {
					Point3D com = tracker.depthGen
							.convertRealWorldToProjective(tracker.userGen
									.getUserCoM(users[i]));
					
					if (!printState) {
						label = new String("" + users[i]);
					} else if (tracker.skeletonCap.isSkeletonTracking(users[i])) {
						// Tracking
						label = new String(users[i] + " - Tracking");
				
					} else if (tracker.skeletonCap
							.isSkeletonCalibrating(users[i])) {
						// Calibrating
						label = new String(users[i] + " - Calibrating");
					} else {
						// Nothing
						label = new String(users[i] + " - Looking for pose ("
								+ tracker.calibPose + ")");
					}

					g.drawString(label, (int) com.getX(), (int) com.getY());
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
