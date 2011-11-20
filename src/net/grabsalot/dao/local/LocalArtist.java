package net.grabsalot.dao.local;

import net.grabsalot.dao.IArtist;

import org.dom4j.Element;
import net.grabsalot.dao.local.LocalAlbum;
import net.grabsalot.dao.local.LocalElement;

import java.awt.Image;
import java.io.File;
import org.dom4j.DocumentHelper;

/**
 * A {@link LocalElement} that holds artist specific information.
 * 
 * @author madboyka
 * 
 */
public class LocalArtist extends LocalElement implements IArtist {

	private LocalAlbum[] albums;
	private boolean albumsLoaded = false;

	/**
	 * Default constructor. The use of this is discouraged.
	 */
	public LocalArtist() {
		this.path = null;
	}

	/**
	 * Constructor using a file denoting the directory for the artist.
	 * 
	 * @param fromDir
	 */
	public LocalArtist(File fromDir) {
		this.path = fromDir;
	}

	/**
	 * Scans the artists directory and returns a {@link LocalAlbum} array
	 * containing albums found in the directory.
	 * 
	 * @return
	 */
	public LocalAlbum[] getAlbums() {
		if (!albumsLoaded) {
			loadAlbums();
		}
		return albums;
	}

	public void loadAlbums() {
		if (!albumsLoaded) {
			File[] dirs = this.getSubDirectories();
			albums = new LocalAlbum[dirs.length];
			for (int i = 0; i < dirs.length; ++i) {
				albums[i] = new LocalAlbum(this, dirs[i]);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see grabsalot.dao.ICollectionElement#getName()
	 */
	@Override
	public String getName() {
		return path.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getName();
	}

	/**
	 * Not yet implemented.
	 * 
	 * @return
	 */
	public Image getLogo() {

		return null;
	}

	/**
	 * Not yet implemented.
	 * 
	 * @return
	 */
	public String getBioSummary() {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see grabsalot.dao.local.LocalElement#getElementType()
	 */
	@Override
	public int getElementType() {
		return LocalElement.ARTIST_ELEMENT_TYPE;
	}

	@Override
	public void saveMetadataToFile(boolean recursive) {
		loadAlbums();
	}

	@Override
	public Element getMetadataAsXML() {
		loadAlbums();
		Element artist = DocumentHelper.createElement("artist");
		artist.addElement("name").setText(getName());
		for (LocalAlbum album : albums) {
			artist.add(album.getMetadataAsXML());
		}
		return artist;
	}
}
