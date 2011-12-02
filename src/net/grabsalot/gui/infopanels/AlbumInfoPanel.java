package net.grabsalot.gui.infopanels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Image;

import net.grabsalot.business.Logger;
import net.grabsalot.dao.local.LocalAlbum;
import net.grabsalot.dao.service.lastfm.Album;
import net.grabsalot.dao.service.lastfm.Artist;
import net.grabsalot.dao.service.lastfm.LastFmException;
import net.grabsalot.util.ImageUtil;

import net.grabsalot.gui.infopanels.GenericInfoPanel;
import net.grabsalot.gui.components.JImageBox;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import net.grabsalot.gui.components.JImageBox;

/**
 * Info panel that displays album specific information.
 *
 * @author madboyka
 *
 */
public class AlbumInfoPanel extends GenericInfoPanel {

	private static final long serialVersionUID = 6993466084731368234L;
	private final Dimension defaultSize = new Dimension(600, 300);

	private JLabel albumName, statistics, tags;
	private JImageBox frontCover;
	private JTextPane wiki;
	private JScrollPane scpBio;
	private Image fullCover;

	/**
	 * Default constructor.
	 */
	public AlbumInfoPanel() {
		super();
		this.setupComponents();
		this.alignComponents();
	}

	/**
	 * Constructor using a local album element.
	 *
	 * @param album
	 */
	public AlbumInfoPanel(LocalAlbum album) {
		this();
		loadFromLocal(album);
	}

	/**
	 * Constructor using a service album element.
	 *
	 * @param album
	 */
	public AlbumInfoPanel(Album album) {
		this();
		loadFromService(album);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see grabsalot.gui.GenericInfoPanel#alignComponents()
	 */
	@Override
	protected void alignComponents() {
		this.scpBio.setVisible(!this.minimalMode);
		if (this.minimalMode) {
			this.setPreferredSize(null);
			this.setMaximumSize(new Dimension(2000, 100));
			try {
				this.fullCover = frontCover.getImage();
				frontCover.setImage((ImageUtil.resize(((ImageIcon) frontCover.getIcon()).getImage(), 60, 60)));
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else {
			if (this.fullCover != null) {
				frontCover.setImage(this.fullCover);
				this.fullCover = null;
			}
			this.setPreferredSize(defaultSize);
			this.setMaximumSize(null);
		}
		this.revalidate();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see grabsalot.gui.GenericInfoPanel#setupComponents()
	 */
	@Override
	protected void setupComponents() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 0;
		gbc.weighty = 0;

		gbc.anchor = GridBagConstraints.FIRST_LINE_START;

		this.frontCover = new JImageBox();
		frontCover.setMaximumSize(new Dimension(300, 300));
		frontCover.setPreferredSize(new Dimension(300, 300));
		frontCover.setSize(new Dimension(300, 300));

		gbc.gridheight = 3;
		this.add(this.frontCover, gbc);
		this.albumName = new JLabel();
		this.albumName.setForeground(this.foregroundColor);
		this.albumName.setFont(new Font("Segoe", Font.BOLD, 24));
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 2;
		gbc.weightx = 1;
		this.add(this.albumName, gbc);

		this.statistics = new JLabel();
		this.statistics.setForeground(this.foregroundColor);
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		this.add(this.statistics, gbc);

		this.tags = new JLabel("no tags");
		this.tags.setForeground(this.foregroundColor);
		gbc.gridx = 2;
		this.add(this.tags, gbc);

		this.wiki = new JTextPane();
		this.wiki.setContentType("text/html");
		this.wiki.setEditable(false);
		this.wiki.setPreferredSize(new Dimension(400, 200));
		gbc.gridy = 2;
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		this.scpBio = new JScrollPane(this.wiki);
		this.add(scpBio, gbc);
	}

	/**
	 * Load information from service album
	 *
	 * @param album
	 */
	private void loadFromService(Album album) {
		this.albumName.setText(album.getName());
		this.statistics.setText(album.getStatistics(false));
		this.wiki.setText(album.getWikiSummary());
		this.tags.setText(album.getTags());
//		if (this.frontCover.getImage() == null) {
//			try {
//				this.frontCover.setImage(album.getCoverUrl());
//			} catch (NullPointerException ex) {
//				Logger._().warning("Couldn't get album image");
//			}
//		}
	}

	/**
	 * Load information from local album
	 *
	 * @param album
	 */
	private void loadFromLocal(LocalAlbum album) {
		this.elementName = album.getName();
		this.albumName.setText(album.getName());
		Image cover = album.getCover();
		if (cover != null) {
			this.frontCover.setImage(cover);
		}
		try {
			Album a = new Album(new Artist(album.getArtist().getName()), album.getName());
			this.albumName.setText(a.getName());
		} catch (LastFmException e) {
			Logger._().warning("AlbumInfoPanel:loadFromLocal:failed to load album, probably does not exist.");
		}
		this.elementName = album.getName();
		Album a = null;
		try {
			a = new Album(new Artist(album.getArtist().getName()), album.getName());
			this.loadFromService(a);
		} catch (LastFmException e) {
			wiki.setText("An error occured, this album probably does not exist!");
		}
	}
}
