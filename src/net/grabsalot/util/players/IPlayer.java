package net.grabsalot.util.players;

import java.io.File;

import net.grabsalot.util.players.PlaybackListener;

public interface IPlayer {

	public void addPlaybackListener(PlaybackListener listener);

	public void loadFile(File file);

	public void play();

	public void pause();

	public void stop();

	public void seek(float percent);

	public int getLength();

	public int getPosition();

	public void setVolume(float value);

	public boolean isPlaying();
}
