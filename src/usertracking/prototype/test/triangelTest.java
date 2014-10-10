package usertracking.prototype.test;

public class triangelTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		float aX = 10;
		float aY = 10;
		float aZ = 10;
		
		float bX = 30;
		float bY = 10;
		float bZ = 10;
		
		float cX = 30;
		float cY = 30;
		float cZ = 10;
		
		double AB = Math.sqrt(Math.pow((aX-bX), 2) + Math.pow((aY-bY), 2) + Math.pow((aZ-bZ), 2));
		double BC = Math.sqrt(Math.pow((bX-cX), 2) + Math.pow((bY-cY), 2) + Math.pow((bZ-cZ), 2));
		double CA = Math.sqrt(Math.pow((cX-aX), 2) + Math.pow((cY-aY), 2) + Math.pow((cZ-cZ), 2));

		
		float eX = 10;
		float eY = 10;
		float eZ = -10;
		
		float rX = 60;
		float rY = 10;
		float rZ = -10;
		
		float tX = 60;
		float tY = 60;
		float tZ = -10;
		
		double ER = Math.sqrt(Math.pow((eX-rX), 2) + Math.pow((eY-rY), 2) + Math.pow((eZ-rZ), 2));
		double RT = Math.sqrt(Math.pow((rX-tX), 2) + Math.pow((rY-tY), 2) + Math.pow((rZ-tZ), 2));
		double TE = Math.sqrt(Math.pow((tX-eX), 2) + Math.pow((tY-eY), 2) + Math.pow((tZ-eZ), 2));
		
		double aber = AB/ER;
		double bcrt = BC/RT;
		double cate = CA/TE;
		
		
		
	}

}
