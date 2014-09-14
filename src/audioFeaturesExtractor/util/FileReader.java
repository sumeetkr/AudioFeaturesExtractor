package audioFeaturesExtractor.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.sf.javaml.utils.ArrayUtils;

import weka.core.parser.java_cup.internal_error;

public class FileReader {
	
	public List<AudioData> readFile(String path, int frameSize) throws Exception{
	
		File testFile = new File(path);
		
		List<AudioData> datas = new ArrayList<AudioData>();
	    Scanner scanner = new Scanner(new FileInputStream( testFile));

	    try {
	      while (scanner.hasNextLine()){
	  	    String[] strArray = scanner.nextLine().split(",");
		   	  	    
		    for(int i = 0; i < strArray.length ; i++) {
		    		short dataShorts[] = new short[frameSize];
		    		int j =0;
		    		while(j< frameSize && i<strArray.length){
		    			dataShorts[j] = Short.parseShort(strArray[i].trim());	
		    			j++;
		    			i++;
		    		}
		    		AudioData rawAudioData = new AudioData(i, dataShorts);
		    		datas.add(rawAudioData);
		    }
	      }
	    }catch (Exception e) {
			System.out.println("Exception in readng" + path +" " + e.getMessage());
		}
	    finally{
	      scanner.close();
	    }
	    
	    return datas;
	}
	
	private AudioData groupRawAudioData(String string,int frameSize) throws Exception{
	    String[] strArray = string.split(",");
	    short dataShorts[] = new short[strArray.length];
	    //skip the 1st, which has time information
	    for(int i = 1; i < strArray.length ; i++) {
	    		dataShorts[i-1] = Short.parseShort(strArray[i].trim());	
	    }
	    AudioData rawAudioData = new AudioData(Long.parseLong(strArray[0]), dataShorts);
	    
	    return rawAudioData;
	}
	
	public List<AudioData> readFile(String path) throws Exception{
		File testFile = new File(path);
		
		List<AudioData> datas = new ArrayList<AudioData>();
	    Scanner scanner = new Scanner(new FileInputStream( testFile));

	    try {
	      while (scanner.hasNextLine()){
	    	  datas.add(extractRawAudioData(scanner.nextLine()));
	      }
	    }catch (Exception e) {
			Logger.logMessageOnConsole("Exception in readng" + path +" " + e.getMessage());
		}
	    finally{
	      scanner.close();
	    }
	    
	    return datas;
	}
	
	private AudioData extractRawAudioData(String string) throws Exception{
	    String[] strArray = string.split(",");
	    short[] dataShorts = getShortsFromString(string);
	    AudioData rawAudioData = new AudioData(Long.parseLong(strArray[0]), dataShorts);
	    
	    return rawAudioData;
	}

	private short[] getShortsFromString(String input) {
		String[] strArray = input.split(",");
		short dataShorts[] = new short[strArray.length -1];
	    //skip the 1st, which has time information
	    for(int i = 1; i < strArray.length ; i++) {
	    		dataShorts[i-1] = Short.parseShort(strArray[i].trim());	
	    }
		return dataShorts;
	}
	
	public short [] readRawData(String filePath){
		File testFile = new File(filePath);
		
		short[] datas = new short[0];
	    Scanner scanner = null;

	    try {
	    	if(testFile.exists() && testFile.canRead()){
	    		scanner = new Scanner(new FileInputStream( testFile));
	  	      while (scanner.hasNextLine()){
	  	    	  String line = scanner.nextLine();
	  	    	  //remove the braces []
	  	    	  if(line.length()>2){
	  	    		  short [] shorts = getShortsFromString(line.substring(1,line.length() -1));
	  		    	  datas = shorts; //I expect only single line  
	  	    	  }
	  	      }	
	    	}
	    }catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (Exception e) {
			Logger.logMessageOnConsole("Exception in readng" + filePath +" " + e.getMessage());
		}finally{
	      if(scanner!= null) scanner.close();
	    }
	    
	    return datas;
	}
	
	public static short [] readFileFromPath(String filePath){
		short [] data = new short[0];
		FileReader reader = new FileReader();
		data = reader.readRawData(filePath);
		return data;
		
	}


}
