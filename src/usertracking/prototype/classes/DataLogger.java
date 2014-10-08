package usertracking.prototype.classes;

import java.io.*;

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
}
