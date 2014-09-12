package audioFeaturesExtractor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import audioFeaturesExtractor.util.AudioData;
import audioFeaturesExtractor.util.FileReader;
import audioFeaturesExtractor.util.Logger;
import audioFeaturesExtractor.util.ShortToByteConverter;

public class RawAudioToAudibleDataConveter {
	public static void writeDataToAudibleFormat(String rawFileDirectoryPath,
			String audibleFilesDirPath) {

		File fileDir = new File(rawFileDirectoryPath);
		File[] filesList = fileDir.listFiles();
		FileReader reader = new FileReader();

		for (File file : filesList) {
			try {
				List<AudioData> rawAudioList = reader.readFile(file
						.getAbsolutePath());
				
				File flToWrite = new File(audibleFilesDirPath, "Audible_"
						+ file.getName());
				FileOutputStream stream = new FileOutputStream(flToWrite);
				
				Logger.logMessageOnConsole("Converting to audible format "+ file.getName()+ " of size "+ rawAudioList.size());

				for (AudioData audioData : rawAudioList) {

					if (stream != null && audioData.getRawAudio()!=null) {
						byte[] bytes = ShortToByteConverter.convert(audioData);
						stream.write(bytes);
					}
				}
				
				stream.flush();
				stream.close();
			} catch (IOException e) {
				System.out.println(file.getName());
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println(file.getName());
				e.printStackTrace();
			} finally {

			}
		}
	}
}
