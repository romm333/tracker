package usertracking.prototype.profile;

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
		return  (a - nextJointVector.a) * (a - nextJointVector.a)
				+ (b - nextJointVector.b) *  (b - nextJointVector.b) 
				+ (c - nextJointVector.c) *  (c - nextJointVector.c)
				+ (d - nextJointVector.d) *  (d - nextJointVector.d);
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
