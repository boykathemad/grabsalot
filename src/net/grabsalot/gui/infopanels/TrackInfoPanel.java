package net.grabsalot.gui.infopanels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import net.grabsalot.dao.local.LocalTrack;
import net.grabsalot.dao.service.lastfm.Track;

import net.grabsalot.gui.infopanels.GenericInfoPanel;

import javax.swing.JLabel;

/** An info panel that displays track specific information.
 * @author madboyka
 *
 */
public class TrackInfoPanel extends GenericInfoPanel {

	private static final long serialVersionUID = 6993466084731368234L;
	private final Dimension defaultSize = new Dimension(620, 100);

	private JLabel fileName, trackName, trackArtist, trackAlbum, format, tags;
	private LocalTrack localTrack;

	/**
	 * Default constructor.
	 */
	public TrackInfoPanel() {
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(this.defaultSize);

		this.setupComponents();
		this.alignComponents();
	}

	/** Constructor using a local track
	 * @param track
	 */
	public TrackInfoPanel(LocalTrack track) {
		this();
		this.localTrack = track;
		loadFromLocal();
	}

	/** Constructor using a service track
	 * @param track
	 */
	public TrackInfoPanel(Track track) {
		this();
		loadFromService(track);
	}


	/* (non-Javadoc)
	 * @see grabsalot.gui.GenericInfoPanel#alignComponents()
	 */
	@Override
	protected void alignComponents() {

	}

	/* (non-Javadoc)
	 * @see grabsalot.gui.GenericInfoPanel#setupComponents()
	 */
	@Override
	protected void setupComponents() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.BOTH;

		this.fileName = new JLabel();
		this.fileName.setForeground(this.foregroundColor);
		gbc.gridy = 0;
		gbc.gridx = 1;
		this.add(this.fileName, gbc);

		this.trackName = new JLabel();
		this.trackName.setForeground(this.foregroundColor);
		this.trackName.setFont(new Font("Segoe", Font.BOLD, 24));
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		this.add(this.trackName, gbc);

		this.trackArtist = new JLabel();
		this.trackArtist.setForeground(this.foregroundColor);
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		this.add(this.trackArtist, gbc);

		this.trackAlbum = new JLabel();
		this.trackAlbum.setForeground(this.foregroundColor);
		gbc.gridx = 1;
		gbc.gridy = 3;
		this.add(this.trackAlbum, gbc);

		this.format = new JLabel();
		this.format.setForeground(this.foregroundColor);
		gbc.gridy = 4;
		this.add(this.format, gbc);

		this.tags = new JLabel();
		this.tags.setForeground(this.foregroundColor);
		gbc.gridy = 5;
		this.add(this.tags, gbc);
	}

	/** Load information from service track. Not yet implemented.
	 * @param track
	 */
	private void loadFromService(Track track) {

	}

	/**
	 * Loads information from the local track
	 */
	private void loadFromLocal() {
		this.fileName.setText(localTrack.getFileName());
		this.trackName.setText(localTrack.getName());
		this.trackArtist.setText("by " + localTrack.getArtist());
		this.trackAlbum.setText("track #" + localTrack.getTrackNumber() + "on " + localTrack.getAlbum() + "(" + localTrack.getReleaseDate() + ")");
		this.format.setText("Length: " + localTrack.getFormattedLength() + " Format: " + localTrack.getFormat() + " "
				+ localTrack.getBitrate() + "kpbs");
	}

	/* (non-Javadoc)
	 * @see grabsalot.gui.GenericInfoPanel#getElementName()
	 */
	@Override
	public String getElementName() {
		return this.trackName.getText();
	}
}
