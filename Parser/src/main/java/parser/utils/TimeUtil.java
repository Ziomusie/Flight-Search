package parser.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeUtil {
	private static final Logger log = LoggerFactory.getLogger(TimeUtil.class);
	
	private long started;
	
	private TimeUtil() {
		started = System.currentTimeMillis();
	}
	
	public static TimeUtil startMeasure() {
		return new TimeUtil();
	}
	
	public long getDuration() {
		return System.currentTimeMillis() - started;
	}
	
	public String getDurationStr() {
		return parse(getDuration());
	}
	
	public static String parse(long millis) {
		String secStr;
		String minStr = "";
		
		long sec = millis / 1000;
		long min = sec / 60;
		long finalMs = millis - 1000*(60*min + sec);
		if (sec < 60) {
			secStr = sec + "s";
		}
		else {
			secStr = (sec - min*60) + "s";
			minStr = min + "m ";
		}
		
		return minStr + secStr + " " + finalMs + "ms";
	}
	
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException e) {
			log.warn("Error while sleeping!");
		}
	}
	
}
