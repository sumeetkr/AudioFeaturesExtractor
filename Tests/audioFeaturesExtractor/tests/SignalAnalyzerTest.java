package audioFeaturesExtractor.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import audioFeaturesExtractor.SignalAnalyzer;
import audioFeaturesExtractor.util.AFEFileWriter;
import audioFeaturesExtractor.util.FileReader;

public class SignalAnalyzerTest {	

	//private String path = "/Users/sumeet/Downloads/IPS_data/1410594731433.pcm";
	private String path ="/Users/sumeet/Documents/workspace/AudioFeaturesExtractor/Tests/TestData/1410835526998.pcm";
	private String outPath = "/Users/sumeet/Downloads/IPS_data/codes.txt";
	//String rawFileDirectoryPath = "/Users/sumeet/Documents/workspace/AudioFeaturesExtractor/Tests/TestData";
	String rawFileDirectoryPath = "/Users/sumeet/Downloads/IPS_data/";
	private short [] data;

	@Before
	public void setUp() throws Exception {
		data = FileReader.readFileFromPath(path);
		assertTrue(data.length>0);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testReadFile() {
		short  [] data = FileReader.readFileFromPath(path);
		assertTrue(data.length>0);
	}

	@Test
	public void testLowPassFiler() {
		short [] data = FileReader.readFileFromPath(path);
		short  [] filteredData = SignalAnalyzer.lowPassFilter(data);
		assertTrue(filteredData.length > 0);

		AFEFileWriter.writeStringToFile(Arrays.toString(filteredData), path, "_filtered.txt");
	}

	@Test
	public void testDigitilizeData() {
		short [] data = FileReader.readFileFromPath(path);
		short  [] filteredData = SignalAnalyzer.lowPassFilter(data);
		short [] dataCopy = FileReader.readFileFromPath(path);
		short [] signalData = SignalAnalyzer.getSignalSegment(filteredData, dataCopy);
		short  [] digitilizedData = SignalAnalyzer.digitilizeData(signalData);
		assertTrue(digitilizedData.length > 0);

		AFEFileWriter.writeStringToFile(Arrays.toString(digitilizedData), path,"_digitalized.txt" );
	}

	@Test
	public void testGetSignalSegment() {
		short [] data = FileReader.readFileFromPath(path);
		short  [] filteredData = SignalAnalyzer.lowPassFilter(data);
		assertTrue(filteredData.length > 0);

		short [] dataCopy = FileReader.readFileFromPath(path);
		short [] signalData = SignalAnalyzer.getSignalSegment(filteredData, dataCopy);
		assertTrue(signalData.length > 0);
		AFEFileWriter.writeStringToFile(Arrays.toString(signalData), path,"_signal.txt" );
	}

	@Test
	public void testGetManchesterDecodedValue() {
		short [] data = FileReader.readFileFromPath(path);
		short  [] filteredData = SignalAnalyzer.lowPassFilter(data);
		short [] dataCopy = FileReader.readFileFromPath(path);
		short [] signalData = SignalAnalyzer.getSignalSegment(filteredData, dataCopy);
		short  [] digitilizedData = SignalAnalyzer.digitilizeData(signalData);
		short  [] codedData = SignalAnalyzer.getManchesterEncodedValue(digitilizedData);
		AFEFileWriter.writeStringToFile(Arrays.toString(codedData), path,"_codearray.txt" );

		assertTrue(codedData.length > 0);
	}

	@Test
	public void testGetManchesterEncodedString() {
		short [] data = FileReader.readFileFromPath(path);
		short  [] filteredData = SignalAnalyzer.lowPassFilter(data);
		short [] dataCopy = FileReader.readFileFromPath(path);
		short [] signalData = SignalAnalyzer.getSignalSegment(filteredData, dataCopy);
		short  [] digitilizedData = SignalAnalyzer.digitilizeData(signalData);
		String codedData = SignalAnalyzer.getManchesterEncodedString(digitilizedData);

		assertTrue(codedData.length() > 0);
		AFEFileWriter.writeStringToFile(codedData, path,"_code.txt" );
	}
	
	@Test
	public void testGetManchesterEncodedStringUsingPhaseLock(){
		short [] data = FileReader.readFileFromPath(path);
		short  [] filteredData = SignalAnalyzer.lowPassFilter(data);
		short [] dataCopy = FileReader.readFileFromPath(path);
		short [] signalData = SignalAnalyzer.getSignalSegment(filteredData, dataCopy);
//		short  [] digitilizedData = SignalAnalyzer.digitilizeData(signalData);
		String codedData = SignalAnalyzer.getManchesterEncodedStringUsingPhaseLock(signalData);

		assertTrue(codedData.length() > 0);
		AFEFileWriter.writeStringToFile(codedData, path,"_code_using_phase_lock.txt" );
	}

	@Test
	public void createManchesterEncodedStringsForAllFilesInTheFolder(){


		File fileDir = new File(rawFileDirectoryPath);
		File[] filesList = fileDir.listFiles();

		for (File file : filesList) {
			String filePath = file.getAbsolutePath();
			if(filePath.contains(".pcm")){
				try {
					short [] data = FileReader.readFileFromPath(filePath);
					short [] dataCopy = FileReader.readFileFromPath(filePath);
					short  [] filteredData = SignalAnalyzer.lowPassFilter(data);
					short [] signalData = SignalAnalyzer.getSignalSegment(filteredData, dataCopy);
					AFEFileWriter.writeStringToFile(Arrays.toString(filteredData), filePath, "_filtered.txt");
					AFEFileWriter.writeStringToFile(Arrays.toString(signalData), filePath,"_signal.txt" );
					
					
					String codedDataUsingPhaseLock = SignalAnalyzer.getManchesterEncodedStringUsingPhaseLock(signalData);
					AFEFileWriter.appendTextToFile(codedDataUsingPhaseLock, outPath);
					
					short  [] digitilizedData = SignalAnalyzer.digitilizeData(signalData);
					AFEFileWriter.writeStringToFile(Arrays.toString(digitilizedData), filePath,"_digitalized.txt" );
					String codedData = SignalAnalyzer.getManchesterEncodedString(digitilizedData);
					AFEFileWriter.appendTextToFile(filePath + ":    \n"+codedData, outPath);
					
					AFEFileWriter.writeStringToFile(codedData, filePath,"_code.txt" );
					AFEFileWriter.appendTextToFile(codedData, outPath);
					
					String decodedValue = SignalAnalyzer.manchesterToBinaryDecoding(codedData);
					AFEFileWriter.appendTextToFile(decodedValue, outPath);
//					
					String decodedValue2 = SignalAnalyzer.manchesterToBinaryDecoding(codedDataUsingPhaseLock);
					AFEFileWriter.appendTextToFile(decodedValue2, outPath);
					
					String beacodId = SignalAnalyzer.getBeconIdFromDecodedString(decodedValue);
					AFEFileWriter.appendTextToFile(beacodId, outPath);
					
					String beacodId2 = SignalAnalyzer.getBeconIdFromDecodedString(decodedValue2);
					AFEFileWriter.appendTextToFile(beacodId2, outPath);
					

					//					assertTrue(codedData.length() > 0);
					
					
					
//					AFEFileWriter.appendTextToFile(codedData2, filePath,"_code.txt" );
					
					
					
				} catch (Exception e) {
					// TODO: handle exception
					AFEFileWriter.appendTextToFile(filePath + ": Error   " + e.getMessage(), outPath);
				}
			}
		}
	}

	@Test
	public void testBinaryToManchesterEncoding(){
		//110001111110111101100101101011011001101101110001111011001011011 -> 7203422764646594139
		String binaryString ="101010111011111000110100000100"; 
		String encodedString = SignalAnalyzer.binaryToManchesterEncoding(binaryString);

		assertTrue(encodedString.compareTo("011001100110010101100101010101101010010110011010101010011010")==0);
		//01100110011001011010010101101010101010011010101001101001011001101001100110100110100101101001101001101010010101101010100110100101100110100110100110010101010110
	}

	
	@Test
	public void testManchesterToBinaryDecoding(){
		String manchesterEncodedString = "01100110011001011010010101101010101010011010101001101001011001101001100110100110100101101001101001101010010101101010100110100101100110100110100110010101010110";
		String decodedString = "";
		try {
			decodedString = SignalAnalyzer.manchesterToBinaryDecoding(manchesterEncodedString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(decodedString.compareTo("0101010011000111111011110110010110101101100110110111000111101100101101101000001")==0);
	}

	@Test
	public void testBinaryToDecimal(){
		//		String binaryString = "0101010011000111111011110110010110101101100110110111000111101100101101101000001";
		String binaryString =         "110001111110111101100101101011011001101101110001111011001011011";
		String decimalString = String.valueOf(SignalAnalyzer.getLongFromBinary(binaryString));
		assertTrue(decimalString.compareTo("7203422764646594139")==0); 
		//7203422764646594139 - wanted
	}

	@Test
	public void testDecodedStringToIRBeconId(){
		String decodedString = "0101010011000111111011110110010110101101100110110111000111101100101101101000001";
		String decimalString = String.valueOf(SignalAnalyzer.getBeconIdFromDecodedString(decodedString));
		assertTrue(decimalString.compareTo("7203422764646594139")==0); 
	}

	@Test
	public void testDoubleFromBinaryString(){
		String binaryString =         "110001111110111101100101101011011001101101110001111011001011011";
		double val = SignalAnalyzer.doubleFromBinaryString((binaryString));
		assertTrue(val == 7.2034227646465946E18); 
	}

	@Test
	public void testGetBeaconIdFromRawSignal(){
		short [] data = FileReader.readFileFromPath("/Users/sumeet/Downloads/IPS_data/1410594731433.pcm");
		String beacodId="";
		try {
			beacodId = SignalAnalyzer.getBeaconIdFromRawSignal(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AFEFileWriter.appendTextToFile(path + ": Error   " + e.getMessage(), outPath);
		}
		assertTrue(beacodId.compareTo("7203422764646594139")==0); 
	}

}
