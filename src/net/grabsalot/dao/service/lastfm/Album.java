package net.grabsalot.dao.service.lastfm;

import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import net.grabsalot.business.Cacher;
import net.grabsalot.business.Logger;
import net.grabsalot.dao.IAlbum;

import org.dom4j.Element;

import net.grabsalot.dao.service.lastfm.Album;
import net.grabsalot.dao.service.lastfm.ApiConnect;
import net.grabsalot.dao.service.lastfm.Artist;
import net.grabsalot.dao.service.lastfm.LastFmException;

/** An albums representation from the last.fm service.
 * @author madboyka
 *
 */
public class Album extends ApiConnect implements IAlbum {

	protected Artist artist;
	protected String name;

	/**
	 * Default constructor.
	 */
	public Album() {
		info = null;
	}

	/** Constructor using an {@link Artist} object and an album name string.
	 * @param artist
	 * @param albumName
	 * @throws LastFmException
	 */
	public Album(Artist artist, String albumName) throws LastFmException {
		this.artist = artist;
		this.load(albumName);
	}

	/** Loads the default informations for this album, from the service.
	 * @param albumName the name of the album to use for loading.
	 * @throws LastFmException
	 */
	private void load(String albumName) throws LastFmException {
		Album cached = (Album) Cacher.getItem(this.getClass().getName() + "_" + this.artist.getName() + albumName);
		if (cached == null) {
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("artist", this.artist.getName());
			parameters.put("album", albumName);
			this.callMethod("album.getInfo", parameters);
			this.info = this.info.element("album");
			System.out.println("Got album.");
			Cacher.addItem(this.getClass().getName() + "_" + this.artist.getName() + albumName, this);
		} else {
			this.info = cached.info;
			this.name = cached.name;
			System.out.println("Got album from cache.");
		}
	}

	/* (non-Javadoc)
	 * @see grabsalot.dao.IAlbum#getArtist()
	 */
	@Override
	public Artist getArtist() {
		return this.artist;
	}

	/* (non-Javadoc)
	 * @see grabsalot.dao.ICollectionElement#getName()
	 */
	@Override
	public String getName() {
		try {
			return this.getProperty("name").getText();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	/** Return the albums wikis full content.
	 * @return
	 */
	public String getWikiContent() {
		String result = "";
		try {
			result = this.getProperty("wiki").element("summary").getText();
		} catch (NullPointerException ex) {
			Logger._().warning("Album:getWikiSummary: wiki is null." + this.toString());
		}
		return result;
	}

	/** Returns the summary of the albums wiki content.
	 * @return
	 */
	public String getWikiSummary() {
		String result = "";
		try {
			result = this.getProperty("wiki").element("summary").getText();
		} catch (NullPointerException ex) {
			Logger._().warning("Album:getWikiSummary: wiki is null." + this.toString());
		}
		return result;
	}

	/** Returns the number of listeners of this album.
	 * @return
	 */
	public int getListeners() {
		return Integer.parseInt(this.getProperty("listeners").getText());
	}

	/** Returns the playcount of this album.
	 * @return
	 */
	public int getPlayCount() {
		return Integer.parseInt(this.getProperty("playcount").getText());
	}

	/** Returns the listeners and playcount of this album formatted as text
	 * @param asHtml whether to format the text as html or not
	 * @return
	 */
	public String getStatistics(boolean asHtml) {
		NumberFormat nf = NumberFormat.getInstance();
		if (asHtml) {
			return "<em>" + nf.format(this.getListeners()) + "</em> listeners / <em>" + nf.format(this.getPlayCount())
					+ "</em> scrobbles";
		} else {
			return nf.format(this.getListeners()) + " listeners / " + nf.format(this.getPlayCount()) + " scrobbles";
		}
	}

	/** Returns the downloaded cover art of this album, or null on error.
	 * @return
	 */
	public Image getCover() {
		try {
			List<Element> images = this.getProperties("image");
			for (Element i : images) {
				if (i.attributeValue("size").equals("extralarge")) {
					return ImageIO.read(new URL(i.getText()));
				}
			}
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return null;
	}

	/** Returns the url of this albums cover, or null on error.
	 * @return
	 */
	public URL getCoverUrl() {
		try {
			List<Element> images = this.getProperties("image");
			for (Element i : images) {
				if (i.attributeValue("size").equals("extralarge")) {
					return new URL(i.getText());
				}
			}
		} catch (MalformedURLException e) {
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see grabsalot.dao.service.lastfm.ApiConnect#getTags()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getTags() {
		String tagsString = "";
		List<Element> tagNodes = this.getProperty("toptags").elements("tag");
		for (int i = 0; i < tagNodes.size(); ++i) {
			if (i > 0) tagsString += ", ";
			tagsString += tagNodes.get(i).elementText("name");
		}
		return tagsString;
	}
}
