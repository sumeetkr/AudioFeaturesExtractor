package audioFeaturesExtractor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import libsvm.LibSVM;

import be.abeel.util.Pair;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.evaluation.EvaluateDataset;
import net.sf.javaml.classification.evaluation.PerformanceMeasure;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.sampling.Sampling;
import net.sf.javaml.tools.data.FileHandler;
import audioFeaturesExtractor.util.AudioData;
import audioFeaturesExtractor.util.FileReader;
import audioFeaturesExtractor.util.ShortToByteConverter;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("app is running");

		// String directoryPath = "/users/sumeet/Downloads/MobiSens_Data";
		String directoryPath = "/users/sumeet/Downloads/ASR/Without_Shredding";
		RawAudioToAudibleDataConveter.writeDataToAudibleFormat(directoryPath);
		FeaturesExtractor.extractFeatures(directoryPath, 0, 100);
		AsrClassifier.runClassifier(directoryPath);

	}
}
