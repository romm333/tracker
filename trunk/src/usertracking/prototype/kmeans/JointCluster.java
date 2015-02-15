package usertracking.prototype.kmeans;

import java.util.ArrayList;
import java.util.List;


public class JointCluster {
	private final List<JointVector> jointVectors;
	private JointVector centroid;
	
	public JointCluster(JointVector firstVector) {
		jointVectors = new ArrayList<JointVector>();
		centroid = firstVector;
	}
	
	public JointVector getCentroid(){
		return centroid;
	}
	
	public void updateCentroid(){
		double newa = 0d, newb = 0d, newc = 0d, newd = 0d;
		for (JointVector oneVector : jointVectors){
			newa += oneVector.a; newb += oneVector.b; newc += oneVector.c; newd += oneVector.d;
		}
		centroid = new JointVector(newa / jointVectors.size(), newb / jointVectors.size(), newc / jointVectors.size(), newd / jointVectors.size());
	}
	
	public List<JointVector> getJointVectors() {
		return jointVectors;
	}
	
	public String toString(){
		StringBuilder builder = new StringBuilder("This cluster contains the following points:\n");
		for (JointVector oneVector : jointVectors)
			builder.append(oneVector.toString() + ",\n");
		return builder.deleteCharAt(builder.length() - 2).toString();	
	}
}
