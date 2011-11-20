package net.grabsalot.dao.service.lastfm;

import java.util.HashMap;
import java.util.List;

import net.grabsalot.business.Cacher;
import net.grabsalot.business.Playlist;
import net.grabsalot.dao.local.LocalCollection;
import net.grabsalot.dao.local.LocalTrack;

import org.dom4j.Element;

import net.grabsalot.dao.service.lastfm.ApiConnect;
import net.grabsalot.dao.service.lastfm.LastFmException;
import net.grabsalot.dao.service.lastfm.Track;

public class User extends ApiConnect {

	private String username;

	public User(String username) {
		this.username = username;
	}

	public Track getLastTrack() {
		return new Track();
	}

	@Override
	public String getTags() {
		// TODO Auto-generated method stub
		return null;
	}

	public Playlist getRecentTracksPlaylist(int length) {
		Playlist playlist = new Playlist();
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("user", this.username);
		parameters.put("limit", Integer.toString(length));
		try {
			this.callMethod("user.getRecentTracks", parameters);
		} catch (LastFmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.info = this.info.element("recenttracks");
		List<Element> tracks = this.getProperties("track");
		LocalCollection man = Cacher.getMainFrame().getCollectionManager();
		for (Element track : tracks) {
			System.out.println("User.getRecentTracksPlaylist()");
			List<LocalTrack> found = man.searchTracks(track.element("artist").getText(), track.element("name")
					.getText());
			if (found.size() > 0) {
				playlist.add(found.get(0));
			}
		}
		return playlist;
	}

	public Element getRecentTracks(int length) {
		Playlist playlist = new Playlist();
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("user", this.username);
		parameters.put("limit", Integer.toString(length));
		try {
			this.callMethod("user.getRecentTracks", parameters);
		} catch (LastFmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.info = this.info.element("recenttracks");
		return this.info;
	}

	public Playlist getRecentTracksPlaylist() {
		return this.getRecentTracksPlaylist(10);
	}

}
