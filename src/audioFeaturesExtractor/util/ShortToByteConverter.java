package audioFeaturesExtractor.util;


public class ShortToByteConverter {

	public static byte[] convert(AudioData data){
		int bufferSamples = data.getRawAudio().length;
		int readAudioSamples = data.getRawAudio().length;
		short data16bit[] = data.getRawAudio();
		byte data8bit[] = new byte[bufferSamples * 2];
		
		if (readAudioSamples > 0) {
			double fN = (double) readAudioSamples;

			// Convert shorts to 8-bit bytes for raw audio output
			for (int i = 0; i < bufferSamples; i++) {
				data8bit[i * 2] = (byte) data16bit[i];
				data8bit[i * 2 + 1] = (byte) (data16bit[i] >> 8);
			}
		}
		
	return data8bit;
	}

}
