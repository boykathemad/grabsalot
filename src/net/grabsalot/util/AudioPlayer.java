package net.grabsalot.util;

import net.grabsalot.dao.local.LocalTrack;
import net.grabsalot.util.players.ApePlayer;
import net.grabsalot.util.players.FlacPlayer;
import net.grabsalot.util.players.IPlayer;
import net.grabsalot.util.players.MP3Player;
import net.grabsalot.util.players.PlaybackListener;
import net.grabsalot.util.players.SpiPlayer;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import net.grabsalot.util.players.PlaybackEvent;

/**
 * @author madboyka The class that plays audio files, currently only supports
 *         MP3, via the JavaLayer library. This Class needs to be updated to
 *         also play flac files.
 */
public class AudioPlayer {

	private volatile IPlayer player;
	private File audioFile;
	private PlayerThread thread;
	private boolean playing = false;
	private List<PlaybackListener> listeners;
	private float volume = 0.0F;

	public AudioPlayer() {
		this((File) null);
	}

	public AudioPlayer(LocalTrack track) {
		this(track.getPath());
	}

	public AudioPlayer(File file) {
		this.listeners = new ArrayList<PlaybackListener>();
		this.setAudio(file);
	}

	public void addPlaybackListener(PlaybackListener listener) {
		this.listeners.add(listener);
	}

	public void setAudio(File file) {
		this.audioFile = file;
		if (file != null) {
			String extension = FileUtil.getFileExtension(this.audioFile);
			if (false) {
//			} else if (extension.equals("mp3")) {
//				this.player = new MP3Player(this.audioFile);
//			} else if (extension.equals("flac")) {
//				this.player = new FlacPlayer(this.audioFile);
//			} else if (extension.equals("ape")) {
//				this.player = new ApePlayer(this.audioFile);
			} else {
				this.player = new SpiPlayer(audioFile);
//				this.player = null;
			}
			this.assignListeners();
		}
	}

	private void assignListeners() {
		if (this.player != null) {
			for (PlaybackListener listener : this.listeners) {
				this.player.addPlaybackListener(listener);
			}
		}
	}

	public void play() {
		System.out.println("AudioPlayer.play()");
		thread = new PlayerThread();
		thread.start();
		player.setVolume(volume);
	}

	public void stop() {
		player.stop();
		this.playing = false;
	}

	public int getPosition() {
		if (player != null) {
			return player.getPosition();
		}
		return 0;
	}

	public int getLength() {
		if (player != null) {
			return player.getLength();
		}
		return -1;
	}

	public void setPosition(int i) {
	}

	public boolean isPlaying() {
		if (player != null) {
			return player.isPlaying();
		}
		return false;
	}

	public void setVolume(float value) {
		volume = value;
		if (player != null) {
			player.setVolume(value);
		}
	}

	private void notifyListenersOfPlabackStateChange(int oldState, int newState, PlaybackEvent e) {
		for (PlaybackListener listener : this.listeners) {
			listener.playbackStateChanged(oldState, newState, e);
		}
	}

	class PlayerThread extends Thread {

		@Override
		public void run() {
			try {
				playing = true;
				player.play();
				notifyListenersOfPlabackStateChange(0,playing ? PlaybackListener.STATE_ENDED : PlaybackListener.STATE_STOPPED,new PlaybackEvent());
				playing = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}