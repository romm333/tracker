package usertracking.prototype.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import org.OpenNI.StatusException;

import usertracking.prototype.classes.SimpleTracker;

public class SkeletonTextInfoFacade extends Component {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SimpleTracker tracker;

	public SkeletonTextInfoFacade(SimpleTracker _traker) {
		this.tracker = _traker;
	}

	@Override
	public void paint(Graphics g) {
		drawTextInfo(g);
	}

	Color colors[] = { Color.RED, Color.BLUE, Color.CYAN, Color.GREEN,
			Color.MAGENTA, Color.PINK, Color.YELLOW, Color.WHITE };

	void drawTextInfo(Graphics g) {
		int[] users;
		try {
			users = tracker.userGen.getUsers();

			for (int i = 0; i < users.length; ++i) {
				Color c = colors[users[i] % colors.length];
				c = new Color(255 - c.getRed(), 255 - c.getGreen(),
						255 - c.getBlue());

				g.setColor(c);

				g.drawString(SimpleTracker.calibrationState, 5, 15);
				g.drawString(SimpleTracker.userState, 5, 30);
				g.drawString(SimpleTracker.trakingState, 5, 45);

			}
		} catch (StatusException e) {

			e.printStackTrace();
		}
	}
}
