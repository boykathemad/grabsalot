package net.grabsalot.util.players;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import davaguine.jmac.player.Player;

public class ApePlayer implements IPlayer {
	private File audioFile;
	private Player player;
	private List<PlaybackListener> listeners;

	public ApePlayer(File file) {
		this.audioFile = file;
		this.listeners = new Vector<PlaybackListener>();
	}

	@Override
	public void addPlaybackListener(PlaybackListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public int getLength() {
		if (player != null) {
			return player.getDecoder().getApeInfoLengthMs();
		}
		return -1;
	}

	@Override
	public void loadFile(File file) {
		audioFile = file;
	}

	@Override
	public void play() {
		try {
			for (PlaybackListener listener : listeners) {
				listener.playbackStateChanged(PlaybackListener.STATE_LOADED, PlaybackListener.STATE_PLAYING,
						new PlaybackEvent());
			}
			player = new Player(audioFile.getAbsolutePath());
			player.play();
			for (PlaybackListener listener : listeners) {
				listener.playbackStateChanged(PlaybackListener.STATE_PLAYING, PlaybackListener.STATE_ENDED,
						new PlaybackEvent());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void seek() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		if (player != null) {
			try {
				player.close();
				for (PlaybackListener listener : listeners) {
					listener.playbackStateChanged(PlaybackListener.STATE_PLAYING, PlaybackListener.STATE_STOPPED,
							new PlaybackEvent());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			player = null;
		}
	}

	@Override
	public int getPosition() {
		if (player != null) {
			return player.getPosition();
		}
		return 0;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setVolume(float value) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isPlaying() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
