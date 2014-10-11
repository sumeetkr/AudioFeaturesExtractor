package audioFeaturesExtractor.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import audioFeaturesExtractor.WavAudioModifier;

public class WavAudioModifierTests {
	
	String srcDirPath = "/Users/sumeet/Downloads/eval92/audio";
	String path = "/Users/sumeet/Downloads/eval92/audio/440c040a.wav";
	String pathOutDir = "/Users/sumeet/Downloads/eval92/test_audio";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

//	@Test
	public void testReadWavFilesAndSubSample() {
		WavAudioModifier.readWavFilesAndSubSample(path, pathOutDir, 20);
	}

//	@Test
//	public void testReadWavFileAndRandomize() {
//		fail("Not yet implemented");
//	}

//	@Test
	public void testSubSampleFilesInDirectory() {
		
		double[] subsamplingPercentages = { 10, 20, 30, 40, 50 ,60, 70, 80, 90, 100 };
		WavAudioModifier.subSampleFilesInDirectory(srcDirPath, subsamplingPercentages);
	}

//	@Test
	public void testRandomizeFilesInDir() {
		double[] subsamplingPercentages = { 10, 20, 30, 40, 50 ,60, 70, 80, 90, 100 };
		WavAudioModifier.randomizeFilesInDir(srcDirPath, subsamplingPercentages, 100);
	}
	
	@Test
	public void testRandomizeFilesInPieces() {
		double[] noOfPieces = {  2, 4, 8, 16, 32 };
		WavAudioModifier.randomizeFilesInPieces(path, noOfPieces);
	}
}
