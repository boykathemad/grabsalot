package net.grabsalot.gui.controllers;

import net.grabsalot.gui.infopanels.GenericInfoPanel;
import net.grabsalot.gui.infopanels.AlbumInfoPanel;
import net.grabsalot.gui.infopanels.TrackInfoPanel;
import net.grabsalot.gui.infopanels.ArtistInfoPanel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;

import net.grabsalot.business.Cacher;
import net.grabsalot.business.CollectionTreeNode;
import net.grabsalot.business.task.Task;
import net.grabsalot.business.task.TaskEvent;
import net.grabsalot.business.task.TaskListener;
import net.grabsalot.business.task.TaskManager;
import net.grabsalot.dao.local.LocalAlbum;
import net.grabsalot.dao.local.LocalArtist;
import net.grabsalot.dao.local.LocalElement;
import net.grabsalot.dao.local.LocalTrack;
import net.grabsalot.gui.MainFrame;
import net.grabsalot.i18n.Translator;

/**
 *
 * @author madboyka
 */
public class DetailViewManager {

	private static EDetailViewMode detailViewMode = EDetailViewMode.InfoPanels;
	private static TaskListener loadInfoEventListener;
	private static JComponent[] infoPanelComponents;

	static {
		loadInfoEventListener = new TaskListener() {

			@Override
			public void taskComplete(TaskEvent e) {
				JPanel panInfo = MainFrame.getInstance().getInfoPanel();
				panInfo.removeAll();
				for (JComponent i : infoPanelComponents) {
					panInfo.add(i);
				}
				panInfo.revalidate();
				panInfo.repaint();
			}

			@Override
			public void taskError(TaskEvent e) {
				MainFrame.getInstance().getInfoPanel().removeAll();
			}
		};
	}

	private DetailViewManager() {
	}

	public static EDetailViewMode getDetailViewMode() {
		return detailViewMode;
	}

	private static void loadDetailsViewInfo(final CollectionTreeNode node) {
		Task task = new Task("loadSelectionInfoTask") {
			@Override
			public void run() {
				Object[][] data = null;
				Object[] columns = null;
				switch (node.getElementType()) {
					case LocalElement.ARTIST_ELEMENT_TYPE:
						columns = new Object[] {"Title"};
						LocalAlbum[] albums = ((LocalArtist) node.getElement()).getAlbums();
						data = new Object[albums.length][];
						int i = 0;
						for (LocalAlbum album : albums) {
							data[i++] = new Object[]{album.getName()};
						}
						break;
					case LocalElement.ALBUM_ELEMENT_TYPE:
						columns = new Object[] {"Title", "FileSize", "Bitrate", "Format"};
						LocalTrack[] tracks = ((LocalAlbum) node.getElement()).getTracks();
						data = new Object[tracks.length][];
						int j = 0;
						for (LocalTrack track : tracks) {
							data[j++] = new Object[]{track.getName(),track.getPath().length(), track.getBitrate(), track.getFormat()};
						}
						break;
				}
				JTable table = new JTable(data, columns);
				table.setFillsViewportHeight(true);
				infoPanelComponents = new JComponent[]{table};
			}
		};
		task.addTaskListener(loadInfoEventListener);
		TaskManager.getInstance().add(task);
		TaskManager.getInstance().start();
	}

	private static void loadServicePageInfo() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	private static void loadListViewInfo(final CollectionTreeNode node) {
		JList list = new JList();
		JTable table = new JTable();
//		table.addColumn(null);
		infoPanelComponents = new JComponent[]{list};

		Task task = new Task("loadSelectionInfoTask") {

			@Override
			public void run() {
			}
		};
		task.addTaskListener(loadInfoEventListener);
		TaskManager.getInstance().add(task);
		TaskManager.getInstance().start();
	}
	private GenericInfoPanel[] infoPanels;

	public static void setDetailViewMode(EDetailViewMode mode) {
		detailViewMode = mode;
	}

	/**
	 * Invokes the load info panel methods according to the specified node.
	 *
	 * @param node
	 *            the node to load the information panels for
	 */
	public static void loadSelectionInfo(final CollectionTreeNode node) {
		switch (detailViewMode) {
			case InfoPanels:
				loadInfoPanelsInfo(node);
				break;
			case Details:
				loadDetailsViewInfo(node);
			case List:
				loadListViewInfo(node);
				break;
			case ServicePage:
				loadServicePageInfo();
				break;
		}
	}

	private static void loadInfoPanelsInfo(final CollectionTreeNode node) {
		if (node.getElementType() == LocalElement.COLLECTION_ELEMENT_TYPE) return;
		Task task = new Task("loadSelectionInfoTask") {

			@Override
			public void run() {
				if (node.getElementType() == LocalElement.ARTIST_ELEMENT_TYPE) {
					infoPanelComponents = new GenericInfoPanel[1];
					loadArtistInfo(node, false);
					System.out.println("ARTIST LOADED");
				}
				if (node.getElementType() == LocalElement.ALBUM_ELEMENT_TYPE) {
					infoPanelComponents = new GenericInfoPanel[2];
					loadArtistInfo((CollectionTreeNode) node.getParent(), true);
					loadAlbumInfo(node, false);
				}
				if (node.getElementType() == LocalElement.TRACK_ELEMENT_TYPE) {
					infoPanelComponents = new GenericInfoPanel[3];
					loadArtistInfo((CollectionTreeNode) node.getParent().getParent(), true);
					loadAlbumInfo((CollectionTreeNode) node.getParent(), false);
					loadTrackInfo(node, false);
				}
			}
		};
		task.addTaskListener(loadInfoEventListener);
		TaskManager.getInstance().add(task);
		TaskManager.getInstance().start();
	}

	/**
	 * Loads the ArtistInfoPanel for the specified node.
	 *
	 * @param node
	 *            the node that holds the artist
	 * @param minimal
	 *            whether to show the panel in minimal or normal mode
	 */
	private static void loadArtistInfo(CollectionTreeNode node, boolean minimal) {
		MainFrame.getInstance().setStatusBarText(Translator.$("StatusText.LoadingArtist"));
		ArtistInfoPanel panel = (ArtistInfoPanel) Cacher.getItem("artistLastLoaded");
		if (panel == null || !((LocalArtist) node.getElement()).getName().equals(panel.getElementName())) {
			LocalArtist artist = (LocalArtist) node.getElement();
			panel = new ArtistInfoPanel(artist);
			Cacher.addItem("artistLastLoaded", panel);
		}
		panel.setMinimalMode(minimal);
		Cacher.addItem("artistLastLoaded", panel);
		infoPanelComponents[0] = panel;
		MainFrame.getInstance().setStatusBarIdle();
	}

	/**
	 * Load the AlbumInfoPanel for the specified node.
	 *
	 * @param node
	 *            the node that holds the album
	 * @param minimal
	 *            not used
	 */
	private static void loadAlbumInfo(CollectionTreeNode node, boolean minimal) {
		AlbumInfoPanel panel = (AlbumInfoPanel) Cacher.getItem("albumLastLoaded");
		if (panel == null || !((LocalAlbum) node.getElement()).getName().equals(panel.getElementName())) {
			LocalAlbum album = (LocalAlbum) node.getElement();
			panel = new AlbumInfoPanel(album);
			Cacher.addItem("albumLastLoaded", panel);
		}
		panel.setMinimalMode(minimal);
		infoPanelComponents[1] = panel;
	}

	/**
	 * Load the AlbumInfoPanel for the specified node.
	 *
	 * @param node
	 *            the node that holds the track
	 * @param minimal
	 *            not used
	 */
	private static void loadTrackInfo(CollectionTreeNode node, boolean minimal) {
		TrackInfoPanel panel = (TrackInfoPanel) Cacher.getItem("trackLastLoaded");
		if (panel == null || !((LocalTrack) node.getElement()).getName().equals(panel.getElementName())) {
			LocalTrack track = (LocalTrack) node.getElement();
			panel = new TrackInfoPanel(track);
			Cacher.addItem("trackLastLoaded", panel);
		}
		panel.setMinimalMode(minimal);
		infoPanelComponents[2] = panel;
	}
}
