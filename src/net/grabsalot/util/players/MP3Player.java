package net.grabsalot.util.players;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Vector;

import net.grabsalot.util.players.IPlayer;
import net.grabsalot.util.players.PlaybackEvent;
import net.grabsalot.util.players.PlaybackListener;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.SampleBuffer;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;

public class MP3Player implements IPlayer {

	private AudioDevice audio;
	private File audioFile;
	private Bitstream bitstream;
	private boolean stopped;
	private boolean complete;
	private Decoder decoder;
	private List<PlaybackListener> listeners;
	private int lastPosition;

	public MP3Player(File audioFile) {
		this.listeners = new Vector<PlaybackListener>();
		this.audioFile = audioFile;
		try {
			bitstream = new Bitstream(new FileInputStream(this.audioFile));
			audio = FactoryRegistry.systemRegistry().createAudioDevice();
			audio.open(decoder = new Decoder());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JavaLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addPlaybackListener(PlaybackListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * Cloases this player. Any audio currently playing is stopped immediately.
	 */
	private synchronized void close() {
		AudioDevice out = audio;
		if (out != null) {
			stopped = true;
			audio = null;
			// this may fail, so ensure object state is set up before
			// calling this method.
			out.close();
			lastPosition = out.getPosition();
			try {
				bitstream.close();
			} catch (BitstreamException ex) {
			}
		}
	}

	/**
	 * Decodes a single frame.
	 *
	 * @return true if there are no more frames to decode, false otherwise.
	 */
	protected boolean decodeFrame() throws JavaLayerException {
		try {
			AudioDevice out = audio;
			if (out == null)
				return false;

			Header h = bitstream.readFrame();
			if (h == null)
				return false;

			// sample buffer set when decoder constructed
			SampleBuffer output = (SampleBuffer) decoder.decodeFrame(h, bitstream);

			this.lastPosition = out.getPosition();
			synchronized (this) {
				out = audio;
				if (out != null) {
					out.write(output.getBuffer(), 0, output.getBufferLength());
				}
			}

			bitstream.closeFrame();
		} catch (RuntimeException ex) {
			throw new JavaLayerException("Exception decoding audio frame", ex);
		}
		return true;
	}

	public int getFrameCount() {
		boolean ret = true;
		int frames = 0;
		while (ret) {
			try {
				ret = skipFrame();
			} catch (JavaLayerException e) {
				e.printStackTrace();
			}
			frames++;
		}
		return frames;
	}

	@Override
	public int getLength() {
		return -1;
	}

	@Override
	public int getPosition() {
		return this.lastPosition;
	}

	public boolean isComplete() {
		return this.complete;
	}

	@Override
	public void loadFile(File file) {
		this.audioFile = file;
	}

	@Override
	public void pause() {

	}

	public void play() {
		System.out.println("MP3Player.play()");
		try {
			this.play(Integer.MAX_VALUE);
		} catch (Exception e) {
		}
	}

	public boolean play(int frames) throws JavaLayerException {
		boolean ret = true;

		// report to listener
		for (PlaybackListener listener : this.listeners) {
			// System.out.println("MP3Player.play()" + listener.toString());
			listener.playbackStateChanged(PlaybackListener.STATE_LOADED, PlaybackListener.STATE_PLAYING,
					new PlaybackEvent());
		}

		while (frames-- > 0 && ret) {
			ret = decodeFrame();
		}

		// if (!ret)
		{
			// last frame, ensure all data flushed to the audio device.
			AudioDevice out = audio;
			if (out != null) {
				// System.out.println(audio.getPosition());
				out.flush();
				// System.out.println(audio.getPosition());
				synchronized (this) {
					complete = (!stopped);
					close();
				}

				// report to listener
				for (PlaybackListener listener : this.listeners) {
					listener.playbackStateChanged(PlaybackListener.STATE_PLAYING, PlaybackListener.STATE_ENDED,
							new PlaybackEvent());
				}
			}
		}
		return ret;
	}

	public boolean play(final int start, final int end) throws JavaLayerException {
		boolean ret = true;
		int offset = start;
		while (offset-- > 0 && ret)
			ret = skipFrame();
		return play(end - start);
	}

	@Override
	public void seek(float percent) {
		// TODO Auto-generated method stub
	}

	protected boolean skipFrame() throws JavaLayerException {
		Header h = bitstream.readFrame();
		if (h == null)
			return false;
		bitstream.closeFrame();
		return true;
	}

	public void stop() {
		for (PlaybackListener listener : this.listeners) {
			listener.playbackStateChanged(PlaybackListener.STATE_PLAYING, PlaybackListener.STATE_STOPPED,
					new PlaybackEvent());
		}
		this.close();
		stopped = true;
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