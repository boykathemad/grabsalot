package net.grabsalot.dao.local;

import java.io.File;
import java.util.ArrayList;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import net.grabsalot.dao.local.LocalAlbum;
import net.grabsalot.dao.local.LocalArtist;
import net.grabsalot.dao.local.LocalElement;
import net.grabsalot.dao.local.LocalTrack;

/**
 * The class for the root directory containing the artist directories.
 *
 * @author madboyka
 *
 */
public class LocalCollection extends LocalElement {

	private LocalArtist[] artists;
	private ArrayList<LocalTrack> tracks;

	/** Default constructor.
	 * @param path the path of the directory denoting the collection.
	 */
	public LocalCollection(File path) {
		this.path = path;
	}

	/** Scans subdirectories and returns LocalArtist elements.
	 * @return artist directories found in the collections subdirectory
	 */
	public LocalArtist[] getArtists() {
		loadArtists();
		return artists;
	}

	public ArrayList<LocalTrack> scanTracks() {
		tracks = new ArrayList<LocalTrack>();
		LocalArtist[] artists = this.getArtists();
		for (LocalArtist artist : artists) {
			LocalAlbum[] albums = artist.getAlbums();
			for (LocalAlbum album : albums) {
				LocalTrack[] tracks = album.getTracks();
				for (LocalTrack track : tracks) {
					this.tracks.add(track);
				}
			}
		}
		return tracks;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (path != null) {
			return path.getName() + "(collection)";
		} else {
			return "Collection";
		}
	}

	/* (non-Javadoc)
	 * @see grabsalot.dao.local.LocalElement#getElementType()
	 */
	@Override
	public int getElementType() {
		return LocalElement.COLLECTION_ELEMENT_TYPE;
	}

	public ArrayList<LocalTrack> searchTracks(String artistName, String trackName) {
		if (this.tracks == null) {
			this.scanTracks();
		}
		ArrayList<LocalTrack> selected = new ArrayList<LocalTrack>();
		for (LocalTrack i : this.tracks) {
			if (i.getArtist().equals(artistName) && i.getName().equals(trackName)) {
				selected.add(i);
			}
		}
		return selected;
	}

	@Override
	public void saveMetadataToFile(boolean recursive) {
		saveXML(new File(path, "collection.xml"), getMetadataAsXML());
	}

	@Override
	public Element getMetadataAsXML() {
		loadArtists();
		Element collection = DocumentHelper.createElement("collection");
		collection.addElement("name").setText(getName());
		for (LocalArtist artist : artists) {
			collection.add(artist.getMetadataAsXML());
		}
		return collection;
	}

	@Override
	public String getName() {
		return path.getName();
	}

	private void loadArtists() {
		File[] dirs = this.getSubDirectories();
		artists = new LocalArtist[dirs.length];
		for (int i = 0; i < dirs.length; ++i) {
			artists[i] = new LocalArtist(dirs[i]);
		}
	}

	public synchronized void scanTracksThreaded() {
		new Thread() {

			@Override
			public void run() {
				scanTracks();
			}
		}.start();
	}
}
