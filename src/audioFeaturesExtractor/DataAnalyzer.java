package audioFeaturesExtractor;

import java.util.Arrays;
import java.util.Queue;

import org.omg.IOP.Codec;

import weka.core.parser.java_cup.internal_error;
import audioFeaturesExtractor.util.FileReader;

public class DataAnalyzer {
	private String path = "/Users/sumeet/Downloads/IPS_data/1410396768414.pcm";
	private static int limit = 500;
	public DataAnalyzer(){
	   
	}
	
	public static short [] lowPassFilter(short [] data){
		short [] filteredData = data;
		
		for (int i = 0; i < filteredData.length; i++) {
			if(filteredData[i] < limit && filteredData[i] > -1*limit){
				filteredData[i]=0;
			}
		}
		return filteredData;
	}
	
	public static short [] digitilizeData(short [] data){
		short [] digitalData = data;
		for (int i = 0; i < digitalData.length; i++) {
			if(digitalData[i] >= limit){
				digitalData[i] = 1;
			}
			
			if(digitalData[i] <= -1*limit){
				digitalData[i] = 0;
			}
		}
		
		return digitalData;
	}
	
	public static short [] getSignalSegment(short [] data){
		short [] digitalSignalData = data;
		
//		String dataString = Arrays.toString(data);
//		String [] datas = dataString.split("0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, [1-9]");
		
		int startIndex = 0;
		int endIndex = 0;
		
		for (int i = 0; i < digitalSignalData.length; i++) {
			if(startIndex==0 && i>10 && digitalSignalData[i] != 0){
				
				//if all j are == 0 before i, then i is the starting index
				boolean allJsAreZero = true;
				for (int j = i-10; j < i; j++) {
					if(allJsAreZero && digitalSignalData[j] != 0){
						allJsAreZero = false;
					}
				}
				if(allJsAreZero) startIndex = i;
			}
		}
		
		for (int i = startIndex; i < digitalSignalData.length; i++) {
			if(endIndex ==0 && digitalSignalData[i] == 0){
				
				//if all j are == 0 before i, then i is the starting index
				boolean allJsAreZero = true;
				for (int j = i+1; j < i+10; j++) {
					if(allJsAreZero && digitalSignalData[j] != 0 && j<digitalSignalData.length){
						allJsAreZero = false;
					}
				}
				if(allJsAreZero) endIndex = i;
			}
		}
		
		short [] signalSegment = Arrays.copyOfRange(data, startIndex, endIndex);
		return signalSegment;
		
	}
	
	public static short [] getManchesterEncodedValue(short [] data){
		short [] digitalSignalData = data;
		String codedValue = getManchesterEncodedString(digitalSignalData);
		
		short [] code = new short[codedValue.length()];
		char [] chars = codedValue.toCharArray();
		for (int i = 0; i < codedValue.length(); i++) {
			code[i] = Short.valueOf(String.valueOf(chars[i]));
		}
		
		return code;
	}

	public static String getManchesterEncodedString(short[] digitalSignalData) {
		String codedValue= "";
		
		short lastValue= digitalSignalData[0];
		int repeatationCount = 0;
		for (int i = 0; i < digitalSignalData.length; i++) {
			if(lastValue == digitalSignalData[i]){
				repeatationCount++;
			}else{
				
				if(repeatationCount< 18) {
					codedValue = codedValue +String.valueOf(lastValue);
				}else {
					codedValue = codedValue +String.valueOf(lastValue)+String.valueOf(lastValue);
				}
				repeatationCount=0;
				lastValue = digitalSignalData[i];
			}
		}
		return codedValue;
	}
	
	public static short [] readFile(String filePath){
		short [] data = new short[0];
		FileReader reader = new FileReader();
		data = reader.readRawData(filePath);
		return data;
		
	}
}
