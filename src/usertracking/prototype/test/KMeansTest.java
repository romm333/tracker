package usertracking.prototype.test;

import java.io.File;

//import net.sf.javaml.clustering.Clusterer;
//import net.sf.javaml.clustering.KMeans;
//import net.sf.javaml.core.Dataset;
//import net.sf.javaml.core.DefaultDataset;
//import net.sf.javaml.core.Instance;
//import net.sf.javaml.tools.InstanceTools;
//import net.sf.javaml.tools.data.FileHandler;
//
//
//
//
//
//import org.junit.Test;
//
//public class KMeansTest {
//
//	@Test
//	public void test() throws Exception {
//		/* Load a dataset */
//		Dataset data = new DefaultDataset();
//        for (int i = 0; i < 10; i++) {
//            Instance tmpInstance = InstanceTools.randomInstance(25);
//            data.add(tmpInstance);
//        }	
//		
//		//Dataset data = FileHandler.loadDataset(new File("C:\\WORK\\PROJECTS\\diplomka\\group1_real4.txt"));
//			
//			//FileHandler.exportDataset(data, new File("text.txt"));
//			
//			data = FileHandler.loadDataset(new File("text.txt"));
//			/*
//			 * Create a new instance of the KMeans algorithm, with no options
//			 * specified. By default this will generate 4 clusters.
//			 */
//			Clusterer km = new KMeans();
//			/*
//			 * Cluster the data, it will be returned as an array of data sets,
//			 * with each dataset representing a cluster
//			 */
//			Dataset[] clusters = km.cluster(data);
//			System.out.println("Cluster count: " + clusters.length);
//		
//	}
//
//}
