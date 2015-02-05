package usertracking.prototype.profile;

import java.io.File;
import java.util.List;

public class ProfileDataFromFiles extends ProfileCache {

	private static final long serialVersionUID = -4318685451015038353L;

	@Override
	public ProfileDataFromFiles loadProfiles() {
		String profileFolder = "profiles";
		
		File folder = new File(profileFolder);
		File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		        System.out.println("File " + listOfFiles[i].getName());
		        
		        String profilePath = profileFolder + "/" + listOfFiles[i].getName();
		        ProfileKMeans kMeans = new ProfileKMeans(profilePath, 2);
		        
		        List<JointCluster> user2Joints = kMeans.getJointsClusters();
				for (int j = 0; j < kMeans.k; j++) {
					System.out.println("Cluster " + j + ": "
							+ user2Joints.get(j).getCentroid());
					//System.out.println("Cluster " + i + ": " + pointsClusters.get(i));
					UserProfileByJointClusters oneProfile = new UserProfileByJointClusters();
					oneProfile.setProfileName(listOfFiles[i].getName());
					oneProfile.setProfileJointVectors(kMeans.getJointsClusters());
					
					this.add(oneProfile);
				}
		      }
		    }
			return this;
	}
}
