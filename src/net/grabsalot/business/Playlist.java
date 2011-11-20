package net.grabsalot.business;

import java.util.ArrayList;
import java.util.List;

import net.grabsalot.dao.local.LocalTrack;
import net.grabsalot.gui.PlayerPanel;
import net.grabsalot.util.AudioPlayer;
import net.grabsalot.util.players.PlaybackEvent;
import net.grabsalot.util.players.PlaybackListener;

public class Playlist implements PlaybackListener {
	private AudioPlayer player;
	private List<LocalTrack> items;
	private int index = -1;
	private PlayerPanel playerPanel;

	public Playlist() {
		this.items = new ArrayList<LocalTrack>();
		this.player = new AudioPlayer();
		this.player.addPlaybackListener(this);
	}

	public void add(LocalTrack track) {
		this.items.add(track);
	}

	public LocalTrack getNext() {
		return items.get(++index);
	}

	public void dumpTracks() {
		for (LocalTrack track : items) {
			System.out.println("Playlist:" + track.getArtist() + " - " + track);
		}
	}

	public int getTrackCount() {
		return items.size();
	}

	public void play() {
		this.dumpTracks();
		if (this.isPlaying()) {
			this.stop();
		}
		if (index < 0) {
			index = 0;
		}
		player.setAudio(this.items.get(this.index).getPath());
		System.out.println("Playlist.play():playing item: " + index);
		player.play();
	}

	public boolean isPlaying() {
		return player.isPlaying();
	}

	public void clear() {
		this.items.clear();
		this.index = -1;
	}

	public void stop() {
		this.player.stop();
	}

	@Override
	public void playbackStateChanged(int oldState, int newState, PlaybackEvent e) {
		if (newState == PlaybackListener.STATE_PLAYING) {
			this.playerPanel.setTrack(this.items.get(index), this.getTrackLength());
		}
		if (newState == PlaybackListener.STATE_STOPPED) {

		}
		if (newState == PlaybackListener.STATE_ENDED) {
			this.stop();
			index++;
			if (index == this.getTrackCount()) {
				index = 0;
			} else {
				this.play();
				this.playerPanel.setTrack(this.items.get(index), this.getTrackLength());
			}
		}
	}

	public long getTrackLength() {
		int length = player.getLength();
		if (length < 0) {
			length = this.items.get(index).getLength()*1000;
		}
		return length;
	}

	public int getPosition() {
		return player.getPosition();
	}

	@Override
	public void positionChanged(PlaybackEvent e) {

	}

	public void setPlayerPanel(PlayerPanel playerPanel) {
		this.playerPanel = playerPanel;
	}

	public AudioPlayer getPlayer() {
		return player;
	}

}
