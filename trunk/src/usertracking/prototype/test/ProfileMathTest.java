package usertracking.prototype.test;

import java.util.HashMap;
import java.util.List;

import javax.vecmath.Vector3d;

import junit.framework.Assert;

import org.OpenNI.Point3D;
import org.junit.Test;

import failures.UserProfileBase;
import usertracking.prototype.gui.utils.DataUtils;
import usertracking.prototype.profile.ProfileMath;

public class ProfileMathTest {

	@Test
	public void testStdVariation() {

		ProfileMath pMath = new ProfileMath();

		List<Float> leftNeckToShoulderNumbers1 = DataUtils
				.getColumnFloatValues(0,
						"C:\\WORK\\PROJECTS\\diplomka\\group2_real1.txt");
		List<Float> leftNeckToShoulderNumbers2 = DataUtils
				.getColumnFloatValues(0,
						"C:\\WORK\\PROJECTS\\diplomka\\group2_real2.txt");
		List<Float> leftNeckToShoulderNumbers3 = DataUtils
				.getColumnFloatValues(0,
						"C:\\WORK\\PROJECTS\\diplomka\\group2_real3.txt");

		double average1 = pMath.getAverage(leftNeckToShoulderNumbers1);
		double average2 = pMath.getAverage(leftNeckToShoulderNumbers2);
		double average3 = pMath.getAverage(leftNeckToShoulderNumbers3);

		double standardVariation1 = pMath
				.getStandardDeviation(leftNeckToShoulderNumbers1);
		double standardVariation2 = pMath
				.getStandardDeviation(leftNeckToShoulderNumbers2);
		double standardVariation3 = pMath
				.getStandardDeviation(leftNeckToShoulderNumbers3);

		double variationCoef1 = pMath
				.getVariationCoefficient(leftNeckToShoulderNumbers1);
		double variationCoef2 = pMath
				.getVariationCoefficient(leftNeckToShoulderNumbers2);
		double variationCoef3 = pMath
				.getVariationCoefficient(leftNeckToShoulderNumbers3);

		System.out.println(average1);
		System.out.println(average2);
		System.out.println(average3);

		System.out.println("---------------------");

		System.out.println(standardVariation1);
		System.out.println(standardVariation2);
		System.out.println(standardVariation3);

		System.out.println("---------------------");

		System.out.println(variationCoef1);
		System.out.println(variationCoef2);
		System.out.println(variationCoef3);
		
		HashMap<Integer, List<Float>> ppp = DataUtils.getValues("profiles/1.csv");
		for(Integer oneInt : ppp.keySet()){
			
			double standardVariation = pMath.getStandardDeviation(ppp.get(oneInt));
			double variationCoef = pMath.getVariationCoefficient(ppp.get(oneInt));
			
			System.out.println(oneInt);
			System.out.println(standardVariation);
			System.out.println(variationCoef);
			
			System.out.println("---------------------");
			
		}
		
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testScalarProduct(){
		//a = {-1; -9; 9} 
		//b = {9; 6; 5} 
		Vector3d vector1 = new Vector3d(-1, -9, 9);
		Vector3d vector2 = new Vector3d(9, 6, 5);
		
		double result = ProfileMath.getScalarProd(vector1, vector2);
		System.out.println(result);
		
		Assert.assertEquals(-18, result, 0);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testGetVector3d(){
		//A = (-15,6,16) 
		//B = (-18,7,2) 
		//AB = (-3,1,-14) 
		Point3D point1 = new Point3D(-15, 6, 16);
		Point3D point2 = new Point3D(-18, 7, 2);
		
		Vector3d probe = new Vector3d(-3,1,-14);
		Vector3d result = ProfileMath.getVector3d(point1, point2);
		
		double vectorLength = probe.length();
		
		System.out.println(result);
		
		Assert.assertEquals(probe, result);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testGetLength(){
		//A = (-15,6,16) 
		//B = (-18,7,2) 
		//AB = (-3,1,-14) 
		Point3D point1 = new Point3D(-15, 6, 16);
		Point3D point2 = new Point3D(-18, 7, 2);
		
		UserProfileBase bb = new UserProfileBase() {
		};
		
		Vector3d resultV = ProfileMath.getVector3d(point1, point2);
		
		double length1 = bb.getLenght(point1, point2);
		double length2 = resultV.length();
		
		Assert.assertEquals(length1, length2,0);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testVectorAngle(){
		//a = {3; 4; 0}  b = {4; 4; 2}.
		//-202,11275200	-0,04417000	8,78720000
		//-89,51500000	-250,43062600	-56,98870000

		Vector3d vector1 = new Vector3d(-202.11275200, -0.04417000, 8.78720000);
		Vector3d vector2 = new Vector3d(-89.51500000,-250.43062600,	-56.98870000);
		
		double angle = vector1.angle(vector2);
		double degrees = Math.toDegrees(angle);
		
		Assert.assertEquals(0.36720802,angle,0);
	}
}
