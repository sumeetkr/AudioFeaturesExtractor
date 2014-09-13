package audioFeaturesExtractor;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;

import weka.core.parser.java_cup.internal_error;
import audioFeaturesExtractor.util.FileReader;

public class DataAnalyzer {
	private static int noiseThresholdAmplitude = 500;
	private static int clockLengthGuess = 25;
	private static int thresholdNoOfZerosToCutSignal = 400;
	
	public static short [] lowPassFilter(short [] data){
		short [] filteredData = data;
		
		for (int i = 0; i < filteredData.length; i++) {
			if(filteredData[i] < noiseThresholdAmplitude && filteredData[i] > -1*noiseThresholdAmplitude){
				filteredData[i]=0;
			}
		}
		return filteredData;
	}
	
	public static short [] digitilizeData(short [] data){
		short [] digitalData = data;
		for (int i = 0; i < digitalData.length; i++) {
			if(digitalData[i] >= noiseThresholdAmplitude){
				digitalData[i] = 1;
			}
			
			if(digitalData[i] <= -1*noiseThresholdAmplitude){
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
			if(startIndex==0 && i>thresholdNoOfZerosToCutSignal && digitalSignalData[i] != 0){
				
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
				for (int j = i+1; j < i+thresholdNoOfZerosToCutSignal; j++) {
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
				
				if(repeatationCount< clockLengthGuess) {
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
	
	public static String binaryToManchesterEncoding(String binaryString){
		String encodedString = "";
		ArrayList<Integer> binaryIntegers =new ArrayList<Integer>();
		for (char chr : binaryString.toCharArray()) {
			Integer integer = Integer.decode(String.valueOf(chr));
			binaryIntegers.add(integer);
		}
		
		for (Integer integer : binaryIntegers) {
			if(integer.intValue() == 0){
				encodedString = encodedString.concat("10");
			}else {
				encodedString = encodedString.concat("01");
			}
		}
		return encodedString;
	}
}
