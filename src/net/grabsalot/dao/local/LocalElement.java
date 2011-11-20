package net.grabsalot.dao.local;

import net.grabsalot.dao.ICollectionElement;
import net.grabsalot.util.FileUtil;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import net.grabsalot.dao.local.LocalElement;

/**
 * Holds information about a local collection element. Also has useful methods.
 * 
 * @author madboyka
 * 
 */
public abstract class LocalElement implements ICollectionElement {

	protected File path;
	protected final String[] audioExtensions = {"mp3", "mp4", "flac", "ape"};
	protected final String[] imageExtensions = {"png", "jpg", "jpeg"};
	public static final int COLLECTION_ELEMENT_TYPE = 1;
	public static final int ARTIST_ELEMENT_TYPE = 2;
	public static final int ALBUM_ELEMENT_TYPE = 3;
	public static final int TRACK_ELEMENT_TYPE = 4;

	/**
	 * Returns the subdirectories of this elements path.
	 * 
	 * @return
	 */
	protected File[] getSubDirectories() {
		File dirs[] = path.listFiles(this.getDirectoryFilter());
		return dirs;
	}

	/**
	 * Return all audio files found in this elements directory.
	 * 
	 * @return
	 */
	protected File[] getAudioFiles() {
		return path.listFiles(this.getAudioFilter());
	}

	/**
	 * Return all image file found in this elements directory.
	 * 
	 * @return
	 */
	protected File[] getImageFiles() {
		return path.listFiles(this.getImageFilter());
	}

	/**
	 * Returns a FileFilter that filter for image files.
	 * 
	 * @return
	 */
	// FIXME transfer to FileUtils
	private FileFilter getImageFilter() {
		return new FileFilter() {

			@Override
			public boolean accept(File file) {
				for (String i : imageExtensions) {
					String ext = FileUtil.getFileExtension(file);
					if (ext != null && i.compareToIgnoreCase(ext) == 0) {
						return true;
					}
				}
				return false;
			}
		};
	}

	/**
	 * Returns a FileFilter that filters for audio files.
	 * 
	 * @return
	 */
	// FIXME transfer to FileUtils
	private FileFilter getAudioFilter() {
		return new FileFilter() {

			@Override
			public boolean accept(File file) {
				for (String i : audioExtensions) {
					String ext = FileUtil.getFileExtension(file);
					if (ext != null && i.compareToIgnoreCase(ext) == 0) {
						return true;
					}
				}
				return false;
			}
		};
	}

	/**
	 * Returns a FileFilter that filters for directories.
	 * 
	 * @return
	 */
	// FIXME transfer to FileUtils
	private FileFilter getDirectoryFilter() {
		return new FileFilter() {

			@Override
			public boolean accept(File file) {
				return file.isDirectory();
			}
		};
	}

	/**
	 * Sets the path for this local element
	 * 
	 * @param path
	 */
	public void setPath(File path) {
		this.path = path;
	}

	/**
	 * Gets the path of this local element
	 * 
	 * @return
	 */
	public File getPath() {
		return this.path;
	}

	/**
	 * Returns an integer representing the type of this local element.
	 * 
	 * @return possible values on {@link LocalElement}
	 */
	public abstract int getElementType();

	public abstract void saveMetadataToFile(boolean recursive);

	protected void saveXML(File file, Element xml) {
		Document doc = DocumentHelper.createDocument(xml);
		try {
			doc.addDocType(xml.getName(), "-//grabsalot//DTD Music Metadata Markup Language 0.1//EN", "http://localhost/m3l.dtd");
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(file)),format);
			writer.write(doc);
			writer.flush();
			writer.close();
		} catch (IOException ex) {
			System.out.println("FAYUL!");
			ex.printStackTrace();
		}
	}

	public abstract Element getMetadataAsXML();

	public void saveMetadataToFile() {
		saveMetadataToFile(false);
	}

	public LocalElement[] getChildren() {
		return null;
	}
}
