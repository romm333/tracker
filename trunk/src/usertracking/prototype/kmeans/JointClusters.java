package usertracking.prototype.kmeans;

import java.util.LinkedList;
import java.util.List;


public class JointClusters extends LinkedList<JointCluster>{
	private static final long serialVersionUID = 1L;
	private final List<JointVector> allJointVectors;
	private boolean isChanged;
	
	public JointClusters(List<JointVector> allPoints){
		this.allJointVectors = allPoints;
	}
	
	/**@param jointVector
	 * @return the index of the Cluster nearest to the point
	 */
	public Integer getNearestJointCluster(JointVector jointVector){
		double minSquareOfDistance = Double.MAX_VALUE;
		int itsIndex = -1;
		for (int i = 0 ; i < size(); i++){
			double squareOfDistance = jointVector.getSquareOfDistance(get(i).getCentroid());
			if (squareOfDistance < minSquareOfDistance){
				minSquareOfDistance = squareOfDistance;
				itsIndex = i;
			}
		}
		return itsIndex;
	}

	public boolean updateJointClusters(){
		for (JointCluster oneCluster : this){
			oneCluster.updateCentroid();
			oneCluster.getJointVectors().clear();
		}
		isChanged = false;
		assignPointsToJointClusters();
		return isChanged;
	}
	
	public void assignPointsToJointClusters(){
		for (JointVector point : allJointVectors){
			int previousIndex = point.getIndex();
			int newIndex = getNearestJointCluster(point);
			if (previousIndex != newIndex)
				isChanged = true;
			
			JointCluster target = get(newIndex);
			point.setIndex(newIndex);
			target.getJointVectors().add(point);
		}
	}
}
