package audioFeaturesExtractor;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import audioFeaturesExtractor.util.AudioData;
import audioFeaturesExtractor.util.RandomNumberGenerator;
import weka.core.parser.java_cup.internal_error;
import LabBook.WavFile;
import LabBook.WavFileException;

public class WavAudioModifier {

	private static String srcDirPath = "/Users/sumeet/Downloads/eval92/audio";
	private static String subSampledDirPath = "/Users/sumeet/Downloads/eval92/subsampled_audio";
	private static String shreddedDirPath = "/Users/sumeet/Downloads/eval92/shredded_audio/shredded_in_pieces";
	private static String shredded_in_pieces = "/Users/sumeet/Downloads/eval92/";
	private static final int BUF_SIZE = 256;
	
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
	
	public static void readWavFileAndRandomize(String wavFilePath, 
						String outputDir, 
						double subSamplingPercentage,
						int randomizationLevelPercentage,
						int frameSize){
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
		
//			AbstractMap.SimpleEntry<Integer, double []> map;
			
			ArrayList<double []> bufferList = new ArrayList<double[]>(); 
			do
			{
				double[] buffer = new double[frameSize * numChannels];
				framesRead = readWavFile.readFrames(buffer, frameSize);
				//System.out.printf("Frames read %d\n", framesRead);
				
				//if you want to sub-sample, you just drops some frames
				//which frame to drop depends on subsampling percentage
				//e.g. we keep 1, 2 frames out of 1,2, ... 10, if subsampling percentage is 20 %
				
				if(framesRead > 1 && frameNo%10 < subSamplingPercentage/10){
					bufferList.add(buffer);
					frameNo++;
				}
			}
			while (framesRead != 0);
			
			
			//Now Randomization
			// Formally define sound shredding as a randomized reordering process:
			// for a sound recording, chop the sound input into a sequence of
			// segments:
			// t1 t2 t3 ... t_i .. t_n
			// where t_i can be of length 1) fixed at value delta, i.e., equally
			// sized sound snippets or 2) random value between [delta1, delta2].
			// We rearrange the snippet index by adding a random value r_i, r_i is
			// in the range of [-w, +w].
			// Now the index for each sound snippets becomes:
			// t_{1+r_1}, t_{2+r_2}..., t_{i+r_i}, ....
			// and then we sort the snippets by the new index and output as the
			// sound stream file.
			//
			// So the parameters are: 1) delta1, 2) delta2, 3) w
			int size = bufferList.size();
			
			for(int i=1; i<size;i++){
				// compute a fraction of the range, 0 <= frac < range
				//int start = i - (randomizationLevelPercentage*i/100);
				int start = 0;
				int end = i;
			    
				int randomNumber = RandomNumberGenerator.getRandomNumberInRange(start, end);
			    int newIndex = randomNumber;
			    if(newIndex<0 || newIndex>=size) newIndex = i-randomNumber;
			    
			    if(newIndex != i){
			    	double [] data = bufferList.remove(i);
			    	//if(newIndex == 0 ) newIndex =1;
			    	bufferList.add(newIndex, data);
			    }
		    	System.out.println("OldIndex : "+ i+" NewIndex : " + newIndex);
			}
			
			//Collections.shuffle(bufferList);
			for (int i = 0; i < bufferList.size(); i++) {
				framesWritten = writeWavFile.writeFrames(bufferList.get(i), frameSize);
				//System.out.printf("Frames written %d \n", framesWritten);
				frameNoWritten ++;
			}
			
			System.out.printf("Frames read: %d, Frames written: %d\n", frameNo, frameNoWritten);
			
		}catch(Exception ex){
			System.err.println(ex);
		}
		
	}
	
	public static void subSampleFilesInDirectory(String srcDirPath, double [] percentages){
		cleanAndCreateDirectories(subSampledDirPath, percentages);
		//get all files in src path dir
		File inputDir = new File(srcDirPath);
		File[] filesList = inputDir.listFiles();
		for (File file : filesList) {
			for (int i = 0; i < percentages.length; i++) {
				readWavFilesAndSubSample(file.getPath(), subSampledDirPath +"/" +percentages[i], percentages[i]);
			}
		}

	}
	
	public static void randomizeFilesInDir(String srcDirPath , double [] percentages, int randomizationLevelPercentage){
		cleanAndCreateDirectories(shreddedDirPath, percentages);
		
		//get all files in src path dir
		File inputDir = new File(srcDirPath);
		File[] filesList = inputDir.listFiles();
		for (File file : filesList) {
			for (int i = 0; i < percentages.length; i++) {
				readWavFileAndRandomize(file.getPath(), shreddedDirPath +"/" +percentages[i], percentages[i],
						randomizationLevelPercentage,
						BUF_SIZE);
			}
		}

	}

	private static void cleanAndCreateDirectories(String path, double[] percentages) {
		//clear subSampledDirPath
		FileUtil.cleanDir(path);
		//create dir for sub-sampled files - if does not exist
		for (int i = 0; i < percentages.length; i++) {
			FileUtil.createDir(path +"/" +percentages[i]);
		}
	}

	public static void randomizeFilesInPieces(String path, double[] noOfPieces) {
		//String newIndexes = "";
		cleanAndCreateDirectories(shredded_in_pieces, noOfPieces);
		int [] rearrangedIndexes = new int[noOfPieces.length];
		File inputFile = new File(path);
		
		for (int i = 0; i < noOfPieces.length; i++) {
			try {
				WavFile wavFileRead = WavFile.openWavFile(inputFile);
				int percentageSubSampling = 100;
				int frameSize = (int) (wavFileRead.getNumFrames()/noOfPieces[i]);
				
				readWavFileAndRandomize(path, shredded_in_pieces +"/" +noOfPieces[i], percentageSubSampling,
						100,
						frameSize);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WavFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	
	
//	public static void readWavFile(String path){
//					
//		try{
////		 	Open the wav file specified as the first argument
//			WavFile wavFile = WavFile.openWavFile(new File(path));
//
//			// Display information about the wav file
//			wavFile.display();
//
//			// Get the number of audio channels in the wav file
//			int numChannels = wavFile.getNumChannels();
//
//			// Create a buffer of 100 frames
//			double[] buffer = new double[100 * numChannels];
//
//			int framesRead;
//			double min = Double.MAX_VALUE;
//			double max = Double.MIN_VALUE;
//
//			do
//			{
//				// Read frames into buffer
//				framesRead = wavFile.readFrames(buffer, 100);
//
//				// Loop through frames and look for minimum and maximum value
//				for (int s=0 ; s<framesRead * numChannels ; s++)
//				{
//					if (buffer[s] > max) max = buffer[s];
//					if (buffer[s] < min) min = buffer[s];
//				}
//			}
//			while (framesRead != 0);
//
//			// Close the wavFile
//			wavFile.close();
//
//			// Output the minimum and maximum value
//			System.out.printf("Min: %f, Max: %f\n", min, max);
//			
//		}catch(Exception ex){
//			
//		}
//	}
//	
//	public static void writeWavFile(String path){
//		try
//		{
//			int sampleRate = 44100;		// Samples per second
//			double duration = 5.0;		// Seconds
//
//			// Calculate the number of frames required for specified duration
//			long numFrames = (long)(duration * sampleRate);
//
//			// Create a wav file with the name specified as the first argument
//			WavFile wavFile = WavFile.newWavFile(new File(path), 2, numFrames, 16, sampleRate);
//
//			// Create a buffer of 100 frames
//			double[][] buffer = new double[2][100];
//
//			// Initialise a local frame counter
//			long frameCounter = 0;
//
//			// Loop until all frames written
//			while (frameCounter < numFrames)
//			{
//				// Determine how many frames to write, up to a maximum of the buffer size
//				long remaining = wavFile.getFramesRemaining();
//				int toWrite = (remaining > 100) ? 100 : (int) remaining;
//
//				// Fill the buffer, one tone per channel
//				for (int s=0 ; s<toWrite ; s++, frameCounter++)
//				{
//					buffer[0][s] = Math.sin(2.0 * Math.PI * 400 * frameCounter / sampleRate);
//					buffer[1][s] = Math.sin(2.0 * Math.PI * 500 * frameCounter / sampleRate);
//				}
//
//				// Write the buffer
//				wavFile.writeFrames(buffer, toWrite);
//			}
//
//			// Close the wavFile
//			wavFile.close();
//		}
//		catch (Exception e)
//		{
//			System.err.println(e);
//		}
//	}
}
