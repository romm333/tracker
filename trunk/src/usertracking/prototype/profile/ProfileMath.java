package usertracking.prototype.profile;

import java.util.List;

import javax.vecmath.Vector3d;

import org.OpenNI.Point3D;

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
	
	public static Vector3d getVector3d(Point3D point1, Point3D point2){
		float fromX = point1.getX();
		float fromY = point1.getY();
		float fromZ = point1.getZ();
		
		float toX = point2.getX();
		float toY = point2.getY();
		float toZ = point2.getZ();
		
		float resultX = toX - fromX;
		float resultY = toY - fromY;
		float resultZ = toZ - fromZ;
		
		Vector3d retVector = new Vector3d(resultX, resultY, resultZ);
		return retVector;
	}
	
	public static double getScalarProd(Vector3d vector1, Vector3d vector2){
		//a · b = ax · bx + ay · by + az · bz
		double fromX = vector1.x;
		double fromY = vector1.y;
		double fromZ = vector1.z;
		
		double toX = vector2.x;
		double toY = vector2.y;
		double toZ = vector2.z;
		
		return fromX * toX + fromY * toY + fromZ * toZ;
	}
	
	public static double getVectorAngleRad(Vector3d vector1, Vector3d vector2){
		return  vector1.angle(vector2);
	}
	
	public static double getVectorAngleDeg(Vector3d vector1, Vector3d vector2){
		return  Math.toDegrees(vector1.angle(vector2));
	}
}
