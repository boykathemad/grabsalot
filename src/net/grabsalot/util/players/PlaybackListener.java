package net.grabsalot.util.players;

import net.grabsalot.util.players.PlaybackEvent;

public interface PlaybackListener {
	public static final int STATE_LOADED = 0;
	public static final int STATE_PLAYING = 1;
	public static final int STATE_PAUSED = 2;
	public static final int STATE_STOPPED = 3;
	public static final int STATE_ENDED = 4;


	public void positionChanged(PlaybackEvent e);

	public void playbackStateChanged(int oldState, int newState, PlaybackEvent e);
}
