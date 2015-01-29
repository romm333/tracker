package usertracking.prototype.test;

import org.junit.Test;

import usertracking.prototype.gui.utils.DataLogger;
import usertracking.prototype.profile.DWT;

public class DWTtest {

	@Test
	public void test() {
		float[] template = DataLogger.readFileToArray("C:\\MYFILES\\Workspace\\UserTracker.Prototype\\out1.txt");
		float[] data = DataLogger.readFileToArray("C:\\MYFILES\\Workspace\\UserTracker.Prototype\\out.txt");
		
		DWT dwt =  new DWT(data, template);
		System.out.println(dwt.toString());
	}

}
