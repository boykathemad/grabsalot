package net.grabsalot.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import java.util.Random;
import net.grabsalot.dao.ITrack;
import net.grabsalot.dao.local.LocalTrack;
import net.grabsalot.gui.components.PlayerPanel;
import net.grabsalot.util.AudioPlayer;
import net.grabsalot.util.players.PlaybackEvent;
import net.grabsalot.util.players.PlaybackListener;

public class Playlist implements PlaybackListener {

	public static final int SHUFFLE_MODE_NONE = 0;
	public static final int SHUFFLE_MODE_ON = 1;
	public static final int SHUFFLE_MODE_RANDOM = 2;
	public static final int REPEAT_MODE_NO = 0;
	public static final int REPEAT_MODE_TRACK = 1;
	public static final int REPEAT_MODE_ALBUM = 2;
	public static final int REPEAT_MODE_PLAYLIST = 3;
	private AudioPlayer player;
	private List<LocalTrack> items;
	private int index = -1;
	private PlayerPanel playerPanel;
	private int repeatMode = 0;
	private int shuffleMode = 0;
	private List<Integer> shuffleMap;
	private Random randomizer;

	public Playlist() {
		shuffleMap = new ArrayList<Integer>();
		items = new ArrayList<LocalTrack>();
		player = new AudioPlayer();
		randomizer = new Random(new Date().getTime());
		player.addPlaybackListener(this);
	}

	public void add(LocalTrack track) {
		items.add(track);
		afterAdd();
	}

	public void addAll(Collection<LocalTrack> allItems) {
		items.addAll(allItems);
		afterAdd();
	}

	private void afterAdd() {
		if (shuffleMode == SHUFFLE_MODE_ON) {
			createLinearMap();
			createShuffleMap();
		}
	}

	private void createLinearMap() {
		shuffleMap.clear();
		int i = 0;
		while (i++ < items.size()) {
			shuffleMap.add(i);
		}
	}

	private void createShuffleMap() {
		for (int i = shuffleMap.size() - 1; i > 0; --i) {
			int j = randomizer.nextInt(i);
			Integer a = shuffleMap.get(j);
			shuffleMap.set(j, shuffleMap.get(i));
			shuffleMap.set(i, a);
		}
		System.out.println(shuffleMap);
	}

	public LocalTrack getNext() {
		if (shuffleMode == SHUFFLE_MODE_NONE) {
			index++;
			if (index > items.size() - 1) {
				index = 0;
			}
			return items.get(index);
		} else if (shuffleMode == SHUFFLE_MODE_ON) {
			index++;
			if (index > items.size() - 1) {
				index = 0;
				createShuffleMap();
			}
			return items.get(shuffleMap.get(index));
		} else {
			return items.get(randomizer.nextInt(items.size()));
		}
	}

	public List<ITrack> getTracks() {
		return new ArrayList<ITrack>(items);
	}

	public int getTrackCount() {
		return items.size();
	}

	public void play() {
		if (this.isPlaying()) {
			this.stop();
		}
		if (index < 0) {
			index = 0;
		}
		player.setAudio(this.items.get(this.index).getPath());
		System.out.println("Playlist.play():playing item: " + items.get(index));
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
			length = this.items.get(index).getLength();
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

	public ITrack getCurrentTrack() {
		return this.items.get(index);
	}

	public AudioPlayer getPlayer() {
		return player;
	}
}
