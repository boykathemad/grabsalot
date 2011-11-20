package net.grabsalot.util;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** File utility class
 * @author madboyka
 *
 */
public final class FileUtil {

	/** Returns the extension of file.
	 * @param file
	 * @return
	 */
	public static String getFileExtension(File file) {
		String ext = null;
		if (file.isFile()) {
			Matcher m = Pattern.compile(".+\\.([A-Za-z0-9]+)").matcher(file.getName());
			if (m.find()) {
				ext = m.group(1);
			}
		}
		return ext;
	}
	
	/** Returns the name of file w/o the extension
	 * @param file
	 * @return
	 */
	public static String stripExtenstion(File file) {
		String fileName = file.getName();
		Matcher m = Pattern.compile("(.+)\\.[A-Za-z0-9]+").matcher(fileName);
		if (m.find()) {
			fileName = m.group(1);
		}
		return fileName;
	}
}
