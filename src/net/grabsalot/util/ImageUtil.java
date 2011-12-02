package net.grabsalot.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
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
		if (true) {//TODO create setting
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		System.out.println("Image resized:" + image);
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		return resizedImage;
	}

	public static BufferedImage blurImage(BufferedImage image) {
		float ninth = 1.0f / 2.0f;
		float[] blurKernel = {
			ninth, ninth, ninth,
			ninth, ninth, ninth,
			ninth, ninth, ninth
		};

		Map map = new HashMap();

		map.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		map.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		map.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		RenderingHints hints = new RenderingHints(map);
		BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, blurKernel), ConvolveOp.EDGE_NO_OP, hints);
		return op.filter(image, null);
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
