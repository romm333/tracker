package usertracking.prototype.test;

import java.util.List;

import usertracking.prototype.gui.utils.DataLogger;
import usertracking.prototype.gui.utils.DataUtils;

public class removeDublicates {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> uniqueStrings = DataUtils.getUniqueValues("C:\\WORK\\PROJECTS\\K-Means\\files\\3d1.csv");
		StringBuilder sb = new StringBuilder();
		String ret = "";
		
		for(String oneString : uniqueStrings){
			
			ret+=oneString + "\r\n";
			
		}
		
		DataLogger.writeFile(ret);
	}

}
