package usertracking.prototype.profile;

import org.OpenNI.DepthGenerator;
import org.OpenNI.SkeletonCapability;
import org.OpenNI.SkeletonJoint;
import org.OpenNI.SkeletonJointPosition;
import org.OpenNI.StatusException;

public class UserProfileByJoints extends UserProfileBase implements IUserProfile{
	public UserProfileByJoints(int uid, SkeletonCapability skeletonCap,
			DepthGenerator depthGen) {
		super(uid, skeletonCap, depthGen);
	}

	private double _profileSignature;
	private String _profileName;
	
	public String getProfileName() {
		return _profileName;
	}

	public void setProfileName(String profileName) {
		this._profileName = profileName;
	}

	@Override
	public double getProfileFignature() {
		return _profileSignature;
	}

	
	@Override
	public void calculateProfileSignature() {
		try {
			//upper body: left shoulder - right shoulder
			SkeletonJointPosition leftShoulder = getJointPosition(get_uid(), SkeletonJoint.LEFT_SHOULDER);
			SkeletonJointPosition rightShoulder = getJointPosition(get_uid(), SkeletonJoint.RIGHT_SHOULDER);
			double shoulders = getLenght(leftShoulder.getPosition(), rightShoulder.getPosition());
			
			//lower body: left hip - right hip
			SkeletonJointPosition leftHip = getJointPosition(get_uid(), SkeletonJoint.LEFT_HIP);
			SkeletonJointPosition rightHip = getJointPosition(get_uid(), SkeletonJoint.RIGHT_HIP);
			double hips = getLenght(leftHip.getPosition(), rightHip.getPosition());
			
			_profileSignature = shoulders + hips;
			
		} catch (StatusException e) {
			e.printStackTrace();
		}
		
	}
}
