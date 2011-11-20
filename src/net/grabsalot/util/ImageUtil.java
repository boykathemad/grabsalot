package net.grabsalot.util;


import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.grabsalot.core.Application;

/** Image utility class
 * @author madboyka
 *
 */
public class ImageUtil {

	/** Resizes the image to the specified width and height, and returns a resized BufferedImage
	 * @param image image to be resized
	 * @param width new image width
	 * @param height new image height
	 * @return the resized image
	 */
	public static BufferedImage resize(Image image, int width, int height) {
		BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		return resizedImage;
	}

	/** Loads an image via the resource loader
	 * @param resourceName
	 * @return
	 */
	public static BufferedImage loadFromResource(String resourceName) {
		try {
			return ImageIO.read(Application.class.getResource("/" + resourceName));
		} catch (IOException ex) {
			//ex.printStackTrace();
		} catch (IllegalArgumentException ex) {
			//ex.printStackTrace();
		}
		return null;
	}
}
