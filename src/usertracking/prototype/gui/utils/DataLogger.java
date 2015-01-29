package usertracking.prototype.gui.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.OpenNI.SkeletonJoint;
import org.OpenNI.SkeletonJointPosition;
import org.OpenNI.StatusException;

import usertracking.prototype.profile.UserProfileBase;
import usertracking.prototype.profile.UserProfileByJoints;

public class DataLogger {
	public static void writeFile(String content) {
		try {
			FileWriter fstream = new FileWriter("out.txt", true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(content);
			out.newLine();
			out.close();
		} catch (Exception ex) {

		}
	}

	public static float[] readFileToArray(String path) {
		try {
			List<String> lines = Files.readAllLines(Paths.get(path), null);
			float[] ret = new float[lines.size()];

			for (int i = 0; i < lines.size(); i++) {
				float f = Float.parseFloat(lines.get(i));
				ret[i] = f;
			}

			return ret;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getCoordsAsCSV(UserProfileBase profile, int groupID) {
		// group 1 left-shoulder, neck,right shoulder,torso
		// group 2 left-shoulder, neck,right shoulder,torso. lefht hip, right
		// hip,
		// group 3 head, left-shoulder, neck,right shoulder,torso. lefht hip,
		// right hip, elbow left, elbow right
		int uid = profile.get_uid();
		
		try {
			SkeletonJointPosition head = profile.getRealJointPosition(uid, SkeletonJoint.HEAD);
			SkeletonJointPosition neck = profile.getRealJointPosition(uid, SkeletonJoint.NECK);
			
			SkeletonJointPosition left_shoulder = profile.getRealJointPosition(uid, SkeletonJoint.LEFT_SHOULDER);
			SkeletonJointPosition left_elbow = profile.getRealJointPosition(uid, SkeletonJoint.LEFT_ELBOW);
			SkeletonJointPosition left_hand = profile.getRealJointPosition(uid, SkeletonJoint.LEFT_HAND);

			SkeletonJointPosition right_shoulder = profile.getRealJointPosition(uid, SkeletonJoint.RIGHT_SHOULDER);
			SkeletonJointPosition right_elbow = profile.getRealJointPosition(uid, SkeletonJoint.RIGHT_ELBOW);
			SkeletonJointPosition right_hand = profile.getRealJointPosition(uid, SkeletonJoint.RIGHT_HAND);

			SkeletonJointPosition torso = profile.getRealJointPosition(uid, SkeletonJoint.TORSO);

			SkeletonJointPosition left_hip = profile.getRealJointPosition(uid, SkeletonJoint.LEFT_HIP);
			SkeletonJointPosition left_knee = profile.getRealJointPosition(uid, SkeletonJoint.LEFT_KNEE);
			SkeletonJointPosition left_foot = profile.getRealJointPosition(uid, SkeletonJoint.LEFT_FOOT);

			SkeletonJointPosition right_hip = profile.getRealJointPosition(uid, SkeletonJoint.RIGHT_HIP);
			SkeletonJointPosition right_knee = profile.getRealJointPosition(uid, SkeletonJoint.RIGHT_KNEE);
			SkeletonJointPosition right_foot = profile.getRealJointPosition(uid, SkeletonJoint.RIGHT_FOOT);
			
			double LeftShoulderToNeck = profile.getLenght(neck.getPosition(), left_shoulder.getPosition());
			double NeckToRightShoulder = profile.getLenght(right_shoulder.getPosition(), neck.getPosition());
			double RightShoulderToTorso = profile.getLenght(torso.getPosition(), right_shoulder.getPosition());
			
			double TorsoToLeftShoulder = profile.getLenght(right_shoulder.getPosition(),torso.getPosition());
			double TorsoToLeftHip = profile.getLenght(left_hip.getPosition(),torso.getPosition());
			double TorsoToRightHip = profile.getLenght(right_hip.getPosition(),torso.getPosition());
			
			double RightHipToLeftHip = profile.getLenght(left_hip.getPosition(),right_hip.getPosition());
			double HeadToNeck = profile.getLenght(neck.getPosition(),head.getPosition());
			
			
			switch (groupID) {
			case 1:
				return LeftShoulderToNeck +","+NeckToRightShoulder+","+RightShoulderToTorso+","+TorsoToLeftShoulder;
			case 2:
				return LeftShoulderToNeck +","+NeckToRightShoulder+","+RightShoulderToTorso+","+TorsoToLeftShoulder+","+
			TorsoToRightHip + "," + RightHipToLeftHip +"," + TorsoToRightHip;
			case 3:
				break;
			default:
				break;
			}
		} catch (StatusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
