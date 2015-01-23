package usertracking.prototype.gui.utils;

import java.awt.Graphics;
import java.util.HashMap;

import org.OpenNI.Point3D;
import org.OpenNI.SkeletonJoint;
import org.OpenNI.SkeletonJointPosition;

import usertracking.prototype.classes.SimpleTracker;

public class SkeletonFrontDrawer extends AbstractSkeletonDrawer {

	public SkeletonFrontDrawer(SimpleTracker _tracker) {
		super(_tracker);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void drawLine(Graphics g,
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
}
