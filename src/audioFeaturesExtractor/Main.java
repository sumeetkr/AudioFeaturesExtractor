package audioFeaturesExtractor;

import java.io.File;
import audioFeaturesExtractor.util.Logger;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("app is running");
		// Pipeline -> preprocess sound data, (convert to audible if needed),
		// randomize sound, extract features, classification
		final String RAW_FILES_DIR_PATH = "/users/sumeet/Downloads/ASR/Without_Shredding/Raw_Sound";
		final String PROCESSED_FILES_DIR_PATH = "/users/sumeet/Downloads/ASR/Without_Shredding/Processed_Sound";
		final String RANDOMIZED_FILES_DIR_PATH = "/users/sumeet/Downloads/ASR/Without_Shredding/Randomized_Sound";
		final String AUDIBLE_FILES_DIR_PATH = "/users/sumeet/Downloads/ASR/Without_Shredding/Audible_Sound";
		final String SOUND_FEATURES_FILES_DIR_PATH = "/users/sumeet/Downloads/ASR/Without_Shredding/Features";
		// clean and create folders
		String[] foldersToBeCleaned = { PROCESSED_FILES_DIR_PATH,
				RANDOMIZED_FILES_DIR_PATH, AUDIBLE_FILES_DIR_PATH,
				SOUND_FEATURES_FILES_DIR_PATH };
		
		String[] foldersToBeCleaned2 = { RANDOMIZED_FILES_DIR_PATH, AUDIBLE_FILES_DIR_PATH,
				SOUND_FEATURES_FILES_DIR_PATH };
		
		int[] subsamplingPercentages = {100, 20};//{ 10, 25, 40, 55 , 70, 85, 100 };//{ 20, 40 };
													// 100};
		int[] randomizationWidthPercentages ={0, 100};// { 30, 50, 80 };//{50};//

		for (int subSamplingPec : subsamplingPercentages) {
			FileUtil.cleanFolders(foldersToBeCleaned);
			// drop a percentage of sound
			Logger.logResultsOnConsole("Subsampling percentage "
					+ subSamplingPec);
			RawDataSubSampler.subSampleSoundData(RAW_FILES_DIR_PATH,
					PROCESSED_FILES_DIR_PATH, subSamplingPec);
			for (int randomizationPec : randomizationWidthPercentages) {
				FileUtil.cleanFolders(foldersToBeCleaned2);
				Logger.logResultsOnConsole("Randomization percentage "
						+ randomizationPec);
				// create audible format
				AudioFramesRandomizer.randomize(PROCESSED_FILES_DIR_PATH,
						RANDOMIZED_FILES_DIR_PATH, randomizationPec);
				
				RawAudioToAudibleDataConveter.writeDataToAudibleFormat(
						RANDOMIZED_FILES_DIR_PATH, AUDIBLE_FILES_DIR_PATH);
				// size depending upon data collection frequency (8000 hz)
//				FeaturesExtractor.extractFeatures(RANDOMIZED_FILES_DIR_PATH,
//						SOUND_FEATURES_FILES_DIR_PATH, 0, 100, 320);
//				for (AsrClassifier.Classifiers classfier : AsrClassifier.Classifiers.values()) {
//					Logger.logResultsOnConsole("Classifier type "
//							+ classfier.name());
//					AsrClassifier.runClassifier(SOUND_FEATURES_FILES_DIR_PATH,
//							classfier);
//				}
			}
		}
	}

}
