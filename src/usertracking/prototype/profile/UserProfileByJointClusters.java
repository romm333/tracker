package usertracking.prototype.profile;

import java.util.ArrayList;
import java.util.List;

import org.OpenNI.DepthGenerator;
import org.OpenNI.UserGenerator;

public class UserProfileByJointClusters extends UserProfileBase implements
		IUserProfile {
	
	private List<JointCluster> profileJointClusters = new ArrayList<JointCluster>();
	private String _profileName;
	
	public UserProfileByJointClusters(){
		
	}
	
	public UserProfileByJointClusters(int uid, UserGenerator userGen,
			DepthGenerator depthGen) {
		super(uid, userGen, depthGen);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double getProfileFignature() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setProfileName(String string) {
		_profileName = string;

	}

	@Override
	public void calculateProfileSignature() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getProfileName() {
		return _profileName;
	}

	public List<JointCluster> getProfileJointClusters() {
		return profileJointClusters;
	}

	public void setProfileJointVectors(List<JointCluster> profileJointClusters) {
		this.profileJointClusters = profileJointClusters;
	}

}
