package usertracking.prototype.classes;

import java.util.LinkedList;
import java.util.List;

import org.OpenNI.Point3D;

public class UserProfile {
	private String profileName;
	private int usageCount;

	public double AB;
	public double BC;
	public double CA;

	private List<Point3D> triangleTops = new LinkedList<Point3D>();

	public void addTriangleTop(Point3D triangleTop) {
		triangleTops.add(triangleTop);
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

	public void calculateProfile() {
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

		AB = Math.sqrt(Math.pow((aX - bX), 2) + Math.pow((aY - bY), 2)
				+ Math.pow((aZ - bZ), 2));
		BC = Math.sqrt(Math.pow((bX - cX), 2) + Math.pow((bY - cY), 2)
				+ Math.pow((bZ - cZ), 2));
		CA = Math.sqrt(Math.pow((cX - aX), 2) + Math.pow((cY - aY), 2)
				+ Math.pow((cZ - aZ), 2));
	}
}
