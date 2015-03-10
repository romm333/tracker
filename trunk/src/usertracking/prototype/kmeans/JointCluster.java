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
		double newa = 0d, newb = 0d, newc = 0d, newd = 0d, newe = 0d, newf = 0d, newg = 0d, newh = 0d,newi = 0d, newj = 0d, newk = 0d, newl = 0d;
		for (JointVector oneVector : jointVectors){
			newa += oneVector.a; newb += oneVector.b; newc += oneVector.c; newd += oneVector.d;
			newe += oneVector.e; newf += oneVector.f; newg += oneVector.g;newh += oneVector.h;
			newi += oneVector.i;newj += oneVector.j; newk += oneVector.k;newl += oneVector.l; 
		}
		centroid = new JointVector(newa / jointVectors.size(), newb / jointVectors.size(), newc / jointVectors.size(), newd / jointVectors.size(),
				newe / jointVectors.size(),newf / jointVectors.size(),newg / jointVectors.size(),newh / jointVectors.size(),
				newi / jointVectors.size(),newj / jointVectors.size(),newk / jointVectors.size(),newl / jointVectors.size());
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
