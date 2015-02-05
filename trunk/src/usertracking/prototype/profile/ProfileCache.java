package usertracking.prototype.profile;

import java.util.LinkedList;
//todo: refactor to abstract profile type!!!!!
public abstract class ProfileCache extends LinkedList<UserProfileByJointClusters> {
	private static final long serialVersionUID = 1L;
	public abstract ProfileCache loadProfiles();
}
