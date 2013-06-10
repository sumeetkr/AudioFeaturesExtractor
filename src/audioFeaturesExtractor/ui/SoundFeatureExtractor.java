package audioFeaturesExtractor.ui;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import audioFeaturesExtractor.util.AudioData;
import audioFeaturesExtractor.util.FileReader;

public class SoundFeatureExtractor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("app is running");
		FileReader reader = new FileReader();
		AudioFeaturesExtractor featuresExtractor = new AudioFeaturesExtractor();
		
		try {
			List<AudioData> rawAudioList = reader.readFile("/users/sumeet/Downloads/353918053306904_audio_Head_1009.csv");
			
			System.out.println(rawAudioList.size());
			
			
			Iterator iterator = rawAudioList.iterator();
			while ( iterator.hasNext()) {
				AudioData audioData = (AudioData) iterator.next();
				featuresExtractor.extractFeatures(audioData);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
