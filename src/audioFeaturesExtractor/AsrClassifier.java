package audioFeaturesExtractor;

import java.io.File;
import java.io.IOException;
import java.security.Timestamp;
import java.sql.Date;
import java.sql.Time;
import java.util.Map;
import java.util.Random;

import weka.core.parser.java_cup.internal_error;
import weka.filters.unsupervised.attribute.TimeSeriesTranslate;

import libsvm.LibSVM;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.KNearestNeighbors;
import net.sf.javaml.classification.evaluation.CrossValidation;
import net.sf.javaml.classification.evaluation.EvaluateDataset;
import net.sf.javaml.classification.evaluation.PerformanceMeasure;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.sampling.Sampling;
import net.sf.javaml.tools.data.FileHandler;
import audioFeaturesExtractor.util.Logger;
import be.abeel.util.Pair;

public class AsrClassifier {
	public enum Classifiers {
		KNearestNeighbors, LibSVM;

	}

	public static void runClassifier(String directoryPath,
			Classifiers classifier) {

		File fileDir = new File(directoryPath);
		File[] filesList = fileDir.listFiles();

		Dataset dataSetCollection = null;

		for (File file : filesList) {
			// runClassifier();
			String filePathForDataString = file.getPath();
			try {
				int featureNameIndex = 12;
				if (dataSetCollection == null) {
					dataSetCollection = FileHandler.loadDataset(file,
							featureNameIndex, ",");
				} else {
					dataSetCollection.addAll(FileHandler.loadDataset(file,
							featureNameIndex, ","));
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("reading features failed of "
						+ file.getName());
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("reading features failed" + file.getName());
				e.printStackTrace();
			}

		}

		try {
			Sampling sampler = Sampling.SubSampling;
			double trainingDataPercentage = 0.8;
			double totaAccuracy = 0;

			Logger.logResultsOnConsole("DataSet size ="
					+ dataSetCollection.size());

			int count = 0;
			int correct = 0;
			int wrong = 0;
//			for (int i = 0; i < 10; i++) {
				Pair<Dataset, Dataset> datas = sampler
						.sample(dataSetCollection,
								(int) (dataSetCollection.size() * trainingDataPercentage),
								5);

				Classifier cfr = null;
				switch (classifier) {
				case LibSVM:
					cfr = new LibSVM();
					break;
				case KNearestNeighbors:
					cfr = new KNearestNeighbors(5);
					break;
				default:
					cfr = new KNearestNeighbors(5);
				}

				cfr.buildClassifier(datas.x());
				try {

					Logger.logResultsOnConsole("Training dataset size ="
							+ datas.x().size());

					Logger.logResultsOnConsole("Test data size ="
							+ datas.y().size());

					for (Instance inst : datas.y()) {
						Object predictedClassValue = cfr.classify(inst);
						Object realClassValue = inst.classValue();
						if (predictedClassValue.equals(realClassValue))
							correct++;
						else
							wrong++;
					}

					Logger.logMessageOnConsole(String.valueOf("Correct "+ correct));
					Logger.logMessageOnConsole(String.valueOf("Wrong " + wrong));
					Logger.logResultsOnConsole(String.valueOf("Accuracy " + 100*correct/(wrong+ correct)));
					// CrossValidation cv = new CrossValidation(cfr);
					// /* Perform cross-validation on the data set */
					// Map<Object, PerformanceMeasure> p =
					// cv.crossValidation(dataSetCollection);
					//
					// for (Object o : p.keySet()) {
					//
					// Logger.logMessageOnConsole(o + ": "
					// + p.get(o).getAccuracy());
					// totaAccuracy += p.get(o).getAccuracy();
					// count++;
					// }

					 Map<Object, PerformanceMeasure> pms = EvaluateDataset
					 .testDataset(cfr, datas.y());

					 for (Object o : pms.keySet()) {
					
					 Logger.logMessageOnConsole(o + ": "
					 + pms.get(o).getAccuracy());
					 totaAccuracy += pms.get(o).getAccuracy();
					 
					 count++;
					 }
//					 System.out.println(pms);
				} catch (Exception e) {
					System.out.println("classification failed");
					e.printStackTrace();
				}
//			}

			// Classifier knn = new KNearestNeighbors(5);
			// CrossValidation cv = new CrossValidation(knn);
			// /* 5-fold CV with fixed random generator */
			// Map<Object, PerformanceMeasure> p = cv.crossValidation(data, 5,
			// new Random(1));
			// Map<Object, PerformanceMeasure> q = cv.crossValidation(data, 5,
			// new Random(1));
			// Map<Object, PerformanceMeasure> r = cv.crossValidation(data, 5,
			// new Random(25));

			Logger.logResultsOnConsole("Average accuracy =" + totaAccuracy
					/ count);
			Logger.logResultsOnConsole("");

		} catch (Exception e) {
			System.out.println("Exception");
			e.printStackTrace();
		}
	}
}
