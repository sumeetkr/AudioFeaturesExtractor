package audioFeaturesExtractor.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileReader {
    
	public List<AudioData> readFile(String path) throws IOException{
		File testFile = new File(path);
		
		List<AudioData> datas = new ArrayList<AudioData>();
	    Scanner scanner = new Scanner(new FileInputStream( testFile));
	    try {
	      while (scanner.hasNextLine()){
	    	  datas.add(extractRawAudioData(scanner.nextLine()));
	      }
	    }
	    finally{
	      scanner.close();
	    }
	    
	    return datas;
	}
	
	public AudioData extractRawAudioData(String string){
	    
	    String[] strArray = string.split(",");
	    short data16bit[] = new short[strArray.length -1];
	    //skip the 1st, which has time information
	    for(int i = 1; i < strArray.length ; i++) {
	    	data16bit[i-1] = Short.parseShort(strArray[i]);
	    }
	    
	    AudioData rawAudioData = new AudioData(Long.parseLong(strArray[0]), data16bit);
	    
	    return rawAudioData;
	}

}
