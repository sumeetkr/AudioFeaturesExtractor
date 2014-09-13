package audioFeaturesExtractor.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import audioFeaturesExtractor.DataAnalyzer;
import audioFeaturesExtractor.util.AFEFileWriter;
import audioFeaturesExtractor.util.FileReader;

public class DataAnalyzerTest {

	private String path1 = "/Users/sumeet/Downloads/IPS_data/1410396768414.pcm";
	private String path2 = "/Users/sumeet/Downloads/IPS_data/1410396771760.pcm";
	private String path3 = "/Users/sumeet/Downloads/IPS_data/1410396764662.pcm";
	private String path4 = "/Users/sumeet/Downloads/IPS_data/1410391391123.pcm";
	private String path5 = "/Users/sumeet/Downloads/IPS_data/1410391387303.pcm";
	private String path6 = "/Users/sumeet/Downloads/IPS_data/1410391383485.pcm";
	private String path7 = "/Users/sumeet/Downloads/IPS_data/1410396778595.pcm";
	private String path8 = "/Users/sumeet/Downloads/IPS_data/1410396781893.pcm";
	private String path9 = "/Users/sumeet/Downloads/IPS_data/1410396785379.pcm";
	private String path10 = "/Users/sumeet/Downloads/IPS_data/1410396789593.pcm";
	private String path11 = "/Users/sumeet/Downloads/IPS_data/1410396792948.pcm";
	
	
	private String path = "/Users/sumeet/Downloads/IPS_data/1410396796230.pcm";
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
		short  [] filteredData = DataAnalyzer.lowPassFilter(data);
		assertTrue(filteredData.length > 0);
		
		AFEFileWriter.writeStringToFile(Arrays.toString(filteredData), path, "_filtered.txt");
	}

	@Test
	public void testDigitilizeData() {
		short [] data = FileReader.readFileFromPath(path);
		short  [] filteredData = DataAnalyzer.lowPassFilter(data);
		short [] signalData = DataAnalyzer.getSignalSegment(filteredData);
		short  [] digitilizedData = DataAnalyzer.digitilizeData(signalData);
		assertTrue(digitilizedData.length > 0);
		
		AFEFileWriter.writeStringToFile(Arrays.toString(digitilizedData), path,"_digitalized.txt" );
	}

	@Test
	public void testGetSignalSegment() {
		short [] data = FileReader.readFileFromPath(path);
		short  [] filteredData = DataAnalyzer.lowPassFilter(data);
		assertTrue(filteredData.length > 0);
		
		short [] signalData = DataAnalyzer.getSignalSegment(filteredData);
		assertTrue(signalData.length > 0);
		AFEFileWriter.writeStringToFile(Arrays.toString(signalData), path,"_signal.txt" );
	}

	@Test
	public void testGetManchesterDecodedValue() {
		short [] data = FileReader.readFileFromPath(path);
		short  [] filteredData = DataAnalyzer.lowPassFilter(data);
		short [] signalData = DataAnalyzer.getSignalSegment(filteredData);
		short  [] digitilizedData = DataAnalyzer.digitilizeData(signalData);
		short  [] codedData = DataAnalyzer.getManchesterEncodedValue(digitilizedData);
		AFEFileWriter.writeStringToFile(Arrays.toString(codedData), path,"_codearray.txt" );
		
		assertTrue(codedData.length > 0);
	}

	@Test
	public void testGetManchesterEncodedString() {
		short [] data = FileReader.readFileFromPath(path);
		short  [] filteredData = DataAnalyzer.lowPassFilter(data);
		short [] signalData = DataAnalyzer.getSignalSegment(filteredData);
		short  [] digitilizedData = DataAnalyzer.digitilizeData(signalData);
		String codedData = DataAnalyzer.getManchesterEncodedString(digitilizedData);
		
		assertTrue(codedData.length() > 0);
		AFEFileWriter.writeStringToFile(codedData, path,"_code.txt" );
	}
	
	@Test
	public void createManchesterEncodedStringsForAllFilesInTheFolder(){
		
		String rawFileDirectoryPath = "/Users/sumeet/Downloads/IPS_data";
		String outPath = "/Users/sumeet/Downloads/IPS_data/codes.txt";
		
		File fileDir = new File(rawFileDirectoryPath);
		File[] filesList = fileDir.listFiles();

		for (File file : filesList) {
			String filePath = file.getAbsolutePath();
			if(filePath.contains(".pcm")){
				try {
					short [] data = FileReader.readFileFromPath(filePath);
					short  [] filteredData = DataAnalyzer.lowPassFilter(data);
					short [] signalData = DataAnalyzer.getSignalSegment(filteredData);
					short  [] digitilizedData = DataAnalyzer.digitilizeData(signalData);
					String codedData = DataAnalyzer.getManchesterEncodedString(digitilizedData);
					
					assertTrue(codedData.length() > 0);
					AFEFileWriter.appendTextToFile(filePath + ":    \n"+codedData, outPath);	
				} catch (Exception e) {
					// TODO: handle exception
					AFEFileWriter.appendTextToFile(filePath + ": Error   ", outPath);
				}
			}
		}
	}
	
	@Test
	public void testBinaryToManchesterEncoding(){
		//101010111011111000110100000100
		String binaryString ="101010111011111000110100000100"; 
		String encodedString = DataAnalyzer.binaryToManchesterEncoding(binaryString);
		
		assertTrue(encodedString.compareTo("011001100110010101100101010101101010010110011010101010011010")==0);
										   //"0000111011011111001001111101101011111011000110001111011001101001"
		                                  //01100110011001010100101010101010101001101010100110100101100110100110011010011010010110100110100110101001010110101010011010010110011010011010011001010101010
		//assertTrue(encodedString.compareTo("01100110011001 0101100101010101101010010110011010101010011010")==0);
        //01100110011001 010100101010101010101001101010100110100101100110100110011010011010010110100110100110101001010110101010011010010110011010011010011001010101010
	}

}
