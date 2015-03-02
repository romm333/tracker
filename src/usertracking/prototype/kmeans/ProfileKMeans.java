package usertracking.prototype.kmeans;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import usertracking.prototype.profile.ProfileMath;


public class ProfileKMeans {
	private static final Random random = new Random();
	public final List<JointVector> allJointVectors;
	public final int k;
	private JointClusters jointClusters; // the k Clusters

	/**
	 * @param pointsFile
	 *            : the csv file for input points
	 * @param k
	 *            : number of clusters
	 */
	public ProfileKMeans(String pointsFile, int k) {
		if (k < 2)
			new Exception("The value of k should be 2 or more.")
					.printStackTrace();
		this.k = k;
		List<JointVector> points = new ArrayList<JointVector>();
		try {
			InputStreamReader read = new InputStreamReader(new FileInputStream(
					pointsFile), "UTF-8");
			BufferedReader reader = new BufferedReader(read);
			String line;
			while ((line = reader.readLine()) != null)
				points.add(getPointByLine(line));
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		this.allJointVectors = Collections.unmodifiableList(points);
	}

	public ProfileKMeans(List<JointVector> userJointVectors, int k) {
		if (k < 2)
			new Exception("The value of k should be 2 or more.")
					.printStackTrace();
		
		this.k = k;
		this.allJointVectors = Collections.unmodifiableList(userJointVectors);
	}

	
	private JointVector getPointByLine(String line) {
		String[] abcd = line.split(",");
		return new JointVector(Double.parseDouble(abcd[0]),
				Double.parseDouble(abcd[1]), Double.parseDouble(abcd[2]),Double.parseDouble(abcd[2]));
	}

	/**
	 * step 1: get random seeds as initial centroids of the k clusters
	 */
	private void getInitialKRandomSeeds() {
		jointClusters = new JointClusters(allJointVectors);
		List<JointVector> kRandomPoints = getKRandomPoints();
		for (int i = 0; i < k; i++) {
			kRandomPoints.get(i).setIndex(i);
			jointClusters.add(new JointCluster(kRandomPoints.get(i)));
		}
	}

	private List<JointVector> getKRandomPoints() {
		List<JointVector> kRandomPoints = new ArrayList<JointVector>();
		boolean[] alreadyChosen = new boolean[allJointVectors.size()];
		int size = allJointVectors.size();
		for (int i = 0; i < k; i++) {
			int index = -1, r = random.nextInt(size--) + 1;
			for (int j = 0; j < r; j++) {
				index++;
				while (alreadyChosen[index])
					index++;
			}
			kRandomPoints.add(allJointVectors.get(index));
			alreadyChosen[index] = true;
		}
		return kRandomPoints;
	}

	/**
	 * step 2: assign points to initial Clusters
	 */
	private void getInitialClusters() {
		jointClusters.assignPointsToJointClusters();
	}

	/**
	 * step 3: update the k Clusters until no changes in their members occur
	 */
	private void updateClustersUntilNoChange() {
		boolean isChanged = jointClusters.updateJointClusters();
		while (isChanged)
			isChanged = jointClusters.updateJointClusters();
	}

	/**
	 * do K-means clustering with this method
	 */
	public List<JointCluster> getJointsClusters() {
		if (jointClusters == null) {
			getInitialKRandomSeeds();
			getInitialClusters();
			updateClustersUntilNoChange();
		}
		return jointClusters;
	}
	
	public JointVector getStandardDeviationVector(JointVector vector1, JointVector vector2){
		ProfileMath pMath = new ProfileMath();
		
		List<Float> listA = new ArrayList<Float>(2);
		float A1 = (float)vector1.a;
		float A2 = (float)vector2.a;
		
		listA.add(A1);
		listA.add(A2);
		
		double variationA = pMath.getVariationCoefficient(listA);
		
		List<Float> listB = new ArrayList<Float>(2);
		float B1 = (float)vector1.b;
		float B2 = (float)vector2.b;
		
		listB.add(B1);
		listB.add(B2);
		
		double variationB = pMath.getVariationCoefficient(listB);
		
		List<Float> listC = new ArrayList<Float>(2);
	 	float C1 = (float)vector1.c;
		float C2 = (float)vector2.c;
		
		listC.add(C1);
		listC.add(C2);
		double variationC = pMath.getVariationCoefficient(listC);
				
		List<Float> listD = new ArrayList<Float>(2);
		float D1 = (float)vector1.d;
		float D2 = (float)vector2.d;
		
		listD.add(D1);
		listD.add(D2);
		double variationD = pMath.getVariationCoefficient(listD);
		
		JointVector jointVector = new JointVector(variationA, variationB, variationD, variationC);
		return jointVector;
	}
	
	public static void main(String[] args) {
		String pointsFilePath = "profiles/1.csv";
		ProfileKMeans kMeans = new ProfileKMeans(pointsFilePath, 3);
		List<JointCluster> user1Joints = kMeans.getJointsClusters();
		for (int i = 0; i < kMeans.k; i++) {
			System.out.println("Cluster " + i + ": "
					+ user1Joints.get(i).getCentroid() + " " + user1Joints.get(i).getJointVectors().size());
			//System.out.println("Cluster " + i + ": " + pointsClusters.get(i));
		}
		
		System.out.println();
		
		JointVector jv = new JointVector(305.3569416516529, 373.06668553774654, 194.83763499833842,194.83763499833842);		
		for (int i = 0; i < kMeans.k; i++) {
			double dd = user1Joints.get(i).getCentroid().getSquareOfDistance(jv);
			System.out.println(dd);
		}
	}
}

