package net.grabsalot.dao.local;

import net.grabsalot.business.Logger;
import net.grabsalot.business.RuleSet;
import net.grabsalot.dao.IAlbum;
import net.grabsalot.dao.IArtist;
import net.grabsalot.dao.service.lastfm.Album;
import net.grabsalot.dao.service.lastfm.Artist;
import net.grabsalot.dao.service.lastfm.LastFmException;
import net.grabsalot.util.FileUtil;

import net.grabsalot.dao.local.LocalArtist;
import net.grabsalot.dao.local.LocalElement;
import net.grabsalot.dao.local.LocalTrack;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * LocalElement that holds extended information about an album.
 * 
 * @author madboyka
 * 
 */
public class LocalAlbum extends LocalElement implements IAlbum {

	private LocalArtist artist;
	private boolean tracksLoaded = false;
	private LocalTrack[] tracks;
	private boolean unanimousAlbumTags = true;
	private String name;

	// private final String[] coverNames = new String[] { "cover", "front",
	// "folder", "albumart" };
	/**
	 * Default constructor. The use of this is discouraged.
	 */
	public LocalAlbum() {
		this.path = null;
		this.name = null;
	}

	/**
	 * Constructor that uses the directory of the album to load information.
	 * 
	 * @param albumDir
	 */
	public LocalAlbum(File albumDir) {
		this.path = albumDir;
		this.artist = new LocalArtist(albumDir.getParentFile());
		this.name = path.getName();
	}

	/**
	 * Constructor that uses an artist and the directory of the album to load
	 * information.
	 * 
	 * @param artist
	 *            a {@link LocalArtist} element
	 * @param albumDir
	 *            the directory of the album
	 */
	public LocalAlbum(IArtist artist, File albumDir) {
		this(albumDir);
		this.artist = (LocalArtist) artist;
	}

	/**
	 * Scans this albums directory for audio files and returns an array of
	 * {@link LocalTrack}s
	 * 
	 * @return an array of {@link LocalTrack}s
	 */
	public LocalTrack[] getTracks() {
		if (!tracksLoaded) {
			loadTracks();
		}
		return tracks;
	}

	public void loadTracks() {
		if (!tracksLoaded) {
			File[] files = this.getAudioFiles();
			tracks = new LocalTrack[files.length];
			String lastAlbumName = null;
			for (int i = 0; i < files.length; ++i) {
				tracks[i] = new LocalTrack(files[i]);
				if (unanimousAlbumTags && lastAlbumName != null && !tracks[i].getAlbum().equals(lastAlbumName)) {
					unanimousAlbumTags = false;
				}
				if (unanimousAlbumTags) {
					lastAlbumName = tracks[i].getAlbum();
				}
			}
			if (unanimousAlbumTags && lastAlbumName != null && lastAlbumName.length() > 1) {
				this.name = lastAlbumName;
			}
			tracksLoaded = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see grabsalot.dao.ICollectionElement#getName()
	 */
	@Override
	public String getName() {
		return name;
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
	 * Returns the cover image found in this albums directory.
	 * 
	 * @return the cover {@link Image}
	 */
	public Image getCover() {
		try {
			String defaultName = RuleSet.getRule(RuleSet.coverArtNameRule).getValue();
			String defaultExtension = RuleSet.getRule(RuleSet.coverArtExtensionRule).getValue();
			File[] images = this.getImageFiles();
			File match = null;
			for (File i : images) {
				System.err.println("Matching:" + FileUtil.stripExtenstion(i) + defaultName);
				if (FileUtil.stripExtenstion(i).equals(defaultName)) {
					if (match == null) {
						match = i;
					} else if (FileUtil.getFileExtension(i).equals(defaultExtension)) {
						match = i;
					}
				}
			}
			if (images.length > 0) {
				return ImageIO.read((match == null) ? images[0] : match);
			}
		} catch (Exception e) {
			Logger._().warning("LocalAlbum:getCover:" + this.toString());
		}
		return null;
	}

	@Override
	public void saveMetadataToFile(boolean recursive) {
		saveXML(new File(path, "album.xml"), getMetadataAsXML());
	}

	/** Downloads the cover of this album from the service, saves it according to the rules and return the file object of the saved cover.
	 * Raises {@link Exception} if an error occurs.
	 * @return the file of the saved cover
	 * @throws Exception
	 */
	public File saveCover() throws Exception {
		String fileName = RuleSet.getRule(RuleSet.coverArtNameRule).getValue();
		String fileExtension = RuleSet.getRule(RuleSet.coverArtExtensionRule).getValue();
		boolean overWrite = RuleSet.getRule(RuleSet.autoOverwriteCoverArtRule).getValue().equals("yes");
		File coverFile = new File(this.getPath(), fileName + "." + fileExtension);
		if (coverFile.exists() && !overWrite) {
			throw new Exception("Album art already exists! Can't overwrite.");
		} else {
			try {
				Artist artist = new Artist();
				artist.setName(this.getArtist().getName());
				Album album = new Album(artist, this.getName());
				Image cover = album.getCover();
				if (cover.getWidth(null) > 0 && cover.getHeight(null) > 0) {
					ImageIO.write((RenderedImage) cover, fileExtension, coverFile);
				} else {
					throw new Exception("Album art is empty");
				}
			} catch (LastFmException ex) {
				throw new Exception("Could not load album.");
			} catch (IOException ex2) {
				throw new Exception("Could not load album image.");
			}
		}
		return coverFile;
	}

	/* (non-Javadoc)
	 * @see grabsalot.dao.IAlbum#getArtist()
	 */
	@Override
	public IArtist getArtist() {
		return (IArtist) this.artist;
	}

	/* (non-Javadoc)
	 * @see grabsalot.dao.local.LocalElement#getElementType()
	 */
	@Override
	public int getElementType() {
		return LocalElement.ALBUM_ELEMENT_TYPE;
	}

	@Override
	public Element getMetadataAsXML() {
		loadTracks();
		Element album = DocumentHelper.createElement("album");
		album.addElement("title").setText(getName());
		for (LocalTrack track : tracks) {
			album.add(track.getMetadataAsXML());
		}
		return album;
	}
}
