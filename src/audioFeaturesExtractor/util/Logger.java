package audioFeaturesExtractor.util;

public class Logger {

	public static void logMessageOnConsole(String message){
		if(Settings.IS_DEBUG_LOG_ENABLED){
			System.out.println(message);
		}
	}
	
	public static void logResultsOnConsole(String resuls){
		if(Settings.IS_RESULTS_LOG_ENABLED){
			System.out.println(resuls);
		}
	}
}
