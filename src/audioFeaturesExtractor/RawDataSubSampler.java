package audioFeaturesExtractor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import weka.core.parser.java_cup.internal_error;

import audioFeaturesExtractor.util.AudioData;
import audioFeaturesExtractor.util.FileReader;
import audioFeaturesExtractor.util.Logger;
import audioFeaturesExtractor.util.RandomNumberGenerator;
import audioFeaturesExtractor.util.Settings;

public class RawDataSubSampler {

	// Takes data in Mobisens (timestamp, data .....) output format and converts
	// to (data...)
	public static void subSampleSoundData(String inputFolder,
			String outputFolder, int subSamplingPercentage) {

		File inputDir = new File(inputFolder);
		File[] filesList = inputDir.listFiles();
		FileReader reader = new FileReader();
		File outputDir = new File(outputFolder);

		for (File file : filesList) {
			FileWriter writer = null;
			try {
				
				List<AudioData> rawAudioList = reader.readFile(file
						.getAbsolutePath());
				File flToWrite = new File(outputDir, "Preprocessed_"
						+ file.getName());
				writer = new FileWriter(flToWrite);
				
				Logger.logMessageOnConsole("Preprossesing " + file.getName()
						+ " with size " + rawAudioList.size());
				
//				Random rand = new Random(System.currentTimeMillis());
//				int subSampleSize = (int)(rawAudioList.size()*subSamplingPercentage/100);
				List<AudioData> subSample = new ArrayList<AudioData>();
//				for (int i = 0; i < subSampleSize; i++) {
//					subSample.add(rawAudioList.remove(rand.nextInt(rawAudioList.size())));
//				}
//				
				for (int i = 0; i < rawAudioList.size(); i++) {
					if(i%10 <= subSamplingPercentage/10){
						subSample.add(rawAudioList.get(i));
					}
				}
				
				for (AudioData audioData : subSample) {
						short[] data = audioData.getRawAudio();
						String str = Arrays.toString(data);
						writer.write(str.substring(1, str.length() - 1) + "\n");
				}
				writer.flush();
			} catch (IOException e) {
				System.out.println(file.getName());
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println(file.getName());
				e.printStackTrace();
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

}
