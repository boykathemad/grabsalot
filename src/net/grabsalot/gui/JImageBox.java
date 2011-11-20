package net.grabsalot.gui;

import net.grabsalot.business.Logger;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/** This class serves as an image container, that can load images in separate threads.
 * @author madboyka
 *
 */
public class JImageBox extends JLabel {

	private static final long serialVersionUID = -2162826495168625683L;

	private Image image;

	/**
	 * Default constructor.
	 */
	public JImageBox() {
		super();
	}

	/** Constructor using an image
	 * @param image
	 */
	public JImageBox(Image image) {
		this();
		this.setImage(image);
	}

	/** Constructor using a file
	 * @param imageFile
	 */
	public JImageBox(File imageFile) {
		this();
		this.setImage(imageFile);
	}

	/** Constructor using a url
	 * @param imageUrl
	 */
	public JImageBox(URL imageUrl) {
		this();
		this.setImage(imageUrl);
	}

	/** Directly sets the image of this component
	 * @param image
	 */
	public void setImage(Image image) {
		try {
			this.image = image;
			this.setIcon(new ImageIcon(image));
		} catch (NullPointerException ex) {
			Logger._().warning("JImageBox:setImage:could not load image: input is null");
		}
	}

	/** Loads the image from the file and sets it the this component
	 * @param imageFile
	 */
	public void setImage(File imageFile) {
		try {
			this.setImage(ImageIO.read(imageFile));
		} catch (IOException e) {
			this.setText("Could not load image");
		}
	}

	/** Loads the image in a separate thread
	 * @param imageUrl
	 */
	public void setImage(URL imageUrl) {
		try {
			imageUrl.openConnection();
			new UrlLoader(imageUrl).start();
			this.setText("");
		} catch (Exception e) {
			this.setText("Could not load image");
		}
	}

	/** This thread loads an image and sets it for the parent JImageBox class
	 * @author madboyka
	 *
	 */
	private class UrlLoader extends Thread {
		private URL url;

		/** Constructor having a url
		 * @param url
		 */
		public UrlLoader(URL url) {
			this.url = url;
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			try {
				JImageBox.this.setImage(ImageIO.read(url));
			} catch (IIOException ex) {
				Logger._().warning("JImageBox.UrlLoader:run:" + ex.getMessage());
			} catch (IOException ex) {
				Logger._().warning("JImageBox.UrlLoader:run:" + ex.getMessage());
			}
		}
	}

	/** Returns the image, this component is showing. Might be null.
	 * @return
	 */
	public Image getImage() {
		return this.image;
	}
}
