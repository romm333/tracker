package usertracking.prototype.profile;

import java.util.LinkedList;

@SuppressWarnings("rawtypes")
public abstract class ProfileCache extends LinkedList {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public abstract void loadProfiles();
	public abstract ProfileCache getProfiles();
}
