package usertracking.prototype.profile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class ProfileKMeans {
	private static final Random random = new Random();
	public final List<JointVector> allPoints;
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
		this.allPoints = Collections.unmodifiableList(points);
	}

	private JointVector getPointByLine(String line) {
		String[] abcd = line.split(",");
		return new JointVector(Double.parseDouble(abcd[0]),
				Double.parseDouble(abcd[1]), Double.parseDouble(abcd[2]),Double.parseDouble(abcd[3]));
	}

	/**
	 * step 1: get random seeds as initial centroids of the k clusters
	 */
	private void getInitialKRandomSeeds() {
		jointClusters = new JointClusters(allPoints);
		List<JointVector> kRandomPoints = getKRandomPoints();
		for (int i = 0; i < k; i++) {
			kRandomPoints.get(i).setIndex(i);
			jointClusters.add(new JointCluster(kRandomPoints.get(i)));
		}
	}

	private List<JointVector> getKRandomPoints() {
		List<JointVector> kRandomPoints = new ArrayList<JointVector>();
		boolean[] alreadyChosen = new boolean[allPoints.size()];
		int size = allPoints.size();
		for (int i = 0; i < k; i++) {
			int index = -1, r = random.nextInt(size--) + 1;
			for (int j = 0; j < r; j++) {
				index++;
				while (alreadyChosen[index])
					index++;
			}
			kRandomPoints.add(allPoints.get(index));
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

	public static void main(String[] args) {
		String pointsFilePath = "profiles/group1_real1.txt";
		ProfileKMeans kMeans = new ProfileKMeans(pointsFilePath, 2);
		List<JointCluster> pointsClusters = kMeans.getJointsClusters();
		for (int i = 0; i < kMeans.k; i++) {
			System.out.println("Cluster " + i + ": "
					+ pointsClusters.get(i).getCentroid());
			//System.out.println("Cluster " + i + ": " + pointsClusters.get(i));
		}
		
//		System.out.println();
//		
//		String pointsFilePath2 = "files/real2.csv";
//		KMeans kMeans2 = new KMeans(pointsFilePath, 2);
//		List<Cluster> pointsClusters2 = kMeans2.getPointsClusters();
//		for (int i = 0; i < kMeans2.k; i++) {
//			System.out.println("Cluster " + i + ": "
//					+ pointsClusters2.get(i).getCentroid());
//			//System.out.println("Cluster " + i + ": " + pointsClusters.get(i));
//		}
		
		System.out.println();
		
		String pointsFilePath3 = "profiles/group1_real2.txt";
		ProfileKMeans kMeans3 = new ProfileKMeans(pointsFilePath3, 2);
		List<JointCluster> pointsClusters3 = kMeans3.getJointsClusters();
		for (int i = 0; i < kMeans3.k; i++) {
			System.out.println("Cluster " + i + ": "
					+ pointsClusters3.get(i).getCentroid());
			//System.out.println("Cluster " + i + ": " + pointsClusters.get(i));
		}
		
		System.out.println();
		
//		for(int i=0; i < kMeans.k;i++){
//			Point centroid1 = kMeans.getPointsClusters().get(i).getCentroid();
//			
//			for(int j=0; j<kMeans3.k;j++){
//				Point centroid3 = kMeans3.getPointsClusters().get(j).getCentroid();
//				
//				double diff = (centroid1.x / centroid3.x);
//				System.out.println("centroid diff " + diff + " "+centroid1.x + " " +centroid3.x );
//			}
//		}
	}
}

