package net.grabsalot.gui;

import java.util.Arrays;
import net.grabsalot.business.Cacher;
import net.grabsalot.business.CollectionTreeNode;
import net.grabsalot.business.task.Task;
import net.grabsalot.business.task.TaskManager;
import net.grabsalot.dao.local.LocalAlbum;
import net.grabsalot.dao.local.LocalArtist;
import net.grabsalot.dao.local.LocalElement;
import net.grabsalot.dao.local.LocalTrack;
import net.grabsalot.util.AlbumArt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;

import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import net.grabsalot.i18n.Translator;

/** Pop-up menu used on the collection tree element.
 * @author madboyka
 *
 */
public class CollectionTreePopupMenu extends JPopupMenu implements ActionListener, LocalizableComponent {

	private static final long serialVersionUID = -5103252433999245500L;
	private JTree tree;
	private JMenuItem iRename;
	private JMenuItem iSaveAlbumArt;
	private JMenuItem iSaveAllAlbumArtsForArtist;
	private JMenuItem iSaveAllAlbumArtsForCollection;
	private JMenuItem iSaveInfoToXml;
	private JMenuItem iPlayTrack;
	private JMenuItem iEnqueueTrack;
	private JMenuItem iPlayAllAlbums;
	private JMenuItem iPlayAlbum;
	private JMenuItem iExpandChildren;
	private JMenuItem iExpandToLeaves;
	private JMenuItem iSaveAllMetadata;
	private MainFrame mainFrame;
	private CollectionTreeNode overNode;

	/** Default constructor specifying the tree and the mainframe
	 * @param tree the tree
	 * @param mainFrame to be removed
	 */
	public CollectionTreePopupMenu(JTree tree, MainFrame mainFrame) {
		this.tree = tree;
		this.mainFrame = mainFrame;
		setupComponents();
	}

	private void setupComponents() {
		iRename = new JMenuItem();
		iRename.addActionListener(this);
		iSaveAlbumArt = new JMenuItem();
		iSaveAlbumArt.addActionListener(this);
		iSaveAllAlbumArtsForArtist = new JMenuItem();
		iSaveAllAlbumArtsForArtist.addActionListener(this);
		iSaveAllAlbumArtsForCollection = new JMenuItem();
		iSaveAllAlbumArtsForCollection.addActionListener(this);
		iSaveInfoToXml = new JMenuItem();
		iSaveInfoToXml.addActionListener(this);
		iPlayTrack = new JMenuItem();
		iPlayTrack.addActionListener(this);
		iEnqueueTrack = new JMenuItem();
		iEnqueueTrack.addActionListener(this);
		iPlayAllAlbums = new JMenuItem();
		iPlayAllAlbums.addActionListener(this);
		iPlayAlbum = new JMenuItem();
		iPlayAlbum.addActionListener(this);
		iExpandChildren = new JMenuItem();
		iExpandChildren.addActionListener(this);
		iExpandToLeaves = new JMenuItem();
		iExpandToLeaves.addActionListener(this);
		iSaveAllMetadata = new JMenuItem();
		iSaveAllMetadata.addActionListener(this);
		updateLabels();
	}

	@Override
	public void updateLabels() {
		iRename.setText(Translator.$("collectionMenu.rename"));
		iSaveAlbumArt.setText(Translator.$("collectionMenu.saveAlbumArt"));
		iSaveAllAlbumArtsForArtist.setText(Translator.$("collectionMenu.saveAllAlbumArts"));
		iSaveAllAlbumArtsForCollection.setText(Translator.$("collectionMenu.saveAllAlbumArts"));
		iSaveInfoToXml.setText(Translator.$("collectionMenu.saveMetadata"));
		iPlayTrack.setText(Translator.$("collectionMenu.playTrack"));
		iEnqueueTrack.setText(Translator.$("collectionMenu.enqueueTrack"));
		iPlayAllAlbums.setText(Translator.$("collectionMenu.playAllAlbums"));
		iPlayAlbum.setText(Translator.$("collectionMenu.playThisAlbum"));
		iExpandChildren.setText(Translator.$("collectionMenu.expandChildren"));
		iExpandToLeaves.setText(Translator.$("collectionMenu.expandAll"));
		iSaveAllMetadata.setText(Translator.$("collectionMenu.saveAllMetadata"));
	}

	/**
	 * Creates and adds the menu elements according to the node under the cursor
	 */
	public final void setMenuItems() {
		int nodeType = 0;
		if (overNode != null) {
			nodeType = overNode.getElementType();
		}
		if (nodeType != 0 && nodeType != LocalElement.TRACK_ELEMENT_TYPE) {
			// this.add(saveInfoToXml);
			this.add(iSaveAllMetadata);
		}
		if (nodeType != LocalElement.COLLECTION_ELEMENT_TYPE) {
			this.add(iRename);
		} else {
			this.add(iExpandChildren);
			this.add(iExpandToLeaves);
			this.add(iSaveAllAlbumArtsForCollection);
		}
		if (nodeType == LocalElement.ARTIST_ELEMENT_TYPE) {
			this.add(iSaveAllAlbumArtsForArtist);
			this.add(iPlayAllAlbums);
		}
		if (nodeType == LocalElement.ALBUM_ELEMENT_TYPE) {
			this.add(iSaveAlbumArt);
			this.add(iPlayAlbum);
		}
		if (nodeType == LocalElement.TRACK_ELEMENT_TYPE) {
			this.add(iEnqueueTrack);
			this.add(iPlayTrack);
		}
	}

	/**
	 * Removes all menu items and reinserts them.
	 */
	private void resetMenuItems() {
		this.removeAll();
		this.setMenuItems();
	}

	/** Pops up the menu at the specified x and y coordinates and sets the node under the cursor.
	 * @param node
	 * @param x
	 * @param y
	 */
	public void show(CollectionTreeNode node, int x, int y) {
		this.overNode = node;
		this.resetMenuItems();
		super.show(tree, x, y);
	}

	/** Actions used by menu items
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.overNode == null) {
			return;
		}
		if (e.getSource().equals(this.iRename)) {
			String newName = JOptionPane.showInputDialog("Enter new name:", this.overNode.getElement().getPath().getName());
			if (newName == null) {
				return;
			}
			File newFile = new File(this.overNode.getElement().getPath().getParentFile(), newName);
			if (this.overNode.getElement().getPath().renameTo(newFile)) {
				this.overNode.getElement().setPath(newFile);
				this.tree.revalidate();
				this.tree.repaint();
				this.mainFrame.loadSelectionInfo(this.overNode);
				Cacher.getMainFrame().setStatusBarText(
						"Element renamed. Due to a bug sub elements won't be valid. Please rescan the collection.");
			}
		}
		if (e.getSource().equals(this.iSaveAlbumArt)) {
			try {
				File coverFile = ((LocalAlbum) overNode.getElement()).saveCover();
				Cacher.getMainFrame().setStatusBarText("Cover art saved as :" + coverFile.getAbsolutePath());
			} catch (Exception ex) {
				Cacher.getMainFrame().setStatusBarText("Failed to save cover art :" + ex.getMessage());
			}
		}
		if (e.getSource().equals(this.iSaveAllAlbumArtsForArtist)) {
			LocalAlbum[] albums = ((LocalArtist) overNode.getElement()).getAlbums();
			AlbumArt.grabAll(albums);
		}
		if (e.getSource().equals(this.iSaveAllAlbumArtsForCollection)) {
			AlbumArt.grabAll(null);
		}
		if (e.getSource().equals(this.iPlayTrack)) {
			Cacher.getMainFrame().playTrack((LocalTrack) overNode.getElement());
		}
		if (e.getSource().equals(this.iEnqueueTrack)) {
			Cacher.getMainFrame().enqueueTrack((LocalTrack) overNode.getElement());
		}
		if (e.getSource().equals(this.iPlayAlbum)) {
			Cacher.getMainFrame().playTracks(((LocalAlbum) overNode.getElement()).getTracks());
		}
		if (e.getSource().equals(this.iPlayAllAlbums)) {
			LocalArtist artist = (LocalArtist) overNode.getElement();
			List<LocalTrack> tracks = new ArrayList<LocalTrack>();
			for (LocalAlbum album : artist.getAlbums()) {
				tracks.addAll(Arrays.asList(album.getTracks()));
			}
			MainFrame.getInstance().playTracks(tracks.toArray(new LocalTrack[0]));
		}
		if (e.getSource().equals(this.iExpandChildren)) {
			Enumeration<CollectionTreeNode> children = overNode.children();
			while (children.hasMoreElements()) {
				tree.expandPath(new TreePath(children.nextElement().getPath()));
			}
		}
		if (e.getSource().equals(this.iExpandToLeaves)) {
			Enumeration<CollectionTreeNode> children = overNode.children();
			while (children.hasMoreElements()) {
				tree.expandPath(new TreePath(children.nextElement().getLastLeaf().getPath()));
			}
		}

		if (e.getSource().equals(this.iSaveAllMetadata)) {
			Task task = new Task() {

				@Override
				public void run() {
					MainFrame.getInstance().setStatusBarText("Scanning file and saving metadata...");
					overNode.getElement().saveMetadataToFile(true);
					MainFrame.getInstance().setStatusBarText("Saved all metadata.");
				}
			};
			TaskManager.getInstance().add(task);
			TaskManager.getInstance().start();
		}
	}
}
