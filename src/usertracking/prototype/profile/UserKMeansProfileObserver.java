package usertracking.prototype.profile;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.FileHandler;

import org.OpenNI.StatusException;

import usertracking.prototype.classes.SimpleTracker;
import usertracking.prototype.gui.utils.DataLogger;
import usertracking.prototype.gui.utils.DataUtils;

public class UserKMeansProfileObserver implements Observer {
	private SimpleTracker tracker;
	private Dataset data;
	private float[][] existingProfileData;
	private DWT dwt1;
	String pointsFilePath = "profiles/1.csv";
	ProfileKMeans profileKmeans = new ProfileKMeans(pointsFilePath,2);

	public List<JointVector> centroids;
	public UserKMeansProfileObserver(SimpleTracker _traker) {
		this.tracker = _traker;

		List<JointCluster> jointClusters = profileKmeans.getJointsClusters();
		centroids = new LinkedList<JointVector>(); 
		
		for (int i = 0; i < profileKmeans.k; i++) {
					JointVector centroid = jointClusters.get(i).getCentroid();
					centroids.add(centroid);
		}
		
		
		existingProfileData = new float[centroids.size()][];

		for (int i = 0; i < centroids.size(); i++) {
			float[] tempValues = new float[4];
			
			tempValues[0] = (float)centroids.get(i).a;
			tempValues[1] = (float)centroids.get(i).b;
			tempValues[2] = (float)centroids.get(i).c;
			tempValues[3] = (float)centroids.get(i).d;
			
			existingProfileData[i] = tempValues;
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		getUserProfile();
	}

	void getUserProfile() {
		int[] users;
		try {
			users = tracker.userGen.getUsers();

			for (int i = 0; i < users.length; ++i) {
				if (tracker.skeletonCap.isSkeletonTracking(users[i])) {
					UserProfileByJoints profile = new UserProfileByJoints(users[i],
							tracker.userGen, tracker.depthGen);
					String[] jointDataStrings = DataLogger.getCoordsAsCSV(
							profile, ProfileJointGroup.FULL).split(",");
					
					float[] jointData = new float[4];
					
					jointData[0] = Float.parseFloat(jointDataStrings[2]);
					jointData[1] = Float.parseFloat(jointDataStrings[4]);
					jointData[2] = Float.parseFloat(jointDataStrings[6]);
					jointData[3] = Float.parseFloat(jointDataStrings[12]);

					for (int k = 0; k < existingProfileData.length; k++) {
						dwt1 = new DWT(existingProfileData[1], jointData);
						
						if(dwt1.getWrapingDistance() <= 10 ){
						
							System.out.println("*********USER " + users[i] + " RECOGNIZED*********");
							System.out.println(dwt1.getWrapingDistance());
							
						}
						
						System.out.println(centroids.get(k).toString());
					}
					
				}
			}
		} catch (StatusException e) {

			e.printStackTrace();
		}
	}

}
