package usertracking.prototype.test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Line2D;
import java.util.HashMap;

import javax.swing.JFrame;

import org.OpenNI.Point3D;
import org.OpenNI.SkeletonJoint;
import org.OpenNI.SkeletonJointPosition;
import org.OpenNI.StatusException;

import usertracking.prototype.classes.SimpleTracker;

public class testRunner extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	SimpleTracker viewer;

	private boolean shouldRun = true;
	// private JFrame frame;
	private testComponent testC;

	public testRunner() {
		this.setSize(500, 500);
		this.setVisible(true);
	}

	public static void main(String s[]) {
		testRunner tt = new testRunner();
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		g.drawLine(200, 200, 200, 300);
		
		int x2  = 200 * (int)Math.cos( Math.toRadians( 180) ) - 300 * (int)Math.sin( Math.toRadians( 180 ) );
		int y2  = 200 * (int)Math.sin( Math.toRadians( 180) ) + 300 * (int)Math.cos( Math.toRadians( 180 ) );
		
		g.drawLine(200, 200, x2, y2);
	}

	void run() {
		while (shouldRun) {
			viewer.updateDepth();
			testC.repaint();
		}

	}
	
	
//	vec2 rotate(vec2 point, float angle){
//        vec2 rotated_point;
//        rotated_point.x = point.x * cos(angle) - point.y * sin(angle);
//        rotated_point.y = point.x * sin(angle) + point.y * cos(angle);
//        return rotated_point;
}
	// void drawSkelet(Graphics g) {
	//
	// int[] users;
	// try {
	// users = viewer.userGen.getUsers();
	//
	// for (int i = 0; i < users.length; ++i) {
	// Color c = Color.black;
	// g.setColor(c);
	// if (viewer.skeletonCap.isSkeletonTracking(users[i])) {
	// drawSkeleton(g, users[i]);
	// System.out.println("drawing");
	// }
	// }
	// } catch (StatusException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	// void run() {
	//
	// while (true) {
	// try {
	// Thread.sleep(100);
	// } catch (InterruptedException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// viewer.updateDepth();
	// this.revalidate();
	// }// app.run
	// }

	// public void getJoint(int user, SkeletonJoint joint) throws
	// StatusException {
	// SkeletonJointPosition pos = viewer.skeletonCap
	// .getSkeletonJointPosition(user, joint);
	// if (pos.getPosition().getZ() != 0) {
	// viewer.getJoints()
	// .get(user)
	// .put(joint,
	// new SkeletonJointPosition(viewer.depthGen
	// .convertRealWorldToProjective(pos
	// .getPosition()), pos
	// .getConfidence()));
	// } else {
	// viewer.getJoints().get(user)
	// .put(joint, new SkeletonJointPosition(new Point3D(), 0));
	// }
	// }
	//
	// public void getJoints(int user) throws StatusException {
	// getJoint(user, SkeletonJoint.HEAD);
	// getJoint(user, SkeletonJoint.NECK);
	//
	// getJoint(user, SkeletonJoint.LEFT_SHOULDER);
	// getJoint(user, SkeletonJoint.LEFT_ELBOW);
	// getJoint(user, SkeletonJoint.LEFT_HAND);
	//
	// getJoint(user, SkeletonJoint.RIGHT_SHOULDER);
	// getJoint(user, SkeletonJoint.RIGHT_ELBOW);
	// getJoint(user, SkeletonJoint.RIGHT_HAND);
	//
	// getJoint(user, SkeletonJoint.TORSO);
	//
	// getJoint(user, SkeletonJoint.LEFT_HIP);
	// getJoint(user, SkeletonJoint.LEFT_KNEE);
	// getJoint(user, SkeletonJoint.LEFT_FOOT);
	//
	// getJoint(user, SkeletonJoint.RIGHT_HIP);
	// getJoint(user, SkeletonJoint.RIGHT_KNEE);
	// getJoint(user, SkeletonJoint.RIGHT_FOOT);
	//
	// }
	//
	// void drawLine(Graphics g,
	// HashMap<SkeletonJoint, SkeletonJointPosition> jointHash,
	// SkeletonJoint joint1, SkeletonJoint joint2) {
	// Point3D pos1 = jointHash.get(joint1).getPosition();
	// Point3D pos2 = jointHash.get(joint2).getPosition();
	//
	// if (jointHash.get(joint1).getConfidence() == 0
	// || jointHash.get(joint2).getConfidence() == 0)
	// return;
	//
	// g.drawLine((int) pos1.getX(), (int) pos1.getY(), (int) pos2.getX(),
	// (int) pos2.getY());
	// }
	//
	// public void drawSkeleton(Graphics g, int user) throws StatusException {
	// getJoints(user);
	// HashMap<SkeletonJoint, SkeletonJointPosition> dict = viewer.getJoints()
	// .get(new Integer(user));
	//
	// drawLine(g, dict, SkeletonJoint.HEAD, SkeletonJoint.NECK);
	//
	// drawLine(g, dict, SkeletonJoint.LEFT_SHOULDER, SkeletonJoint.TORSO);
	// drawLine(g, dict, SkeletonJoint.RIGHT_SHOULDER, SkeletonJoint.TORSO);
	//
	// drawLine(g, dict, SkeletonJoint.NECK, SkeletonJoint.LEFT_SHOULDER);
	// drawLine(g, dict, SkeletonJoint.LEFT_SHOULDER, SkeletonJoint.LEFT_ELBOW);
	// drawLine(g, dict, SkeletonJoint.LEFT_ELBOW, SkeletonJoint.LEFT_HAND);
	//
	// drawLine(g, dict, SkeletonJoint.NECK, SkeletonJoint.RIGHT_SHOULDER);
	// drawLine(g, dict, SkeletonJoint.RIGHT_SHOULDER,
	// SkeletonJoint.RIGHT_ELBOW);
	// drawLine(g, dict, SkeletonJoint.RIGHT_ELBOW, SkeletonJoint.RIGHT_HAND);
	//
	// drawLine(g, dict, SkeletonJoint.LEFT_HIP, SkeletonJoint.TORSO);
	// drawLine(g, dict, SkeletonJoint.RIGHT_HIP, SkeletonJoint.TORSO);
	// drawLine(g, dict, SkeletonJoint.LEFT_HIP, SkeletonJoint.RIGHT_HIP);
	//
	// drawLine(g, dict, SkeletonJoint.LEFT_HIP, SkeletonJoint.LEFT_KNEE);
	// drawLine(g, dict, SkeletonJoint.LEFT_KNEE, SkeletonJoint.LEFT_FOOT);
	//
	// drawLine(g, dict, SkeletonJoint.RIGHT_HIP, SkeletonJoint.RIGHT_KNEE);
	// drawLine(g, dict, SkeletonJoint.RIGHT_KNEE, SkeletonJoint.RIGHT_FOOT);
	//
	// }


