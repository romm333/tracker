package usertracking.prototype.profile;

import org.OpenNI.DepthGenerator;
import org.OpenNI.Point3D;
import org.OpenNI.SkeletonCapability;
import org.OpenNI.SkeletonJoint;
import org.OpenNI.SkeletonJointPosition;
import org.OpenNI.StatusException;

public class UserProfileByCentroids extends UserProfileBase implements IUserProfile{

	private double _profileSignature;
	private String _profileName;
	
	public UserProfileByCentroids(int uid, SkeletonCapability skeletonCap,
			DepthGenerator depthGen) {
		super(uid, skeletonCap, depthGen);
	}

	@Override
	public double getProfileFignature() {
		return _profileSignature;
	}

	@Override
	public void setProfileName(String string) {
		_profileName = string;
	}
	
	@Override
	public void calculateProfileSignature() {
		try {
			//upper body: left shoulder - right shoulder - torso
			SkeletonJointPosition leftShoulder = getJointPosition(get_uid(), SkeletonJoint.LEFT_SHOULDER);
			SkeletonJointPosition rightShoulder = getJointPosition(get_uid(), SkeletonJoint.RIGHT_SHOULDER);
			SkeletonJointPosition torso = getJointPosition(get_uid(), SkeletonJoint.TORSO);
		
			float upperBodyCentroidX = (leftShoulder.getPosition().getX() + rightShoulder.getPosition().getX() + torso.getPosition().getX())/3;  
			float upperBodyCentroidY = (leftShoulder.getPosition().getY() + rightShoulder.getPosition().getY() + torso.getPosition().getY())/3;
			float upperBodyCentroidZ = (leftShoulder.getPosition().getZ() + rightShoulder.getPosition().getZ() + torso.getPosition().getZ())/3;
			
			Point3D upperBodyCentroid = new Point3D(upperBodyCentroidX, upperBodyCentroidY, upperBodyCentroidZ);
			
			//lower body: left shoulder - right shoulder - torso
			SkeletonJointPosition leftHip = getJointPosition(get_uid(), SkeletonJoint.LEFT_HIP);
			SkeletonJointPosition rightHip = getJointPosition(get_uid(), SkeletonJoint.RIGHT_HIP);
		
			float lowerBodyCentroidX = (torso.getPosition().getX() + leftHip.getPosition().getX() + rightHip.getPosition().getX())/3;  
			float lowerBodyCentroidY = (torso.getPosition().getY() + leftHip.getPosition().getY() + rightHip.getPosition().getY())/3;
			float lowerBodyCentroidZ = (torso.getPosition().getZ() + leftHip.getPosition().getZ() + rightHip.getPosition().getZ())/3;
		
			Point3D lowerBodyCentroid = new Point3D(lowerBodyCentroidX, lowerBodyCentroidY, lowerBodyCentroidZ);
			
			_profileSignature = getLenght(upperBodyCentroid, lowerBodyCentroid);
			
		} catch (StatusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public String getProfileName() {
		return _profileName;
	}

}
