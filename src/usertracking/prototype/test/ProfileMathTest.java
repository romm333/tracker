package usertracking.prototype.test;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import usertracking.prototype.gui.utils.DataLogger;
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
			
			double average = pMath.getAverage(ppp.get(oneInt));
			double standardVariation = pMath.getStandardDeviation(ppp.get(oneInt));
			double variationCoef = pMath.getVariationCoefficient(ppp.get(oneInt));
			
			System.out.println(oneInt);
			System.out.println(standardVariation);
			System.out.println(variationCoef);
			
			System.out.println("---------------------");
			
		}
		
	}

}
