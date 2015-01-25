package usertracking.prototype.profile;

import java.util.List;

public interface IUserProfile {
	public double getProfileFignature();
	public void setProfileName(String string);
	public void addUserFeatures(List<?> featuresForProfiling);
	public void calculateProfileSignature();
	public String getProfileName();
}
