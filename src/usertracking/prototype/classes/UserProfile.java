package usertracking.prototype.classes;

import java.io.ObjectInputStream.GetField;
import java.util.List;

import org.OpenNI.Point3D;

public class UserProfile {
	private String profileName;
	private int usageCount;

	public double torsoToNeck;
	public double torsoToLeftShoulder;
	public double torsoToRigthShoulder;
	public double torsoToHead;

	public double LStH;
	public double finalVector;

	public String movingDirection = "";

	public void addProfileJoints(List<Point3D> profileJoints) {

		Point3D torsoCoords = profileJoints.get(0);
		Point3D lefhShoulderCoords = profileJoints.get(1);
		Point3D rightShoulderCoords = profileJoints.get(2);

		Point3D neckCoords = profileJoints.get(3);
		Point3D headCoords = profileJoints.get(4);

		torsoToNeck = getLenght(torsoCoords, neckCoords);
		torsoToLeftShoulder = getLenght(torsoCoords, lefhShoulderCoords);
		torsoToRigthShoulder = getLenght(torsoCoords, rightShoulderCoords);
		torsoToHead = getLenght(torsoCoords, headCoords);

		if (neckCoords.getZ() < headCoords.getZ())
			movingDirection = "MOVING BACK";
		else
			movingDirection = "MOVING FORWARD";
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public int getUsageCount() {
		return usageCount;
	}

	public void setUsageCount(int usageCount) {
		this.usageCount = usageCount;
	}

	public double getLenght(Point3D joint1, Point3D joint2) {
		return Math.sqrt(Math.pow(joint1.getX() - joint2.getX(), 2)
				+ Math.pow(joint1.getY() - joint2.getY(), 2)
				+ Math.pow(joint1.getZ() - joint2.getZ(), 2));
	}

	public void calculateVectorLength() {
		// double p = (HtRS + RStT + TtLS) / 2;
		// finalVector = Math.sqrt(p * (p - HtRS) * (p - RStT) * (p - TtLS));

		// double finalVector = 0;
		// // left to right
		// float hX = triangleTops.get(0).getX();
		// float hY = triangleTops.get(0).getY();
		// float hZ = triangleTops.get(0).getZ();
		//
		// float rsX = triangleTops.get(1).getX();
		// float rsY = triangleTops.get(1).getY();
		// float rsZ = triangleTops.get(1).getZ();
		//
		// float tX = triangleTops.get(2).getX();
		// float tY = triangleTops.get(2).getY();
		// float tZ = triangleTops.get(2).getZ();
		//
		// float lsX = triangleTops.get(3).getX();
		// float lsY = triangleTops.get(3).getY();
		// float lsZ = triangleTops.get(3).getZ();
		//
		// double HtRS = Math.sqrt(Math.pow((hX - rsX), 2) + Math.pow((hY -
		// rsY), 2)
		// + Math.pow((hZ - rsZ), 2));
		// double RStT = Math.sqrt(Math.pow((rsX - tX), 2) + Math.pow((rsY -
		// tY), 2)
		// + Math.pow((rsZ - tZ), 2));
		// double TtLS = Math.sqrt(Math.pow((tX - lsX), 2) + Math.pow((tY -
		// lsY), 2)
		// + Math.pow((tZ - lsZ), 2));
		// double LStH = Math.sqrt(Math.pow((lsX - tX), 2) + Math.pow((lsY -
		// tY), 2)
		// + Math.pow((lsZ - tZ), 2));

		finalVector = torsoToLeftShoulder + torsoToRigthShoulder;
		// return finalVector;
	}

}
