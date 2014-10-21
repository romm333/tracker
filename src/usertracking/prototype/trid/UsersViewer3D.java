package usertracking.prototype.trid;

// UsersViewer3D.java
// Andrew Davison, October 2011, ad@fivedots.psu.ac.th

/* Track Kinect users by displaying their bodies as
 3D skeletons made of limbs and joints in a simple Java 3D scene.

 Based on the 2D UserTrackerApplication.java
 from the Java OpenNI UserTracker sample, and my
 ArniesTracker example.

 Usage:
 > java UsersViewer3D
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class UsersViewer3D extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TrackerPanel3D tp3D;

	public UsersViewer3D() {
		super("Users Viewer 3D");

		Container c = getContentPane();
		c.setLayout(new BorderLayout());

		tp3D = new TrackerPanel3D();
		c.add(tp3D, BorderLayout.SOUTH);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				tp3D.closeDown(); // stop showing images

			}
		});

		pack();
		// setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	} // end of UsersViewer3D()

	// -------------------------------------------------------

	public static void main(String args[]) {
		new UsersViewer3D();
	}

} // end of UsersViewer3D class
