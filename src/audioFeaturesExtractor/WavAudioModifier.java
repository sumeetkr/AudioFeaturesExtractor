package audioFeaturesExtractor;

import java.io.File;

import weka.core.parser.java_cup.internal_error;
import LabBook.WavFile;

public class WavAudioModifier {

	private static String srcDirPath = "/Users/sumeet/Downloads/eval92/audio";
	private static String subSampledDirPath = "/Users/sumeet/Downloads/eval92/subsampled_audio";
	private static String shreddedDirPath = "/Users/sumeet/Downloads/eval92/shredded_audio";
	private static final int BUF_SIZE = 1024;
	
	public static void readWavFilesAndSubSample(String wavFilePath, String outputDir, double subSamplingPercentage){
		try{
			// Open the wav file specified as the first argument
			File inputFile = new File(wavFilePath);
			WavFile readWavFile = WavFile.openWavFile(inputFile);
			
			// Create a wav file with the name specified as the first argument
			String newFilePathString = outputDir +"/"+ inputFile.getName();
			WavFile writeWavFile = WavFile.newWavFile(new File(newFilePathString), 
					readWavFile.getNumChannels(),
					readWavFile.getNumFrames(), 
				    readWavFile.getValidBits(), 
					readWavFile.getSampleRate());

			// Display information about the wav file
			readWavFile.display();

			// Get the number of audio channels in the wav file
			int numChannels = readWavFile.getNumChannels();

			int frameNo=0;
			int frameNoWritten=0;
			int framesRead;
			int framesWritten;
		
			double[] buffer = new double[BUF_SIZE * numChannels];
			do
			{
				framesRead = readWavFile.readFrames(buffer, BUF_SIZE);
				System.out.printf("Frames read %d\n", framesRead);
				
				//if you want to sub-sample, you just drops some frames
				//which frame to drop depends on subsampling percentage
				//e.g. we keep 1, 2 frames out of 1,2, ... 10, if subsampling percentage is 20 %
				
				if(frameNo%10 < subSamplingPercentage/10){
					framesWritten = writeWavFile.writeFrames(buffer, BUF_SIZE);
					System.out.printf("Frames written %d \n", framesWritten);
					frameNoWritten++;
				}
				
				frameNo++;
			}
			while (framesRead != 0);
			
			System.out.printf("Frames read: %d, Frames written: %d\n", frameNo, frameNoWritten);
			
		}catch(Exception ex){
			System.err.println(ex);
		}
		
	}
	
	public static void readWavFileAndRandomize(String wavFilePath, String outputDir){
		
	}
	
	public static void subSampleFilesInDirectory(String srcDirPath, double [] percentages){
		//clear subSampledDirPath
		FileUtil.cleanDir(subSampledDirPath);
		//create dir for sub-sampled files - if does not exist
		for (int i = 0; i < percentages.length; i++) {
			FileUtil.createDir(subSampledDirPath +"/" +percentages[i]);
		}
		
		//get all files in src path dir
		File inputDir = new File(srcDirPath);
		File[] filesList = inputDir.listFiles();
		for (File file : filesList) {
			for (int i = 0; i < percentages.length; i++) {
				readWavFilesAndSubSample(file.getPath(), subSampledDirPath +"/" +percentages[i], percentages[i]);
			}
		}

	}
	
	public static void randomizeFilesInDir(String srcPath){
		
	}
	
	public static void readWavFile(String path){
					
		try{
//		 	Open the wav file specified as the first argument
			WavFile wavFile = WavFile.openWavFile(new File(path));

			// Display information about the wav file
			wavFile.display();

			// Get the number of audio channels in the wav file
			int numChannels = wavFile.getNumChannels();

			// Create a buffer of 100 frames
			double[] buffer = new double[100 * numChannels];

			int framesRead;
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;

			do
			{
				// Read frames into buffer
				framesRead = wavFile.readFrames(buffer, 100);

				// Loop through frames and look for minimum and maximum value
				for (int s=0 ; s<framesRead * numChannels ; s++)
				{
					if (buffer[s] > max) max = buffer[s];
					if (buffer[s] < min) min = buffer[s];
				}
			}
			while (framesRead != 0);

			// Close the wavFile
			wavFile.close();

			// Output the minimum and maximum value
			System.out.printf("Min: %f, Max: %f\n", min, max);
			
		}catch(Exception ex){
			
		}
	}
	
	public static void writeWavFile(String path){
		try
		{
			int sampleRate = 44100;		// Samples per second
			double duration = 5.0;		// Seconds

			// Calculate the number of frames required for specified duration
			long numFrames = (long)(duration * sampleRate);

			// Create a wav file with the name specified as the first argument
			WavFile wavFile = WavFile.newWavFile(new File(path), 2, numFrames, 16, sampleRate);

			// Create a buffer of 100 frames
			double[][] buffer = new double[2][100];

			// Initialise a local frame counter
			long frameCounter = 0;

			// Loop until all frames written
			while (frameCounter < numFrames)
			{
				// Determine how many frames to write, up to a maximum of the buffer size
				long remaining = wavFile.getFramesRemaining();
				int toWrite = (remaining > 100) ? 100 : (int) remaining;

				// Fill the buffer, one tone per channel
				for (int s=0 ; s<toWrite ; s++, frameCounter++)
				{
					buffer[0][s] = Math.sin(2.0 * Math.PI * 400 * frameCounter / sampleRate);
					buffer[1][s] = Math.sin(2.0 * Math.PI * 500 * frameCounter / sampleRate);
				}

				// Write the buffer
				wavFile.writeFrames(buffer, toWrite);
			}

			// Close the wavFile
			wavFile.close();
		}
		catch (Exception e)
		{
			System.err.println(e);
		}
	}
}
