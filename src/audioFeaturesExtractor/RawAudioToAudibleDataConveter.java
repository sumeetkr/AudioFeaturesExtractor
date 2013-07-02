package audioFeaturesExtractor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import audioFeaturesExtractor.util.AudioData;
import audioFeaturesExtractor.util.FileReader;
import audioFeaturesExtractor.util.ShortToByteConverter;

public class RawAudioToAudibleDataConveter {
	public static void writeDataToAudibleFormat(String directoryPath) {

		File fileDir = new File(directoryPath);
		File[] filesList = fileDir.listFiles();
		FileReader reader = new FileReader();

		for (File file : filesList) {
			if (!file.getName().contains("features")
					&& !file.getName().contains("audible")) {

				try {
					List<AudioData> rawAudioList = reader.readFile(file
							.getAbsolutePath());
					// write the features in a file
					File flToWrite = new File(fileDir, "audible"
							+ file.getName());
					FileOutputStream stream = new FileOutputStream(flToWrite);
					System.out.println(rawAudioList.size());

					for (AudioData audioData : rawAudioList) {

						if (stream != null) {
							byte[] bytes = ShortToByteConverter
									.convert(audioData);
							stream.write(bytes);
						}
					}
					stream.flush();
					stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {

				}
			}
		}

	}


}
