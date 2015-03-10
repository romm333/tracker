package usertracking.prototype.kmeans;

public class JointVector {
	
	private int index = -1;	//denotes which Cluster it belongs to
	public double a,b,c,d,e,f,g,h,i,j,k,l;
	
	public JointVector(double a, double b, double c, double d,double e, double f, double g, double h, double i, double j, double k, double l) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
		this.g = g;
		this.h = h;
		this.i = i;
		this.j = j;
		this.k = k;
		this.l = l;
	}
	
	public Double getSquareOfDistance(JointVector nextJointVector) {
		return Math.pow((a - nextJointVector.a), 2)
				+ Math.pow((b - nextJointVector.b), 2)
				+ Math.pow((c - nextJointVector.c), 2)
				+ Math.pow((d - nextJointVector.d), 2)
				+ Math.pow((e - nextJointVector.e), 2)
				+ Math.pow((f - nextJointVector.f), 2)
				+ Math.pow((j - nextJointVector.j), 2)
				+ Math.pow((h - nextJointVector.h), 2)
				+ Math.pow((i - nextJointVector.i), 2)
				+ Math.pow((j - nextJointVector.j), 2)
				+ Math.pow((k - nextJointVector.k), 2)
				+ Math.pow((l - nextJointVector.l), 2);
	}
	
	public Double getJointVectorLenght() {
		return this.a + this.b + this.c + this.d + this.e + this.f + this.g
				+ this.h + this.i + this.j + this.k + this.l;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public String toString(){
		return "(" + a + "," + b + "," + c  + "," + d +"," + e +"," + f +"," + g +"," + h +"," + i +"," + j +"," + k +"," + l +")";
	}
}
