package net.grabsalot.dao.service.lastfm;

import net.grabsalot.business.Cacher;
import net.grabsalot.business.Logger;
import net.grabsalot.dao.IArtist;

import net.grabsalot.dao.service.lastfm.ApiConnect;
import net.grabsalot.dao.service.lastfm.Artist;
import net.grabsalot.dao.service.lastfm.LastFmException;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.dom4j.Element;

/**
 * An artist representation from the last.fm service.
 * 
 * @author madboyka
 * 
 */
public class Artist extends ApiConnect implements IArtist {

	private String name;

	/**
	 * Default constructor.
	 */
	public Artist() {
		info = null;
	}

	/**
	 * Constructor using the artists name.
	 * 
	 * @param artistName
	 * @throws LastFmException
	 */
	public Artist(String artistName) throws LastFmException {
		this.load(artistName);
	}

	/**
	 * Loads basic information about the artist from the service.
	 * 
	 * @param artistName
	 * @throws LastFmException
	 */
	public void load(String artistName) throws LastFmException {
		Artist cached = (Artist) Cacher.getItem(this.getClass().getName() + "_" + artistName);
		if (cached == null) {
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("artist", artistName);
			this.callMethod("artist.getInfo", parameters);
			this.info = this.info.element("artist");
			System.out.println("Got artist.");
			Cacher.addItem(this.getClass().getName() + "_" + artistName, this);
		} else {
			this.info = cached.info;
			this.name = cached.name;
			System.out.println("Got artist from cache.");
		}
	}

	/**
	 * Sets the basic information xml for this artist, useful w/ similar
	 * artists.
	 * 
	 * @param xml
	 */
	public void setInfoXml(Element xml) {
		this.info = (Element) xml.clone();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see grabsalot.dao.ICollectionElement#getName()
	 */
	@Override
	public String getName() {
		if (this.name == null) {
			try {
				this.name = this.getProperty("name").getText();
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
		return this.name;
	}

	/**
	 * Sets the name of this artist. The usage is discouraged.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the first page of images of an artist, and returns a {@link Vector}
	 * of {@link URL}s representing the images.
	 * 
	 * @return
	 * @throws LastFmException
	 */
	public Vector<URL> getImages() throws LastFmException {
		Vector<URL> images = new Vector<URL>();
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("artist", "Novembers Doom");
		this.callMethod("artist.getImages", parameters);
		return images;
	}

	/** Returns the full contents of this artists biography.
	 * @return
	 */
	public String getBioContent() {
		return this.getProperty("bio").element("content").getText();
	}

	/** Returns the summary of this artists biography.
	 * @return
	 */
	public String getBioSummary() {
		return this.getProperty("bio").element("summary").getText();
	}

	/** Returns the number of listeners.
	 * @return
	 */
	public int getListeners() {
		return Integer.parseInt(this.getProperty("stats").element("listeners").getText());
	}

	/** Returns the playcount.
	 * @return
	 */
	public int getPlayCount() {
		return Integer.parseInt(this.getProperty("stats").element("playcount").getText());
	}

	/** Returns the listeners and playcount of this artist, formatted as text
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

	/** Returns the URL of the first picture of this artist.
	 * @param size the size of the picture to return
	 * @return
	 */
	public URL getImageUrl(String size) {
		try {
			List<Element> images = this.getProperties("image");
			for (Element i : images) {
				if (i.attributeValue("size").equals(size)) {
					return new URL(i.getText());
				}
			}
		} catch (MalformedURLException e) {
			Logger._().warning("Artist:getImageUrl:MalformedURLException" + this.toString());
		}
		return null;
	}

	/** Returns the URL of the first picture of this artist that has extralarge size
	 * @return
	 */
	public URL getImageUrl() {
		return this.getImageUrl("extralarge");
	}

	/** Returns an array of artists, containing this artists similar artists.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Artist[] getSimilarArtists() {
		List<Element> similarArtistNodes = this.getProperty("similar").elements("artist");
		Artist[] similarArtists = new Artist[similarArtistNodes.size()];
		for (int i = 0; i < similarArtistNodes.size(); ++i) {
			similarArtists[i] = new Artist();
			similarArtists[i].setInfoXml(similarArtistNodes.get(i));
		}
		return similarArtists;
	}

	/* (non-Javadoc)
	 * @see grabsalot.dao.service.lastfm.ApiConnect#getTags()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getTags() {
		String tagsString = "";
		List<Element> tagNodes = this.getProperty("tags").elements("tag");
		for (int i = 0; i < tagNodes.size(); ++i) {
			if (i > 0)
				tagsString += ", ";
			tagsString += tagNodes.get(i).elementText("name");
		}
		return tagsString;
	}
}
