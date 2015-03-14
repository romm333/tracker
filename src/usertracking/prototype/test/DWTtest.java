package usertracking.prototype.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import usertracking.prototype.gui.utils.DataLogger;
import usertracking.prototype.gui.utils.DataUtils;
import usertracking.prototype.kmeans.DWT;
import usertracking.prototype.kmeans.JointCluster;
import usertracking.prototype.kmeans.ProfileKMeans;
import usertracking.prototype.profile.DummyProfile;

public class DWTtest {

	@Test
	public void test() {
////		float[] template = DataLogger.readFileToArray("C:\\MYFILES\\Workspace\\UserTracker.Prototype\\out1.txt");
////		float[] data = DataLogger.readFileToArray("C:\\MYFILES\\Workspace\\UserTracker.Prototype\\out.txt");
//		float[] template1 = {182.6385651f,182.638588f,299.537162f,299.537162f,261.4814786f,219.1517133f,261.4814306f,310.4660113f,310.465485f,310.4654181f,310.4655151f,223.7175531f,237.4145661f};
//
//		float[] template = {190.8136036f, 190.8136036f,	277.9826238f,	277.9826238f,	225.222593f,	198.6020303f,	225.2225898f,	312.0630428f	,310.8820212f,	310.6354771f,	330.1717036f,	218.5235099f,	202.1497566f};
//		float[] data = {190.8136036f, 190.8136036f,	277.9826238f,	277.9826238f,	225.222593f,	198.6020303f,	225.2225898f,	312.0630428f	,310.8820212f,	310.6354771f,	330.1717036f,	218.5235099f,	202.1497566f};
//		float[] data1 = {190.815043f,190.8150142f,277.9974859f,277.9974859f,225.2394226f,198.6006069f,225.2393692f,311.956163f,300.9412772f,304.9392688f,331.4735339f,217.8291183f,202.168829f};
//
//		float[][] coordinates = DataUtils.readFileToArrays("C:\\MYFILES\\Workspace\\UserTracker.Prototype\\out1.txt",null, ",");
//		
//		
//		//float[] template = DataLogger.readFileToArray("C:\\MYFILES\\Workspace\\UserTracker.Prototype\\out1.txt");
//		//float[] data = DataLogger.readFileToArray("C:\\MYFILES\\Workspace\\UserTracker.Prototype\\out.txt");
//		
//		DWT dwt =  new DWT(data1, template1);
//		
//		for(int i=0; i <coordinates.length; i++ ){
//			DWT dwt1 =  new DWT(coordinates[i], template1);
//			System.out.println(dwt1.getWrapingDistance());
//			
//		}
		
		ProfileKMeans profileData;
		List<JointCluster> jointClusters;
		List<DummyProfile> profileMeans;
		
		profileData = new ProfileKMeans("profiles/1.csv", 4);
		jointClusters = profileData.getJointsClusters();
		
		profileMeans = new ArrayList<DummyProfile>();
		float[] dataString = {0f,0f,0f,0f,0f,0f,0f,0f,0f,0f,0f,0f,0f};
		
		for (int i = 0; i < profileData.k; i++) {
			System.out.println("Cluster " + i + ": "
					+ jointClusters.get(i).getCentroid() + " " + jointClusters.get(i).getJointVectors().size());
			float[] template1 = new float[13];
			
			template1[0] = (float) jointClusters.get(i).getCentroid().a;
			template1[1] = (float) jointClusters.get(i).getCentroid().b;
			template1[2] = (float) jointClusters.get(i).getCentroid().c;
			template1[3] = (float) jointClusters.get(i).getCentroid().d;
			template1[4] = (float) jointClusters.get(i).getCentroid().e;
			template1[5] = (float) jointClusters.get(i).getCentroid().f;
			template1[6] = (float) jointClusters.get(i).getCentroid().g;
			template1[7] = (float) jointClusters.get(i).getCentroid().h;
			template1[8] = (float) jointClusters.get(i).getCentroid().i;
			template1[10] = (float)jointClusters.get(i).getCentroid().j;
			template1[11] = (float) jointClusters.get(i).getCentroid().k;
			template1[12] = (float) jointClusters.get(i).getCentroid().l;
			
//			template1[0] = (float) (Math.pow(jointClusters.get(i).getCentroid().a,2) + jointClusters.get(i).getCentroid().a);
//			template1[1] = (float) (Math.pow(jointClusters.get(i).getCentroid().b,2) + jointClusters.get(i).getCentroid().b);
//			template1[2] = (float) (Math.pow(jointClusters.get(i).getCentroid().c,2) + jointClusters.get(i).getCentroid().c);
//			template1[3] = (float) (Math.pow(jointClusters.get(i).getCentroid().d,2)+jointClusters.get(i).getCentroid().d);
//			template1[4] = (float) (Math.pow(jointClusters.get(i).getCentroid().e,2)+jointClusters.get(i).getCentroid().e);
//			template1[5] = (float) (Math.pow(jointClusters.get(i).getCentroid().f,2)+jointClusters.get(i).getCentroid().f);
//			template1[6] = (float) (Math.pow(jointClusters.get(i).getCentroid().g,2)+jointClusters.get(i).getCentroid().g);
//			template1[7] = (float) (Math.pow(jointClusters.get(i).getCentroid().h,2)+jointClusters.get(i).getCentroid().h);
//			template1[8] = (float) (Math.pow(jointClusters.get(i).getCentroid().i,2)+jointClusters.get(i).getCentroid().i);
//			template1[10] = (float) (Math.pow(jointClusters.get(i).getCentroid().j,2)+jointClusters.get(i).getCentroid().j);
//			template1[11] = (float) (Math.pow(jointClusters.get(i).getCentroid().k,2)+jointClusters.get(i).getCentroid().k);
//			template1[12] = (float) (Math.pow(jointClusters.get(i).getCentroid().l,2)+jointClusters.get(i).getCentroid().l);
		//1	227.2561121	376.7267777	209.7775054	540.9049593	462.1140511	303.0081784	195.5014287	100.4068338	49.55082668
			
			dataString[0] = 227.2561121f;
			dataString[1] = 376.7267777f;
			dataString[2] = 209.7775054f;
			dataString[3] = 540.9049593f;
			dataString[4] = 462.1140511f;
			dataString[5] = 303.0081784f;
			dataString[6] = 195.5014287f;
			dataString[7] = 100.4068338f;
			dataString[8] = 49.55082668f;
			dataString[9] = 0f;
			dataString[10] = 0f;
			dataString[11] = 0f;
			dataString[12] = 0f;
			
			DWT dwt = new DWT(template1, dataString);
			
		System.out.println( dwt.getWrapingDistance() );
	}

	}}
