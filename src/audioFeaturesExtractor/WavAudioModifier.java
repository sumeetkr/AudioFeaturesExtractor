package audioFeaturesExtractor;

import java.io.File;
import LabBook.WavFile;

public class WavAudioModifier {

	private String srcDirPath = "/Users/sumeet/Downloads/eval92/audio";
	
	public static void readWavFilesAndSubSample(String wavFilePath, String outputDir, double subSamplingPercentage){
		try{
//		 	Open the wav file specified as the first argument
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

			int framesRead;
			int framesWritten;
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;

			// Output the minimum and maximum value
			System.out.printf("Min: %f, Max: %f\n", min, max);
			
			
			final int BUF_SIZE = 5001;
			double[] buffer = new double[BUF_SIZE * numChannels];
			do
			{
				framesRead = readWavFile.readFrames(buffer, BUF_SIZE);
				framesWritten = writeWavFile.writeFrames(buffer, BUF_SIZE);
				System.out.printf("%d %d\n", framesRead, framesWritten);
			}
			while (framesRead != 0);
			
		}catch(Exception ex){
			System.err.println(ex);
		}
		
	}
	
	public static void readWavFileAndRandomize(String wavFilePath, String outputDir){
		
	}
	
	public static void subSampleFilesInDirectory(String srcPath, double [] percentages){
		
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
