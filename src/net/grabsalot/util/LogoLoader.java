package net.grabsalot.util;

import net.grabsalot.business.Logger;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/** Class that loads artists logo images
 * @author madboyka
 *
 */
public class LogoLoader {

	public static File logoPath = new File("X:\\music\\bandlogos");
	public static String[] extensions = new String[]{"png","gif","jpg"};

	/** Return the logo image of the artist denoted by artistName
	 * @param artistName the name of the artist
	 * @return the logo image
	 */
	public static Image getLogo(String artistName) {
		try {
			if (!logoPath.exists()) {
				return null;
			}
			File imageFile;
			int i = 0;
			do {
				imageFile = new File(logoPath, artistName+"." + extensions[i++]);
			} while (!imageFile.exists() || i > extensions.length);
			return ImageIO.read(imageFile);
		} catch (IOException e) {
			Logger._().warning("LogoLoader:getLogo: failed to load logo for " + artistName);
		}
		return null;
	}
}
