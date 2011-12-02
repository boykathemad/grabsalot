package net.grabsalot.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.grabsalot.dao.local.LocalTrack;
import net.grabsalot.util.players.IPlayer;
import net.grabsalot.util.players.PlaybackListener;
import net.grabsalot.util.players.SpiPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import net.grabsalot.util.players.PlaybackEvent;

/**
 * The class that plays audio files.
 */
public class AudioPlayer {

	private volatile IPlayer player;
	private File audioFile;
	private PlayerThread thread;
	private boolean playing = false;
	private List<PlaybackListener> listeners;
	private float volume = 0.0F;
	private final CountDownLatch latch = new CountDownLatch(0);

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
		if (playing) {
			stop();
		}
		System.out.println("AudioPlayer.play()");
		thread = new PlayerThread();
		thread.start();
		player.setVolume(volume);
	}

	public void stop() {
		player.stop();
		try {
			latch.await();
		} catch (InterruptedException ex) {
			Logger.getLogger(AudioPlayer.class.getName()).log(Level.SEVERE, null, ex);
		}
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

	private synchronized void sendEnd() {
		notifyListenersOfPlabackStateChange(0, playing ? PlaybackListener.STATE_ENDED : PlaybackListener.STATE_STOPPED, new PlaybackEvent());

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
		System.err.println("STATE CHANGE NOTIFICATION");
		for (PlaybackListener listener : this.listeners) {
			listener.playbackStateChanged(oldState, newState, e);
		}
	}

	public void seekTo(float ratio) {
		if (player != null) {
			player.seek(ratio);
		}
	}

	class PlayerThread extends Thread {

		public PlayerThread() {
			super("Player Thread");
		}

		@Override
		public void run() {
			synchronized (latch) {
				try {
					playing = true;
					player.play();
					if (playing) {
						sendEnd();
					}
					playing = false;
				} catch (Exception e) {
				} finally {
					latch.countDown();
				}
			}
		}
	}
}