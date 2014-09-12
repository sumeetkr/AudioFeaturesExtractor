package audioFeaturesExtractor.tests;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import audioFeaturesExtractor.DataAnalyzer;
import audioFeaturesExtractor.util.FileWriter;

public class DataAnalyzerTest {

	private String path = "/Users/sumeet/Downloads/IPS_data/1410396768414.pcm";
	private short [] data;
	
	@Before
	public void setUp() throws Exception {
		data = DataAnalyzer.readFile(path);
		assertTrue(data.length>0);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testReadFile() {
		short  [] data = DataAnalyzer.readFile(path);
		assertTrue(data.length>0);
	}
	
	@Test
	public void testLowPassFiler() {
		short [] data = DataAnalyzer.readFile(path);
		short  [] filteredData = DataAnalyzer.lowPassFilter(data);
		assertTrue(filteredData.length > 0);
		
		FileWriter.writeStringToFile(Arrays.toString(filteredData), path, "_filtered.pcm");
	}

	@Test
	public void testDigitilizeData() {
		short [] data = DataAnalyzer.readFile(path);
		short  [] filteredData = DataAnalyzer.lowPassFilter(data);
		short [] signalData = DataAnalyzer.getSignalSegment(filteredData);
		short  [] digitilizedData = DataAnalyzer.digitilizeData(signalData);
		assertTrue(digitilizedData.length > 0);
		
		FileWriter.writeStringToFile(Arrays.toString(digitilizedData), path,"_digitalized.pcm" );
	}

	@Test
	public void testGetSignalSegment() {
		short [] data = DataAnalyzer.readFile(path);
		short  [] filteredData = DataAnalyzer.lowPassFilter(data);
		assertTrue(filteredData.length > 0);
		
		short [] signalData = DataAnalyzer.getSignalSegment(filteredData);
		assertTrue(signalData.length > 0);
		FileWriter.writeStringToFile(Arrays.toString(signalData), path,"_signal.pcm" );
	}

	@Test
	public void testGetManchesterDecodedValue() {
		short [] data = DataAnalyzer.readFile(path);
		short  [] filteredData = DataAnalyzer.lowPassFilter(data);
		short [] signalData = DataAnalyzer.getSignalSegment(filteredData);
		short  [] digitilizedData = DataAnalyzer.digitilizeData(signalData);
		short  [] codedData = DataAnalyzer.getManchesterEncodedValue(digitilizedData);
		FileWriter.writeStringToFile(Arrays.toString(codedData), path,"_codearray.txt" );
		
		assertTrue(codedData.length > 0);
	}

	@Test
	public void testGetManchesterEncodedString() {
		short [] data = DataAnalyzer.readFile(path);
		short  [] filteredData = DataAnalyzer.lowPassFilter(data);
		short [] signalData = DataAnalyzer.getSignalSegment(filteredData);
		short  [] digitilizedData = DataAnalyzer.digitilizeData(signalData);
		String codedData = DataAnalyzer.getManchesterEncodedString(digitilizedData);
		
		assertTrue(codedData.length() > 0);
		FileWriter.writeStringToFile(codedData, path,"_code.txt" );
	}

}
