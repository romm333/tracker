package usertracking.prototype.gui.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
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

	
	public static float[][] readFileToArrays(String path, String splitOption) {
		try {
			Path file_path = Paths.get("profiles", "roman2.csv");

			
			List<String> lines = Files.readAllLines(file_path, StandardCharsets.US_ASCII);
			float[][] ret = new float[lines.size()][];

			for (int i = 0; i < lines.size(); i++) {
				String[] splits = lines.get(i).split(splitOption);
				float [] parsedValues = new float[splits.length];
				for (int j = 0; j < splits.length; j++) {
					parsedValues[j] = Float.parseFloat(splits[j]);
				}
				ret[i] = parsedValues;
			}

			return ret;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<String> getUniqueValues(String dataFilePath) {
		List<String> retList = new ArrayList<String>();

		try {

			BufferedReader in = new BufferedReader(new FileReader(dataFilePath));
			while (in.ready()) {

				String s = in.readLine();
				String[] columns = s.split(",");

				if (!retList.contains(columns[0]))
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

	public static List<Float> getColumnFloatValues(int columnId,
			String dataFilePath) {
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

	public static HashMap<Integer, List<Float>> getValues(String dataFilePath) {
		HashMap<Integer, List<Float>> retList = new HashMap<Integer, List<Float>>();

		try {

			BufferedReader in = new BufferedReader(new FileReader(dataFilePath));
			String s = in.readLine();
			in.close();

			String[] columns = s.split(",");

			for (int i = 0; i < columns.length; i++) {
				List<Float> columnValues = getColumnFloatValues(i, dataFilePath);
				retList.put(i, columnValues);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return retList;
	}

}
