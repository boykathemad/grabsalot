package net.grabsalot.util.players;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import net.grabsalot.util.players.IPlayer;
import net.grabsalot.util.players.PlaybackEvent;
import net.grabsalot.util.players.PlaybackListener;
import org.kc7bfi.jflac.FLACDecoder;
import org.kc7bfi.jflac.apps.Player;
import org.kc7bfi.jflac.metadata.StreamInfo;
import org.kc7bfi.jflac.util.ByteData;

public class FlacPlayer extends Player implements IPlayer {
	private File file;
	private boolean stopped = false;
	private SourceDataLine line;
	private FLACDecoder decoder;
	private AudioFormat fmt;
	private DataLine.Info info;
	private List<PlaybackListener> listeners;

	public FlacPlayer() {
		this(null);
	}

	public FlacPlayer(File file) {
		this.file = file;
		this.listeners = new Vector<PlaybackListener>();
	}

	@Override
	public void decode(String inFileName) throws IOException, LineUnavailableException {

		for (PlaybackListener listener : listeners) {
			listener.playbackStateChanged(PlaybackListener.STATE_LOADED, PlaybackListener.STATE_PLAYING, new PlaybackEvent());
		}
		FileInputStream is = new FileInputStream(inFileName);

		decoder = new FLACDecoder(is);
		decoder.addPCMProcessor(this);
		try {
			decoder.decode();
		} catch (EOFException e) {
			// skip
		}
		line.drain();
		line.close();
		for (PlaybackListener listener : listeners) {
			listener.playbackStateChanged(PlaybackListener.STATE_PLAYING, PlaybackListener.STATE_ENDED, new PlaybackEvent());
		}
	}

	@Override
	public void processStreamInfo(StreamInfo streamInfo) {
		try {
			fmt = streamInfo.getAudioFormat();
			info = new DataLine.Info(SourceDataLine.class, fmt, AudioSystem.NOT_SPECIFIED);
			line = (SourceDataLine) AudioSystem.getLine(info);

			// Add the listeners to the line at this point, it's the only
			// way to get the events triggered.
			/*
			 * int size = listeners.size(); for (int index = 0; index < size;
			 * index++) line.addLineListener((LineListener)
			 * listeners.get(index));
			 */
			line.open(fmt, AudioSystem.NOT_SPECIFIED);
			line.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addPlaybackListener(PlaybackListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public int getLength() {
		return -1;
	}

	@Override
	public void loadFile(File file) {
		this.file = file;
	}

	@Override
	public void play() {
		System.out.println("FlacPlayer.play()");
		try {
			stopped = false;
			this.decode(file.getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
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
		stopped = true;
		for (PlaybackListener listener : listeners) {
			listener.playbackStateChanged(PlaybackListener.STATE_PLAYING, PlaybackListener.STATE_STOPPED, new PlaybackEvent());
		}
		System.out.println("FlacPlayer.stop()");
	}

	@Override
	public void processPCM(ByteData pcm) {
		line.write(pcm.getData(), 0, pcm.getLen());
		if (this.stopped) {
			decoder.removePCMProcessor(this);
		}
	}

	@Override
	public int getPosition() {
		if (line != null) {
			return (int) (line.getMicrosecondPosition()/1000);
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
