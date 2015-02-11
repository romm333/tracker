package usertracking.prototype.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMedoids;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.FileHandler;

import org.junit.Test;

import usertracking.prototype.profile.KMeansT;

public class JMLTest {

	@Test
	public void test() throws IOException {
		 /* Load a dataset */
        Dataset data = FileHandler.loadDataset(new File("profiles/1.csv"), 4, ",");
        /*
         * Create a new instance of the KMeans algorithm, with no options
         * specified. By default this will generate 4 clusters.
         */
        KMeansT km = new KMeansT();
        /*
         * Cluster the data, it will be returned as an array of data sets, with
         * each dataset representing a cluster
         */
        Dataset[] clusters = km.cluster(data);
        
        for(Dataset oneDataSet : clusters){
        	for(int i=0; i < oneDataSet.size(); i++){
        		Instance ii = oneDataSet.get(i);
        		//System.out.println(ii.toString());
        	}
        	
        	System.out.println("Points count " + oneDataSet.size());
        }
        
        for(Instance oneDataSet : km.getCentroids()){
        		//System.out.println(ii.toString());
        	
        	System.out.println("Centroid " + oneDataSet.toString());
        }
        
        
        System.out.println("Cluster count: " + clusters.length);
        

	}

}
