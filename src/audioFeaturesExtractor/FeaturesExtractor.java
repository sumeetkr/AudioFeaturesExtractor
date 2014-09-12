package audioFeaturesExtractor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import weka.core.parser.java_cup.internal_error;

import audioFeaturesExtractor.math.FFT;
import audioFeaturesExtractor.math.Window;
import audioFeaturesExtractor.math.MFCC;
import audioFeaturesExtractor.util.AudioData;
import audioFeaturesExtractor.util.Constants;
import audioFeaturesExtractor.util.FileReader;
import audioFeaturesExtractor.util.Logger;

public class FeaturesExtractor {
	private MFCC featureMFCC;
	private FFT featureFFT;
	private Window featureWin;
	private int bufferSamples;
	private static int[] freqBandIdx = null;
	private int shreddingPercentage = 100;

	public FeaturesExtractor(int shreddingPercentage) {
		this();
		this.shreddingPercentage = shreddingPercentage;
	}

	public FeaturesExtractor() {
		featureMFCC = new MFCC(Constants.FFT_SIZE, Constants.MFCCS_VALUE,
				Constants.MEL_BANDS, Constants.RECORDER_SAMPLERATE);

		featureFFT = new FFT(Constants.FFT_SIZE);
		featureWin = new Window(bufferSamples);

		freqBandIdx = new int[Constants.FREQ_BANDEDGES.length];
		for (int i = 0; i < Constants.FREQ_BANDEDGES.length; i++) {
			freqBandIdx[i] = Math
					.round((float) Constants.FREQ_BANDEDGES[i]
							* ((float) Constants.FFT_SIZE / (float) Constants.RECORDER_SAMPLERATE));
		}
	}

	public void extractFeatures(AudioData data, int lengthPct) {
		
		short[] audio = data.getRawAudio(lengthPct);
		
		bufferSamples = audio.length;
		featureWin = new Window(bufferSamples);
		int readAudioSamplesLength = audio.length;
		short data16bit[] =audio;
		byte data8bit[] = new byte[bufferSamples * 2];
		double fftBufferR[] = new double[Constants.FFT_SIZE];
		double fftBufferI[] = new double[Constants.FFT_SIZE];
		double featureCepstrum[] = new double[Constants.MFCCS_VALUE];

		if (readAudioSamplesLength > 0) {
			double fN = (double) readAudioSamplesLength;

			// Convert shorts to 8-bit bytes for raw audio output
			for (int i = 0; i < bufferSamples; i++) {
				data8bit[i * 2] = (byte) data16bit[i];
				data8bit[i * 2 + 1] = (byte) (data16bit[i] >> 8);
			}
			// writeLogTextLine("Read " + readAudioSamples + " samples");

			// L1-norm
			double accum = 0;
			for (int i = 0; i < readAudioSamplesLength; i++) {
				accum += Math.abs((double) data16bit[i]);
			}
			data.setL1_NORM(accum / fN);

			// L2-norm
			accum = 0;
			for (int i = 0; i < readAudioSamplesLength; i++) {
				accum += (double) data16bit[i] * (double) data16bit[i];
			}
			data.setL2_NORM(Math.sqrt(accum / fN));

			// Linf-norm
			accum = 0;
			for (int i = 0; i < readAudioSamplesLength; i++) {
				accum = Math.max(Math.abs((double) data16bit[i]), accum);
			}
			data.setLinf_NORM(Math.sqrt(accum));

			// Frequency analysis
			Arrays.fill(fftBufferR, 0);
			Arrays.fill(fftBufferI, 0);

			// Convert audio buffer to doubles
			for (int i = 0; i < readAudioSamplesLength; i++) {
				fftBufferR[i] = data16bit[i];
			}

			// In-place windowing
			featureWin.applyWindow(fftBufferR);

			// In-place FFT
			featureFFT.fft(fftBufferR, fftBufferI);

			// Get PSD across frequency band ranges
			double[] psdAcrossFrequencyBands = new double[Constants.FREQ_BANDEDGES.length - 1];
			for (int b = 0; b < (Constants.FREQ_BANDEDGES.length - 1); b++) {
				int j = freqBandIdx[b];
				int k = freqBandIdx[b + 1];
				accum = 0;
				for (int h = j; h < k; h++) {
					accum += fftBufferR[h] * fftBufferR[h] + fftBufferI[h]
							* fftBufferI[h];
				}
				psdAcrossFrequencyBands[b] = accum / ((double) (k - j));
			}

			data.setPsdAcrossFrequencyBands(psdAcrossFrequencyBands);

			// Get MFCCs
			featureCepstrum = featureMFCC.cepstrum(fftBufferR, fftBufferI);
			data.setFeatureCepstrum(featureCepstrum);

		}
	}

	public static void extractFeatures(String directoryPath,
			String featuresDirectoryPath,
			int shreddingPercentage, 
			int sampleLengthPct, 
			int audioFrameLength) {

		File fileDir = new File(directoryPath);
		File[] filesList = fileDir.listFiles();
		FileReader reader = new FileReader();

		for (File file : filesList) {
				// writeDataToAudibleFormat(reader, fileDir, file);
				try {
					
					List<AudioData> rawAudioList = reader.readFile(file
							.getAbsolutePath(), audioFrameLength);
					
					extractAndWriteFeatures(fileDir, file, featuresDirectoryPath, rawAudioList,
							shreddingPercentage, sampleLengthPct);

				} catch (IOException e) {
					e.printStackTrace();
					System.out.println(file.getAbsolutePath());
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(file.getAbsolutePath());
				}
			}
	}

	private static void extractAndWriteFeatures(File fileDir, File file,
			String featuresDirectoryPath,
			List<AudioData> rawAudioList,
			int shreddingPercentage,
			int sampleLength)
			throws IOException {
		FeaturesExtractor featuresExtractor = new FeaturesExtractor();
		// write the features in a file
		File flToWrite = new File(featuresDirectoryPath, "Features_" + file.getName());
		FileWriter writer = new FileWriter(flToWrite);

		Logger.logMessageOnConsole("Extracting features from "+ file.getName() + " with size " +rawAudioList.size());
		//int shreddingParam = 100 / (100 - shreddingPercentage);

		for (int i = 0; i < rawAudioList.size(); i++) {
			//if (i % shreddingParam == 0) {
				AudioData audioData = rawAudioList.get(i);
				featuresExtractor.extractFeatures(audioData,sampleLength);

				if (writer != null) {
					double [] cepstrum = audioData.getFeatureCepstrum();
					if(!Double.isInfinite(cepstrum[0]) && !Double.isNaN(cepstrum[0])){
						String str = Arrays
								.toString(cepstrum);
						// audioData.getTime()+","+
						String labelString = file.getName().split("_")[file.getName().split("_").length -1];
						writer.write(str.substring(1, str.length() - 1) 
								+ ","
								+ labelString);
						writer.append('\n');
						writer.flush();	
					}
				}
			//}
		}

		writer.close();
	}

}
