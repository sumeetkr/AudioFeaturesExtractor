package audioFeaturesExtractor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import audioFeaturesExtractor.util.AudioData;
import audioFeaturesExtractor.util.FileReader;
import audioFeaturesExtractor.util.Logger;
import audioFeaturesExtractor.util.RandomNumberGenerator;

public class AudioFramesRandomizer {
	public static void randomize(String fromFolder, String toFolder, int randomizationLevelPercentage) {
		// randomize round
		// Formally define sound shredding as a randomized reordering process:
		// for a sound recording, chop the sound input into a sequence of
		// segments:
		// t1 t2 t3 ... t_i .. t_n
		// where t_i can be of length 1) fixed at value delta, i.e., equally
		// sized sound snippets or 2) random value between [delta1, delta2].
		// We rearrange the snippet index by adding a random value r_i, r_i is
		// in the range of [-w, +w].
		// Now the index for each sound snippets becomes:
		// t_{1+r_1}, t_{2+r_2}..., t_{i+r_i}, ....
		// and then we sort the snippets by the new index and output as the
		// sound stream file.
		//
		// So the parameters are: 1) delta1, 2) delta2, 3) w
		
		File inputDir = new File(fromFolder);
		File[] filesList = inputDir.listFiles();
		FileReader reader = new FileReader();
		File outputDir = new File(toFolder);

		for (File file : filesList) {
			FileWriter writer= null; 
			try {
				List<AudioData> rawAudioList = reader.readFile(file
						.getAbsolutePath());
				File flToWrite = new File(outputDir, "Randomized_"
						+ file.getName());
				writer = new FileWriter(flToWrite);
				
				Logger.logMessageOnConsole("Randomizing "+file.getName()+ " with size "+ rawAudioList.size());
				//randomize
				int size = rawAudioList.size();
				
				for(int i=0; i<rawAudioList.size();i++){
					// compute a fraction of the range, 0 <= frac < range
					int start = i - (randomizationLevelPercentage*i/100);
					int end = i;
				    
					int randomNumber = RandomNumberGenerator.getRandomNumberInRange(start, end);
				    int newIndex = randomNumber;
				    if(newIndex<0 || newIndex>=size) newIndex = i-randomNumber;
				    
				    //System.out.println("OldIndex : "+ i+" NewIndex : " + newIndex);
				    if(newIndex != i){
				    	AudioData data = rawAudioList.remove(i);
					    rawAudioList.add(newIndex, data);	
				    }
				}
				
				for (AudioData audioData : rawAudioList) {
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
				if(writer != null){
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
