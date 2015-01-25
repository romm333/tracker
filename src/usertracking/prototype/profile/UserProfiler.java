package usertracking.prototype.profile;

import java.util.LinkedList;
import java.util.List;

public class UserProfiler {

	private List<IUserProfile> profileList = new LinkedList<IUserProfile>();

	public void insertProfile(IUserProfile profile) {
		profileList.add(profile);
	}

	public void removeProfile(IUserProfile profile) {
		profileList.remove(profile);
	}

	public void removeProfile(int profileIndex) {
		profileList.remove(profileIndex);
	}

	public IUserProfile getSimilarProfile(IUserProfile profile) {

		for (IUserProfile oneProfile : profileList) {
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

			double vectorsAtt = oneProfile.getProfileFignature()
					/ profile.getProfileFignature();

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
}
