package usertracking.prototype.profile;

import org.OpenNI.DepthGenerator;
import org.OpenNI.Point3D;
import org.OpenNI.SkeletonJoint;
import org.OpenNI.SkeletonJointPosition;
import org.OpenNI.StatusException;
import org.OpenNI.UserGenerator;

public abstract class UserProfileBase {
	private UserGenerator _userGen;
	private DepthGenerator _depthGen;
	private int _uid;

	public UserProfileBase(int uid, UserGenerator userGen,
			DepthGenerator depthGen) {
		_uid = uid;
		_userGen = userGen;
		_depthGen = depthGen;
	}

	public UserProfileBase() {
		//default constructor
	}

	public SkeletonJointPosition getJointPosition(int user, SkeletonJoint joint)
			throws StatusException {
		SkeletonJointPosition pos = _userGen.getSkeletonCapability()
				.getSkeletonJointPosition(user, joint);
		if (pos.getPosition().getZ() != 0) {
			return new SkeletonJointPosition(
					_depthGen.convertRealWorldToProjective(pos.getPosition()),
					pos.getConfidence());
		}

		return pos;
	}

	public SkeletonJointPosition getRealJointPosition(int user, SkeletonJoint joint)
			throws StatusException {
		SkeletonJointPosition pos = _userGen.getSkeletonCapability()
				.getSkeletonJointPosition(user, joint);
		return pos;
	}

	public Point3D getUserCom() {
		try {
			return _userGen.getUserCoM(_uid);
		} catch (StatusException e) {
			e.printStackTrace();
		}
		return null;
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
