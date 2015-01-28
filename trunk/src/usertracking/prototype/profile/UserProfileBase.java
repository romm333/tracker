package usertracking.prototype.profile;

import org.OpenNI.DepthGenerator;
import org.OpenNI.Point3D;
import org.OpenNI.SkeletonCapability;
import org.OpenNI.SkeletonJoint;
import org.OpenNI.SkeletonJointPosition;
import org.OpenNI.StatusException;

public abstract class UserProfileBase {
	private SkeletonCapability _skeletonCap;
	private DepthGenerator _depthGen;
	private int _uid;
	
	public UserProfileBase(int uid, SkeletonCapability skeletonCap, DepthGenerator depthGen) {
		_uid = uid;
		_skeletonCap = skeletonCap;
		_depthGen = depthGen;
	}
	
	public SkeletonJointPosition getJointPosition(int user, SkeletonJoint joint)
			throws StatusException {
		SkeletonJointPosition pos = _skeletonCap.getSkeletonJointPosition(user,
				joint);
		if (pos.getPosition().getZ() != 0) {
			return new SkeletonJointPosition(
					_depthGen.convertRealWorldToProjective(pos.getPosition()),
					pos.getConfidence());
		}
		
		return pos;
	}
	
	public double getLenght(Point3D joint1, Point3D joint2) {
		return Math.sqrt(Math.pow(joint1.getX() - joint2.getX(), 2)
				+ Math.pow(joint1.getY() - joint2.getY(), 2)
				+ Math.pow(joint1.getZ() - joint2.getZ(), 2));
	}

	public int get_uid() {
		return _uid;
	}
}
