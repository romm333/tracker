package usertracking.prototype.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import usertracking.prototype.kmeans.JointCluster;
import usertracking.prototype.kmeans.ProfileKMeans;
import usertracking.prototype.profile.DummyProfile;

public class ProfileKMeansTest {

	@Test
	public void loadProfileDataTest(){
		ProfileKMeans profileData;
		List<JointCluster> jointClusters;
		List<DummyProfile> profileMeans;
		
		profileData = new ProfileKMeans("profiles/1.csv", 3);
		jointClusters = profileData.getJointsClusters();
		
		profileMeans = new ArrayList<DummyProfile>();
		
		for (int i = 0; i < profileData.k; i++) {
			System.out.println("Cluster " + i + ": "
					+ jointClusters.get(i).getCentroid() + " " + jointClusters.get(i).getJointVectors().size());
			DummyProfile oneProfile = new DummyProfile();
			oneProfile.ID = i;
			oneProfile.Name = "Profile" + String.valueOf(i);
			oneProfile.profileMean = jointClusters.get(i).getCentroid();
			profileMeans.add(oneProfile);
		}
	}

}
