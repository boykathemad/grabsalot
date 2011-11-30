package net.grabsalot.dao.local;

import java.util.logging.Level;
import net.grabsalot.business.Logger;
import net.grabsalot.dao.ITrack;
import net.grabsalot.util.FileUtil;

import org.dom4j.Element;

import java.io.File;
import java.io.IOException;
import org.dom4j.DocumentHelper;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

/**
 * A {@link LocalElement} that holds track specific information, and contains
 * useful methods.
 *
 * @author madboyka
 *
 */
public class LocalTrack extends LocalElement implements ITrack {
	public static final FieldKey ARTIST_TAG = FieldKey.ARTIST;
	private AudioFile audioFile;
	private Tag tagger;

	/**
	 * Default constructor. The use of this is discouraged.
	 */
	public LocalTrack() {
		this.path = null;
	}

	/**
	 * Constructor that uses a file.
	 *
	 * @param fromFile
	 */
	public LocalTrack(File fromFile) {
		this.path = fromFile;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see grabsalot.dao.ICollectionElement#getName()
	 */
	@Override
	public String getName() {
		if (this.getTagger() == null) {
			return this.getFileName();
		} else {
			return this.getTag(FieldKey.TITLE);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getName();
	}

	/**
	 * Return the tagging information for this local track
	 *
	 * @return
	 */
	public Tag getTagger() {
		if (!path.exists()) {
			return null;
		}
		if (audioFile == null) {
			try {
				if (FileUtil.getFileExtension(path).equalsIgnoreCase("mp3")) {
					MP3File f = (MP3File) AudioFileIO.read(this.path);
					if (f.hasID3v2Tag()) {
						this.tagger = f.getID3v2TagAsv24();
					} else {
						this.tagger = f.getTag();
					}
					this.audioFile = f;
				} else {
					this.audioFile = AudioFileIO.read(this.path);
					this.tagger = this.audioFile.getTag();
				}
				return tagger;
			} catch (CannotReadException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (TagException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (ReadOnlyFileException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (InvalidAudioFrameException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		} else {
			if (this.tagger != null) {
				return this.tagger;
			}
		}
		return null;
	}

	/**
	 * Returns the length of this track.
	 *
	 * @return number representing the length of this track in seconds.
	 */
	public int getLength() {
		try {
			return this.audioFile.getAudioHeader().getTrackLength();
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Returns a string that containes the formatted length of this track.
	 *
	 * @return the formatted length
	 */
	public String getFormattedLength() {
		int length = this.getLength();
		int secs = (length % 60);
		int mins = (length / 60);
		return mins + ":" + (secs < 10 ? "0" : "") + secs;
	}

	/**
	 * Returns the bitrate of this track.
	 *
	 * @return
	 */
	public String getBitrate() {
		try {
			return this.audioFile.getAudioHeader().getBitRate();
		} catch (Exception e) {
			return "0";
		}
	}

	/**
	 * Returns the sample rate of this track.
	 *
	 * @return
	 */
	public String getSampleRate() {
		try {
			return this.audioFile.getAudioHeader().getSampleRate();
		} catch (Exception e) {
			return "0";
		}
	}

	/**
	 * Returns the audio format of this track as a string. Supported formats
	 * depend on the jaudiotagger library.
	 *
	 * @return
	 */
	public String getFormat() {
		try {
			String format = this.audioFile.getAudioHeader().getFormat();
			if (format.equals("MPEG-1 Layer 3")) {
				return "MP3";
			} else {
				return format;
			}
		} catch (Exception e) {
			return "Unknown";
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see grabsalot.dao.local.LocalElement#getElementType()
	 */
	@Override
	public int getElementType() {
		return LocalElement.TRACK_ELEMENT_TYPE;
	}

	/**
	 * Returns the filename of this track.
	 *
	 * @return
	 */
	public String getFileName() {
		return this.path.getName();
	}

	/**
	 * Returns the track title from the tags of this track.
	 *
	 * @return
	 */
	public String getTitle() {
		return this.getTag(FieldKey.TITLE);
	}

	/**
	 * Returns the artist name from the tags of this track.
	 *
	 * @return
	 */
	public String getArtist() {
		return this.getTag(FieldKey.ARTIST);
	}

	/**
	 * Returns the album name from the tags of this track.
	 *
	 * @return
	 */
	public String getAlbum() {
		return this.getTag(FieldKey.ALBUM);
	}

	/**
	 * Returns the year of realease from the tags of this track.
	 *
	 * @return
	 */
	public String getReleaseDate() {
		return this.getTag(FieldKey.YEAR);
	}

	/**
	 * Returns the tracknumber from the tags of this track.
	 *
	 * @return
	 */
	public String getTrackNumber() {
		return this.getTag(FieldKey.TRACK);
	}

	/**
	 * Returns the value of the tag denoted be fieldKey
	 *
	 * @param fieldKey
	 * @return
	 */
	public String getTag(FieldKey fieldKey) {
		if (this.getTagger() == null) {
			return "";
		}
		try {
			return tagger.getFirst(fieldKey);
		} catch (Exception ex) {
			Logger._().log(Level.SEVERE, "LocalTrack:getTag: jaudioTagger: some nullpointer exception.{0}", this.toString());
		}
		return "";
	}

	@Override
	public void saveMetadataToFile(boolean recursive) {
	}

	@Override
	public Element getMetadataAsXML() {
		Element track = DocumentHelper.createElement("track");
		track.addElement("filename").setText(getFileName());
		track.addElement("size").setText(Long.toString(path.length()));
		track.addElement("genre").setText(getTag(FieldKey.GENRE));
		track.addElement("albumartist").setText(getTag(FieldKey.ARTIST));
		track.addElement("albumname").setText(getTag(FieldKey.ALBUM));
		track.addElement("albumyear").setText(getTag(FieldKey.YEAR));
		return track;
	}
}
