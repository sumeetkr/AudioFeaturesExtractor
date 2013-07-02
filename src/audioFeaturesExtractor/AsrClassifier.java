package audioFeaturesExtractor;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import libsvm.LibSVM;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.evaluation.EvaluateDataset;
import net.sf.javaml.classification.evaluation.PerformanceMeasure;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.sampling.Sampling;
import net.sf.javaml.tools.data.FileHandler;
import be.abeel.util.Pair;

public class AsrClassifier {
	public static void runClassifier(String directoryPath) {

		File fileDir = new File(directoryPath);
		File[] filesList = fileDir.listFiles();

		Dataset dataSetCollection = null;

		for (File file : filesList) {

			if (file.getName().contains("features")) {
				// runClassifier();
				String filePathForDataString = file.getPath();
				try {
					int featureNameIndex = 12;
					if (dataSetCollection == null) {
						dataSetCollection = FileHandler.loadDataset(new File(
								filePathForDataString), featureNameIndex, ",");
					} else {
						dataSetCollection.addAll(FileHandler.loadDataset(
								new File(filePathForDataString),
								featureNameIndex, ","));
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		try {
			Sampling s = Sampling.SubSampling;

			double totaAccuracy = 0.0;
			int count =0;
			for (int i = 0; i < 5; i++) {
				Pair<Dataset, Dataset> datas = s.sample(dataSetCollection,
						(int) (dataSetCollection.size() * 0.8), i);
				Classifier c = new LibSVM();
				c.buildClassifier(datas.x());
				Map<Object, PerformanceMeasure> pms = EvaluateDataset
						.testDataset(c, datas.y());

				System.out.println("DataSet size =" + dataSetCollection.size());
				System.out.println("Training dataset size =" + datas.x().size());
				System.out.println("Test data size =" + datas.y().size());
				
				for (Object o : pms.keySet())
				{
					System.out.println(o + ": " + pms.get(o).getAccuracy());
					totaAccuracy+= pms.get(o).getAccuracy();
					count++;
					
				}
//				System.out.println(pms);
			}
			
			System.out.println("Average accuracy =" + totaAccuracy/count);

		} catch (Exception e) {
			System.out.println("Exception");
			e.printStackTrace();
		}
	}
}
