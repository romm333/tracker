package usertracking.prototype.classes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.OpenNI.Point3D;

public class UserProfiler {

	private List<UserProfile> profileList = new ArrayList<UserProfile>();

	public void insertProfile(UserProfile profile) {
		profileList.add(profile);
	}

	public void removeProfile(UserProfile profile) {
		profileList.remove(profile);
	}

	public void removeProfile(int profileIndex) {
		profileList.remove(profileIndex);
	}

	public UserProfile getSimilarProfile(UserProfile profile) {

		for (UserProfile oneProfile : profileList) {
			// double HeadToRightShoulder = oneProfile.HtRS / profile.HtRS;
			// double RightShoulderToTorso = oneProfile.RStT / profile.RStT;
			// double TorsoToLeftShoulder = oneProfile.TtLS / profile.TtLS;
			// double LeftShoulderToHead = oneProfile.LStH / profile.LStH;
			//
			// System.out.println("HeadToRightShoulder: " +
			// HeadToRightShoulder);
			// System.out.println("RightShoulderToTorso: " +
			// RightShoulderToTorso);
			// System.out.println("TorsoToLeftShoulder: " +
			// TorsoToLeftShoulder);
			// System.out.println("LeftShoulderToHead: " + LeftShoulderToHead);
			// System.out.println(oneProfile.getProfileName());
			// System.out.println("VectorLenght " + oneProfile.finalVector);

			double vectorsAtt = oneProfile.finalVector / profile.finalVector;

			// if(RightShoulderToTorso == TorsoToLeftShoulder &&
			// TorsoToLeftShoulder == LeftShoulderToHead)
			if (1.05 > vectorsAtt && vectorsAtt > 0.95) {
				// System.out.println("HeadToRightShoulder: " +
				// HeadToRightShoulder);
				// System.out.println("RightShoulderToTorso: " +
				// RightShoulderToTorso);
				// System.out.println("TorsoToLeftShoulder: " +
				// TorsoToLeftShoulder);
				// System.out.println("LeftShoulderToHead: " +
				// LeftShoulderToHead);
				// System.out.println(oneProfile.getProfileName());
				// System.out.println("VectorLenght " + oneProfile.finalVector);

				System.out.println("Matching profile found"
						+ oneProfile.getProfileName());

				String name = oneProfile.getProfileName();
				if (!name.contains("_recognized"))
					name += "_recognized";

				oneProfile.setProfileName(name);
				return oneProfile;
			}

		}
		return null;
	}

	public double getVectorLength(List<Point3D> triangleTops) {

		double finalVector = 0;
		// left to right
		float hX = triangleTops.get(0).getX();
		float hY = triangleTops.get(0).getY();
		float hZ = triangleTops.get(0).getZ();

		float rsX = triangleTops.get(1).getX();
		float rsY = triangleTops.get(1).getY();
		float rsZ = triangleTops.get(1).getZ();

		float tX = triangleTops.get(2).getX();
		float tY = triangleTops.get(2).getY();
		float tZ = triangleTops.get(2).getZ();

		float lsX = triangleTops.get(3).getX();
		float lsY = triangleTops.get(3).getY();
		float lsZ = triangleTops.get(3).getZ();

		double HtRS = Math.sqrt(Math.pow((hX - rsX), 2)
				+ Math.pow((hY - rsY), 2) + Math.pow((hZ - rsZ), 2));
		double RStT = Math.sqrt(Math.pow((rsX - tX), 2)
				+ Math.pow((rsY - tY), 2) + Math.pow((rsZ - tZ), 2));
		double TtLS = Math.sqrt(Math.pow((tX - lsX), 2)
				+ Math.pow((tY - lsY), 2) + Math.pow((tZ - lsZ), 2));
		double LStH = Math.sqrt(Math.pow((lsX - tX), 2)
				+ Math.pow((lsY - tY), 2) + Math.pow((lsZ - tZ), 2));

		finalVector = RStT + TtLS + LStH;
		return finalVector;
	}
}
