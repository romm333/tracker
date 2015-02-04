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
	
	public Double getSquareOfDistance(JointVector nextJointLength){
		return  (a - nextJointLength.a) * (a - nextJointLength.a)
				+ (b - nextJointLength.b) *  (b - nextJointLength.b) 
				+ (c - nextJointLength.c) *  (c - nextJointLength.c)
				+ (d - nextJointLength.d) *  (d - nextJointLength.d);
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
