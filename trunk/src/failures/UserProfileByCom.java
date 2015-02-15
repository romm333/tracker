package failures;

import org.OpenNI.DepthGenerator;
import org.OpenNI.Point3D;
import org.OpenNI.UserGenerator;

import usertracking.prototype.profile.IUserProfile;

public class UserProfileByCom extends UserProfileBase implements IUserProfile {

	
	public UserProfileByCom(int uid, UserGenerator userGen,
			DepthGenerator depthGen) {
		super(uid, userGen, depthGen);
		
		
	}

	private double _profileSignature;
	private String _profileName;

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
		Point3D com = getUserCom();
		_profileSignature = com.getX() + com.getY() + com.getZ();
	}

	@Override
	public String getProfileName() {
		return _profileName;
	}

}
