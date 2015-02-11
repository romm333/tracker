package usertracking.prototype.profile;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.FileHandler;

import org.OpenNI.StatusException;

import usertracking.prototype.classes.SimpleTracker;
import usertracking.prototype.gui.utils.DataLogger;
import usertracking.prototype.gui.utils.DataUtils;

public class UserProfileObserver implements Observer {
	private SimpleTracker tracker;
	private Dataset data;
	private float[][] existingProfileData;
	private DWT dwt1;

	KMeansT profileKmeans = new KMeansT();

	public UserProfileObserver(SimpleTracker _traker) {
		this.tracker = _traker;

		try {
			data = FileHandler.loadDataset(new File("profiles/1.csv"), 4, ",");
			KMeansT km = new KMeansT();
			Dataset[] clusters = km.cluster(data);
			Instance[] centroids = km.getCentroids();
			
			
			existingProfileData = new float[centroids.length][];

			for (int i = 0; i < centroids.length; i++) {
				float[] tempValues = new float[centroids[i].size()];

				for (int k = 0; k < tempValues.length; k++) {
					double buff = centroids[i].get(k);
					tempValues[i] = (float) buff;
				}
				existingProfileData[i] = tempValues;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
					float[] jointData = new float[jointDataStrings.length];

					for (int a = 0; a < jointDataStrings.length; a++) {
						jointData[a] = Float.parseFloat(jointDataStrings[a]);
					}

					for (int k = 0; k < existingProfileData.length; k++) {
						dwt1 = new DWT(existingProfileData[k], jointData);
						System.out.println(dwt1.toString());
					}
					
//					System.out.println(DataLogger.getCoordsAsCSV(
//							profile, ProfileJointGroup.FULL));
				}
			}
		} catch (StatusException e) {

			e.printStackTrace();
		}
	}

}
