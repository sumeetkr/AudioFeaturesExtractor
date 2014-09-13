package audioFeaturesExtractor.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class AFEFileWriter {

	public static void writeStringToFile(String text, String path, String label){
		try {
			
			String filename = path.substring(0, path.length()-4);
			File flToWrite = new File(filename+label);
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
	
	public static void appendTextToFile(String text, String path){
		try {
			
			String filename= path;
		    FileWriter fw = new FileWriter(filename,true); //the true will append the new data
		    fw.write(text);//appends the string to the file
		    fw.write("\n \n");
		    fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

	}

}
