package usertracking.prototype.profile;

import usertracking.prototype.classes.SimpleTracker;
import usertracking.prototype.kmeans.JointVector;

public class NewKmeansObserver extends ProfileObserver {

	public NewKmeansObserver(SimpleTracker _tracker) {
		super(_tracker);
	}

	@Override
	public Double getProfileDistance(JointVector jv, Integer profileMeanIndex) {
		return getTracker().getProfileMeans().get(profileMeanIndex).profileMean.getSquareOfDistance(jv);
	}
}
