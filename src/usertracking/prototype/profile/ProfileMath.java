package usertracking.prototype.profile;

import java.util.List;

public class ProfileMath {
	public double getStandardDeviation(List<Float> arrayOfNumbers) {
		double result = 0;
		for (double oneNumber : arrayOfNumbers) {
			result += oneNumber;
		}

		double average = result / arrayOfNumbers.size();
		
		double finalsumX = 0;
		for (int i = 0; i < arrayOfNumbers.size(); i++) {
			double fvalue = (Math.pow((arrayOfNumbers.get(i) - average), 2));
			finalsumX += fvalue;
		}

		return  Math.sqrt(finalsumX / (arrayOfNumbers.size()));
	}

	public double getAverage(List<Float> numbers) {
		double result = 0;
		for (double oneNumber : numbers) {
			result += oneNumber;
		}

		return result / numbers.size();
	}

	public double getVariationCoefficient(List<Float> arrayOfNumbers) {
		double result = 0;
		for (double oneNumber : arrayOfNumbers) {
			result += oneNumber;
		}

		double average = result / arrayOfNumbers.size();
		
		double finalsumX = 0;
		for (int i = 0; i < arrayOfNumbers.size(); i++) {
			double fvalue = (Math.pow((arrayOfNumbers.get(i) - average), 2));
			finalsumX += fvalue;
		}

		double standardVariation = Math.sqrt(finalsumX / (arrayOfNumbers.size()));
		
		return (standardVariation/average) * 100;
	}
}
