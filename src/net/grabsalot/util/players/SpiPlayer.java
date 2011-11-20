/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.grabsalot.util.players;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

import javax.sound.sampled.UnsupportedAudioFileException;
import net.grabsalot.util.players.IPlayer;
import net.grabsalot.util.players.PlaybackEvent;
import net.grabsalot.util.players.PlaybackListener;
import net.grabsalot.util.players.SpiPlayer;

/**
 *
 * @author madboyka
 */
public class SpiPlayer implements IPlayer {

	private File audioFile;
	private boolean stopped;
	private boolean complete;
	private boolean open;
	private List<PlaybackListener> listeners;
	private int lastPosition;
	private SourceDataLine line;
	private AudioInputStream inputStream;
	private AudioInputStream stream;
	private AudioFormat outputFormat;
	private float volume = 0F;

	public SpiPlayer(File audioFile) {
		this.listeners = new ArrayList<PlaybackListener>();
		this.audioFile = audioFile;
	}

	@Override
	public void addPlaybackListener(PlaybackListener listener) {
		this.listeners.add(listener);
	}

	private Mixer getMixer() {
		return getMixer(-1);
	}

	private Mixer getMixer(int selectedId) {
		Mixer javaMixer = null;
		Mixer goodMixer = null;
		Mixer mixer = null;
		int id = 0;
		Pattern noMicrophone = Pattern.compile("microphone", Pattern.CASE_INSENSITIVE);
		for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
			id++;
			mixer = AudioSystem.getMixer(mixerInfo);
			if (selectedId > 0 && id == selectedId) {
				return mixer;
			}
			// && noMicrophone.matcher(mixerInfo.getName()).matches()
			if (goodMixer == null && mixer.getSourceLineInfo().length > 0) {
				goodMixer = mixer;
			}
			if (mixerInfo.getName().startsWith("Java Sound")) {
				javaMixer = mixer;
			}
		}
		return goodMixer != null ? goodMixer : javaMixer;
	}

	private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException {
		line = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		Mixer mixer = null;
		mixer = getMixer();
		System.out.println("Using mixer: " + mixer.getMixerInfo().getName());
		System.out.println("Mixer is supporting " + mixer.getSourceLineInfo().length + " source lines.");
		System.out.println("Mixer is supporting " + mixer.getTargetLineInfo().length + " target lines.");
		for (Line.Info linfo : mixer.getSourceLineInfo()) {
			System.out.println("Line info: " + linfo.toString());
		}
		line = (SourceDataLine) mixer.getLine(info);
		line.open(audioFormat);
		return line;
	}

	private void open() {
		try {
			AudioFileFormat aff = AudioSystem.getAudioFileFormat(audioFile);
//			if (!AudioSystem.isFileTypeSupported(aff.getType())) {
//				throw new Exception("Unsupported file");
//			}
			inputStream = AudioSystem.getAudioInputStream(audioFile);
			stream = null;
			if (inputStream != null) {
				AudioFormat baseFormat = inputStream.getFormat();
				outputFormat = new AudioFormat(
						AudioFormat.Encoding.PCM_SIGNED,
						baseFormat.getSampleRate(),
						16,
						baseFormat.getChannels(),
						baseFormat.getChannels() * 2,
						baseFormat.getSampleRate(),
						false);
				stream = AudioSystem.getAudioInputStream(outputFormat, inputStream);
				open = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			open = false;
		}
	}

	private void rawplay(AudioFormat targetFormat, AudioInputStream din) throws IOException, LineUnavailableException {
		byte[] data = new byte[4096];
		Date start = new Date();
		line = getLine(targetFormat);
		setVolume(volume);
		if (line != null) {
			// Start
			line.start();
//			FloatControl volCtrl = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
//			volCtrl.setValue(0.3F);
			int nBytesRead = 1, nBytesWritten = 0;
			while (!stopped && nBytesRead > 0) {
				lastPosition = (int) (new Date().getTime() - start.getTime());
				nBytesRead = din.read(data, 0, data.length);
				if (nBytesRead != -1) {
					nBytesWritten = line.write(data, 0, nBytesRead);
				}
			}
			// Stop
			line.drain();
			line.stop();
			line.close();
			din.close();
		}
	}

	/**
	 * Closes this player. Any audio currently playing is stopped immediately.
	 */
	private synchronized void close() {
		try {
			inputStream.close();
			open = false;
		} catch (IOException ex) {
			Logger.getLogger(SpiPlayer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public int getLength() {
		long duration = -1;
		AudioFileFormat audioFileFormat;
		try {
			audioFileFormat = AudioSystem.getAudioFileFormat(audioFile);
			Map<String, Object> properties = audioFileFormat.properties();
			duration = (Long) properties.get("duration");
			duration /= 1000000;
		} catch (UnsupportedAudioFileException ex) {
			Logger.getLogger(SpiPlayer.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(SpiPlayer.class.getName()).log(Level.SEVERE, null, ex);
		}
		return (int) duration;
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

	@Override
	public void stop() {
		for (PlaybackListener listener : this.listeners) {
			listener.playbackStateChanged(PlaybackListener.STATE_PLAYING, PlaybackListener.STATE_STOPPED,
					new PlaybackEvent());
		}
		this.close();
		stopped = true;
	}

	@Override
	public void play() {
		stopped = false;
		try {
			if (!open) {
				open();
			}
			rawplay(outputFormat, stream);
		} catch (IOException ex) {
			Logger.getLogger(SpiPlayer.class.getName()).log(Level.SEVERE, null, ex);
		} catch (LineUnavailableException ex) {
			Logger.getLogger(SpiPlayer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void setVolume(float value) {
		volume = value;
		if (line != null && line.isOpen() && line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
			FloatControl gain = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
			gain.setValue(value);
		}
	}

	@Override
	public void seek() {
	}

	@Override
	public boolean isPlaying() {
		return !stopped;
	}
}
