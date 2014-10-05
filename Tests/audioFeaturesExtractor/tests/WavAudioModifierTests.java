package audioFeaturesExtractor.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import audioFeaturesExtractor.WavAudioModifier;

public class WavAudioModifierTests {
	
	String path = "/Users/sumeet/Downloads/eval92/audio/440c040a.wav";
	String pathOutDir = "/Users/sumeet/Downloads/eval92/audio_modified";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testReadWavFilesAndSubSample() {
		WavAudioModifier.readWavFilesAndSubSample(path, pathOutDir, 50);
	}

	@Test
	public void testReadWavFileAndRandomize() {
		fail("Not yet implemented");
	}

	@Test
	public void testSubSampleFilesInDirectory() {
		fail("Not yet implemented");
	}

	@Test
	public void testRandomizeFilesInDir() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadWavFile() {
		String path = "/Users/sumeet/Downloads/eval92/audio/440c040a.wav";
		WavAudioModifier.readWavFile(path);
	}

	@Test
	public void testWriteWavFile() {
		WavAudioModifier.readWavFile(pathOutDir+"/"+"newav.wav");
	}

}