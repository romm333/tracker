package usertracking.prototype.gui.utils;

import java.awt.Graphics;
import java.util.HashMap;

import org.OpenNI.Point3D;
import org.OpenNI.SkeletonJoint;
import org.OpenNI.SkeletonJointPosition;

import usertracking.prototype.classes.SimpleTracker;

public class SkeletonTopDrawer extends AbstractSkeletonDrawer {

	public SkeletonTopDrawer(SimpleTracker _tracker) {
		super(_tracker);
	}

	@Override
	public void drawLimb(Graphics g,
			HashMap<SkeletonJoint, SkeletonJointPosition> jointHash,
			SkeletonJoint joint1, SkeletonJoint joint2) {
		Point3D pos1 = jointHash.get(joint1).getPosition();
		Point3D pos2 = jointHash.get(joint2).getPosition();

		if (jointHash.get(joint1).getConfidence() == 0
				|| jointHash.get(joint2).getConfidence() == 0)
			return;

		double Zcoord1 = pos1.getZ() / 3 - getTracker().height;

		if (Zcoord1 < getTracker().height / 40 + 8)
			Zcoord1 = getTracker().height / 40 + 8;

		double Zcoord2 = pos2.getZ() / 3 - getTracker().height;

		if (Zcoord2 < getTracker().height / 40 + 8)
			Zcoord2 = getTracker().height / 40 + 8;

		g.fillOval((int) pos1.getX() - 4, (int) Zcoord1 - 4, 8, 8);
		g.fillOval((int) pos2.getX() - 4, (int) Zcoord2 - 4, 8, 8);
		g.drawLine((int) pos1.getX(), (int) Zcoord1, (int) pos2.getX(),
				(int) Zcoord2);
	}
}
