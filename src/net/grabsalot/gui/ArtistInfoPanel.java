package net.grabsalot.gui;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import net.grabsalot.business.Cacher;
import net.grabsalot.business.Logger;
import net.grabsalot.dao.local.LocalArtist;
import net.grabsalot.dao.service.lastfm.Artist;
import net.grabsalot.dao.service.lastfm.LastFmException;
import net.grabsalot.util.ClipBoard;
import net.grabsalot.util.LogoLoader;

import net.grabsalot.gui.GenericInfoPanel;
import net.grabsalot.gui.JImageBox;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/** Info panel that displays artist specific information.
 * @author madboyka
 *
 */
public class ArtistInfoPanel extends GenericInfoPanel {

	private static final long serialVersionUID = 6993466084731368234L;
	private final Dimension defaultSize = new Dimension(620, 260);

	private JLabel artistName, statistics, tags;
	private JImageBox logo, picture;
	private JTextPane bio;
	private JScrollPane scpBio;
	private JPanel panSimilarArtists;

	/**
	 * Default constructor
	 */
	public ArtistInfoPanel() {
		super();
		this.setupComponents();
		this.alignComponents();
	}

	/** Constructor using local artist
	 * @param artist
	 */
	public ArtistInfoPanel(LocalArtist artist) {
		this();
		loadFromLocal(artist);
	}

	/** Constructor using service artist
	 * @param artist
	 */
	public ArtistInfoPanel(Artist artist) {
		this();
		loadFromService(artist);
	}

	/* (non-Javadoc)
	 * @see grabsalot.gui.GenericInfoPanel#alignComponents()
	 */
	@Override
	protected void alignComponents() {
		this.picture.setVisible(!this.minimalMode);
		this.statistics.setVisible(!this.minimalMode);
		this.tags.setVisible(!this.minimalMode);
		this.scpBio.setVisible(!this.minimalMode);
		this.panSimilarArtists.setVisible(!this.minimalMode);
		if (this.minimalMode) {
			this.setPreferredSize(null);
			this.setMaximumSize(new Dimension(2000, Math.max(this.logo.getHeight(), this.artistName.getHeight())));
		} else {
			this.setMaximumSize(null);
			this.setPreferredSize(defaultSize);
		}
		this.revalidate();
	}

	/* (non-Javadoc)
	 * @see grabsalot.gui.GenericInfoPanel#setupComponents()
	 */
	@Override
	protected void setupComponents() {
		GridBagConstraints gbc = new GridBagConstraints();

		this.logo = new JImageBox();
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		this.add(this.logo, gbc);

		this.picture = new JImageBox();
		this.picture.setMinimumSize(new Dimension(252, this.defaultSize.height));
		this.picture.setPreferredSize(new Dimension(252, this.defaultSize.height));
		gbc.gridy = 1;
		gbc.gridheight = 2;
		this.add(this.picture, gbc);

		this.artistName = new JLabel();
		this.artistName.setForeground(this.foregroundColor);
		this.artistName.setFont(new Font("Segoe", Font.BOLD, 28));
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 2;
		gbc.weightx = 1;
		this.add(this.artistName, gbc);

		this.statistics = new JLabel();
		this.statistics.setForeground(this.foregroundColor);
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		this.add(this.statistics, gbc);

		this.tags = new JLabel("no tags");
		this.tags.setForeground(this.foregroundColor);
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.gridx = 2;
		this.add(this.tags, gbc);

		this.bio = new JTextPane();
		this.bio.setBorder(BorderFactory.createEmptyBorder());
		this.bio.setContentType("text/html");
		this.bio.setEditable(false);
		this.bio.setPreferredSize(new Dimension(400, 200));
		this.bio.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					ClipBoard.getInstance().setClipboardContents(e.getDescription());
					Cacher.getMainFrame().setStatusBarText("Link url copied to clipboard:" + e.getDescription());
					System.out.println("Link clicked:" + e.getSourceElement().toString());
					try {
						Desktop.getDesktop().browse(new URL(e.getDescription().toString()).toURI());
					} catch (Exception ex) {
						System.err.println(ex.getMessage());
						ex.printStackTrace();
					}
				}
			}
		});
		this.scpBio = new JScrollPane(this.bio);
		gbc.gridy = 2;
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(scpBio, gbc);

		this.panSimilarArtists = new JPanel();
		this.panSimilarArtists.setLayout(new GridLayout(1, 5));
		this.panSimilarArtists.setBackground(this.backgroundColor);
		this.panSimilarArtists.setPreferredSize(new Dimension(500, 180));
		this.panSimilarArtists.setToolTipText("Similar artists");
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridheight = 1;
		gbc.gridwidth = 3;
		gbc.weighty = 0;
		this.add(this.panSimilarArtists, gbc);
	}

	/** Load information from service artist
	 * @param artist
	 */
	private void loadFromService(Artist artist) {
		this.artistName.setText(artist.getName());
		this.statistics.setText(artist.getStatistics(false));
		this.bio.setText(artist.getBioContent());
		this.bio.setCaretPosition(0);
		this.tags.setText(artist.getTags());
		try {
			this.picture.setImage(artist.getImageUrl());
		} catch (NullPointerException ex) {
			Logger._().warning("Couldn't get artist image");
		}
		MouseListener similarLoader = new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				JImageBox src = (JImageBox)e.getSource();
				try {
					loadFromService(new Artist(src.getText()));
				} catch (LastFmException ex) {
					java.util.logging.Logger.getLogger(ArtistInfoPanel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		};
		panSimilarArtists.removeAll();
		try {
			Artist[] similar = artist.getSimilarArtists();
			for (Artist a : similar) {
				JImageBox artistBox = new JImageBox(a.getImageUrl("medium"));
				artistBox.setForeground(this.foregroundColor);
				artistBox.setVerticalTextPosition(JImageBox.BOTTOM);
				artistBox.setHorizontalTextPosition(JImageBox.CENTER);
				artistBox.setPreferredSize(new Dimension(200, 100));
				artistBox.setMinimumSize(new Dimension(200, 100));
				artistBox.setText(a.getName());
				artistBox.addMouseListener(similarLoader);
				artistBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				this.panSimilarArtists.add(artistBox);
			}
		} catch (Exception e) {
			Logger._().warning("ArtistInfoPanel:loadFromService: some error happened.");
		}
	}

	/** Load information from local artist
	 * @param artist
	 */
	private void loadFromLocal(LocalArtist artist) {
		try {
			this.logo.setImage(LogoLoader.getLogo(artist.getName()));
		} catch (Exception e) {
			Logger._().warning("ArtistInfoPanel:loadFromLocal: failed to load Logo.");
		}
		this.elementName = artist.getName();
		this.artistName.setText(artist.getName());
		Artist a = null;
		try {
			a = new Artist(artist.getName());
			this.loadFromService(a);
		} catch (LastFmException e) {
			bio.setText("An error occured, this artist probably does not exist!");
		}
		if (a == null) {
			this.artistName.setText(artist.getName());
		}
	}
}
