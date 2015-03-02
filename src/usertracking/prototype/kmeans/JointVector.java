package usertracking.prototype.kmeans;

public class JointVector {
	
	private int index = -1;	//denotes which Cluster it belongs to
	public double a,b,c,d;
	
	public JointVector(double a, double b, double c, double d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}
	
	public Double getSquareOfDistance(JointVector nextJointVector){
		return  Math.pow((a - nextJointVector.a),2) + Math.pow((b - nextJointVector.b), 2) + Math.pow((c - nextJointVector.c),2);
	}
	
	public Double getJointVectorLenght() {
		return this.a + this.b + this.c + this.d;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public String toString(){
		return "(" + a + "," + b + "," + c  + "," + d +")";
	}
}
