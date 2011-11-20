package net.grabsalot.util;

public class GenericUtil {

	public static String getFormattedTime(long milliseconds) {
		int seconds = Math.round((float)milliseconds/1000);
		int minutes = seconds/60;
		seconds = seconds % 60;
		return minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
	}
}
