package usertracking.prototype.gui.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataUtils {
	
	
	public static List<String> getColumnValues(int columnId, String dataFilePath) {
		List<String> retList = new ArrayList<String>();

		try {

			BufferedReader in = new BufferedReader(new FileReader(dataFilePath));
			while (in.ready()) {
				
				String s = in.readLine();
				String[] columns = s.split(",");
				retList.add(columns[columnId]);
				
			}

			in.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return retList;
	}
	
	public static List<String> getUniqueValues(String dataFilePath) {
		List<String> retList = new ArrayList<String>();
		
		try {

			BufferedReader in = new BufferedReader(new FileReader(dataFilePath));
			while (in.ready()) {
				
				String s = in.readLine();
				String[] columns = s.split(",");
				
				if(!retList.contains(columns[0]))
					retList.add(columns[0]);
			}

			in.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return retList;
	}
	
	public static List<Float> getColumnFloatValues(int columnId, String dataFilePath) {
		List<Float> retList = new ArrayList<Float>();

		try {

			BufferedReader in = new BufferedReader(new FileReader(dataFilePath));
			while (in.ready()) {
				
				String s = in.readLine();
				String[] columns = s.split(",");
				float f = Float.parseFloat(columns[columnId]);
				
				retList.add(f);
			}

			in.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return retList;
	}
	
	
}
