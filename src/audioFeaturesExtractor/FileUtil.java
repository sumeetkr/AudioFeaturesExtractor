package audioFeaturesExtractor;

import java.io.File;

public class FileUtil {
	public static void cleanFolders(String[] paths) {
		for (String path : paths) {
			cleanDir(path);
		}
	}

	public static void cleanDir(String path) {
		try {
			File inputDir = new File(path);
			File[] filesList = inputDir.listFiles();
			for (File file : filesList) {
				file.delete();
			}
//			inputDir.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void createDir(String path){
		File dirpath = new File(path);

		  // if the directory does not exist, create it
		  if (!dirpath.exists()) {
		    System.out.println("creating directory: " + dirpath);
		    boolean result = false;

		    try{
		        dirpath.mkdir();
		        result = true;
		     } catch(SecurityException se){
		        //handle it
		     }        
		     if(result) {    
		       System.out.println("DIR created");  
		     }
		  }
	}
}
