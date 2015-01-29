package usertracking.prototype.gui.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DataLogger {
	public static void writeFile(String content) {
		try {
			FileWriter fstream = new FileWriter("out.txt", true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(content);
			out.newLine();
			out.close();
		} catch (Exception ex) {

		}
	}
	
	public static float[] readFileToArray(String path){
		try {
			List<String> lines = Files.readAllLines(Paths.get(path));
			float[] ret = new float[lines.size()];
			
			for(int i=0; i < lines.size(); i++){
				float f = Float.parseFloat(lines.get(i));
				ret[i] = f;
			}
			
			return ret;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	
}
