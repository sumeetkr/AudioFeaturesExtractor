package audioFeaturesExtractor.util;

import java.util.Random;

public class RandomNumberGenerator {

	public static int getRandomNumberInRange(int start,int end) {
		Random random = new Random();
		//get the range, casting to long to avoid overflow problems
	    long range = (long)end - (long)start + 1;
		long fraction = (long)(range *random.nextDouble());
		int randomNumber =  (int)(fraction + start);
		return randomNumber;
	}
}
