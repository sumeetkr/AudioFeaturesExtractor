package audioFeaturesExtractor.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class FileWriter {

	public static void writeStringToFile(String text, String path, String label){
		try {
			
			String filename = path.substring(0, path.length()-4);
			File flToWrite = new File(filename+label);
			
//			Logger.logMessageOnConsole("Converting to audible format "+ file.getName()+ " of size "+ rawAudioList.size());


			PrintWriter writer = new PrintWriter(flToWrite, "UTF-8");
			writer.println(text);
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

	}
}
