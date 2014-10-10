package usertracking.prototype.classes;

import java.util.LinkedList;
import java.util.List;

import org.OpenNI.Point3D;

public class UserProfiler {

	static List<UserProfile> profileList = new LinkedList<UserProfile>();

	public static void insertProfile(UserProfile profile) {
		profileList.add(profile);
	}

	public static void removeProfile(UserProfile profile) {
		profileList.remove(profile);
	}

	public static void removeProfile(int profileIndex) {
		profileList.remove(profileIndex);
	}

	public static UserProfile getSimilarProfile(UserProfile profile) {
		profile.calculateProfile();
		for (UserProfile oneProfile : profileList) {
			oneProfile.calculateProfile();
			if ((oneProfile.AB / profile.AB) == (oneProfile.BC / profile.BC))
				return oneProfile;
		}
		return null;
	}

	public static double getVectorLength(List<Point3D> triangleTops) {

		double finalVector = 0;
		// left to right
		float aX = triangleTops.get(0).getX();
		float aY = triangleTops.get(0).getY();
		float aZ = triangleTops.get(0).getZ();

		float bX = triangleTops.get(1).getX();
		float bY = triangleTops.get(1).getY();
		float bZ = triangleTops.get(1).getZ();

		float cX = triangleTops.get(2).getX();
		float cY = triangleTops.get(2).getY();
		float cZ = triangleTops.get(2).getZ();

		double AB = Math.sqrt(Math.pow((aX - bX), 2) + Math.pow((aY - bY), 2)
				+ Math.pow((aZ - bZ), 2));
		double BC = Math.sqrt(Math.pow((bX - cX), 2) + Math.pow((bY - cY), 2)
				+ Math.pow((bZ - cZ), 2));
		double CA = Math.sqrt(Math.pow((cX - aX), 2) + Math.pow((cY - aY), 2)
				+ Math.pow((cZ - aZ), 2));

		finalVector = AB + BC + CA;
		return finalVector;
	}
}
