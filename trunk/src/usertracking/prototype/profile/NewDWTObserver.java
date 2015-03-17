package usertracking.prototype.profile;

import usertracking.prototype.classes.SimpleTracker;
import usertracking.prototype.kmeans.DWT;
import usertracking.prototype.kmeans.JointVector;

public class NewDWTObserver extends ProfileObserver {

	public NewDWTObserver(SimpleTracker _tracker) {
		super(_tracker);
	}
	
	@Override
	public Double getProfileDistance(JointVector jv, Integer profileMeanIndex) {
		float[] profileFeatures = new float[12];

		profileFeatures[0] = (float) jv.a;
		profileFeatures[1] = (float) jv.b;
		profileFeatures[2] = (float) jv.c;
		profileFeatures[3] = (float) jv.d;
		profileFeatures[4] = (float) jv.e;
		profileFeatures[5] = (float) jv.f;
		profileFeatures[6] = (float) jv.g;
		profileFeatures[7] = (float) jv.h;
		profileFeatures[8] = (float) jv.i;
		profileFeatures[9] = (float) jv.j;
		profileFeatures[10] = (float) jv.k;
		profileFeatures[11] = (float) jv.l;

		float[] profileMeanFeatures = new float[12];

		profileMeanFeatures[0] = (float) getTracker().getProfileMeans().get(
				profileMeanIndex).profileMean.a;
		profileMeanFeatures[1] = (float) getTracker().getProfileMeans().get(
				profileMeanIndex).profileMean.b;
		profileMeanFeatures[2] = (float) getTracker().getProfileMeans().get(
				profileMeanIndex).profileMean.c;
		profileMeanFeatures[3] = (float) getTracker().getProfileMeans().get(
				profileMeanIndex).profileMean.d;
		profileMeanFeatures[4] = (float) getTracker().getProfileMeans().get(
				profileMeanIndex).profileMean.e;
		profileMeanFeatures[5] = (float) getTracker().getProfileMeans().get(
				profileMeanIndex).profileMean.f;
		profileMeanFeatures[6] = (float) getTracker().getProfileMeans().get(
				profileMeanIndex).profileMean.g;
		profileMeanFeatures[7] = (float) getTracker().getProfileMeans().get(
				profileMeanIndex).profileMean.h;
		profileMeanFeatures[8] = (float) getTracker().getProfileMeans().get(
				profileMeanIndex).profileMean.i;
		profileMeanFeatures[9] = (float) getTracker().getProfileMeans().get(
				profileMeanIndex).profileMean.j;
		profileMeanFeatures[10] = (float) getTracker().getProfileMeans().get(
				profileMeanIndex).profileMean.k;
		profileMeanFeatures[11] = (float) getTracker().getProfileMeans().get(
				profileMeanIndex).profileMean.l;

		DWT dwt = new DWT(profileMeanFeatures, profileFeatures);
		return dwt.getWrapingDistance();
	}
}
