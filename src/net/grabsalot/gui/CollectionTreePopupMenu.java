package net.grabsalot.gui;

import net.grabsalot.business.Cacher;
import net.grabsalot.business.CollectionTreeNode;
import net.grabsalot.business.task.Task;
import net.grabsalot.business.task.TaskManager;
import net.grabsalot.dao.local.LocalAlbum;
import net.grabsalot.dao.local.LocalArtist;
import net.grabsalot.dao.local.LocalElement;
import net.grabsalot.dao.local.LocalTrack;
import net.grabsalot.util.AlbumArt;

import net.grabsalot.gui.MainFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Enumeration;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

/** Popup menu used on the collection tree element.
 * @author madboyka
 *
 */
public class CollectionTreePopupMenu extends JPopupMenu implements ActionListener {

	private static final long serialVersionUID = -5103252433999245500L;
	private JTree tree;
	private JMenuItem rename;
	private JMenuItem saveAlbumArt;
	private JMenuItem saveAllAlbumArtsForArtist;
	private JMenuItem saveAllAlbumArtsForCollection;
	private JMenuItem saveInfoToXml;
	private JMenuItem playTrack;
	private JMenuItem enqueueTrack;
	private JMenuItem playAlbum;
	private JMenuItem expandChildren;
	private JMenuItem expandToLeaves;
	private JMenuItem saveAllMetadata;
	private MainFrame mainFrame;
	private CollectionTreeNode overNode;

	/** Default constructor specifying the tree and the mainframe
	 * @param tree the tree
	 * @param mainFrame to be removed
	 */
	public CollectionTreePopupMenu(JTree tree, MainFrame mainFrame) {
		this.tree = tree;
		this.mainFrame = mainFrame;
		setMenuItems();
	}

	/**
	 * Creates and adds the menu elements according to the node under the cursor
	 */
	public final void setMenuItems() {
		int nodeType = 0;
		if (overNode != null) {
			nodeType = overNode.getElementType();
		}
		if (rename == null) {
			rename = new JMenuItem("Rename");
			rename.addActionListener(this);
			saveAlbumArt = new JMenuItem("Save album art");
			saveAlbumArt.addActionListener(this);
			saveAllAlbumArtsForArtist = new JMenuItem("Save all album arts for this artist");
			saveAllAlbumArtsForArtist.addActionListener(this);
			saveAllAlbumArtsForCollection = new JMenuItem("Save all album arts");
			saveAllAlbumArtsForCollection.addActionListener(this);
			saveInfoToXml = new JMenuItem("Save info to xml");
			saveInfoToXml.addActionListener(this);
			playTrack = new JMenuItem("Play this track");
			playTrack.addActionListener(this);
			enqueueTrack = new JMenuItem("Enqueue this track");
			enqueueTrack.addActionListener(this);
			playAlbum = new JMenuItem("Play this album");
			playAlbum.addActionListener(this);
			expandChildren = new JMenuItem("Expand all children");
			expandChildren.addActionListener(this);
			expandToLeaves = new JMenuItem("Expand everything");
			expandToLeaves.addActionListener(this);
			saveAllMetadata = new JMenuItem("Save all metadata");
			saveAllMetadata.addActionListener(this);
		}
		if (nodeType != 0 && nodeType != LocalElement.TRACK_ELEMENT_TYPE) {
			// this.add(saveInfoToXml);
			this.add(saveAllMetadata);
		}
		if (nodeType != LocalElement.COLLECTION_ELEMENT_TYPE) {
			this.add(rename);
		} else {
			this.add(expandChildren);
			this.add(expandToLeaves);
			this.add(saveAllAlbumArtsForCollection);
		}
		if (nodeType == LocalElement.ARTIST_ELEMENT_TYPE) {
			this.add(saveAllAlbumArtsForArtist);
		}
		if (nodeType == LocalElement.ALBUM_ELEMENT_TYPE) {
			this.add(saveAlbumArt);
			this.add(playAlbum);
		}
		if (nodeType == LocalElement.TRACK_ELEMENT_TYPE) {
			this.add(playTrack);
			this.add(enqueueTrack);
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
		if (e.getSource().equals(this.rename)) {
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
		if (e.getSource().equals(this.saveAlbumArt)) {
			try {
				File coverFile = ((LocalAlbum) overNode.getElement()).saveCover();
				Cacher.getMainFrame().setStatusBarText("Cover art saved as :" + coverFile.getAbsolutePath());
			} catch (Exception ex) {
				Cacher.getMainFrame().setStatusBarText("Failed to save cover art :" + ex.getMessage());
			}
		}
		if (e.getSource().equals(this.saveAllAlbumArtsForArtist)) {
			LocalAlbum[] albums = ((LocalArtist) overNode.getElement()).getAlbums();
			AlbumArt.grabAll(albums);
		}
		if (e.getSource().equals(this.saveAllAlbumArtsForCollection)) {
			AlbumArt.grabAll(null);
		}
		if (e.getSource().equals(this.playTrack)) {
			Cacher.getMainFrame().playTrack((LocalTrack) overNode.getElement());
		}
		if (e.getSource().equals(this.enqueueTrack)) {
			Cacher.getMainFrame().enqueueTrack((LocalTrack) overNode.getElement());
		}
		if (e.getSource().equals(this.playAlbum)) {
			Cacher.getMainFrame().playTracks(((LocalAlbum) overNode.getElement()).getTracks());
		}
		if (e.getSource().equals(this.expandChildren)) {
			Enumeration<CollectionTreeNode> children = overNode.children();
			while (children.hasMoreElements()) {
				tree.expandPath(new TreePath(children.nextElement().getPath()));
			}
		}
		if (e.getSource().equals(this.expandToLeaves)) {
			Enumeration<CollectionTreeNode> children = overNode.children();
			while (children.hasMoreElements()) {
				tree.expandPath(new TreePath(children.nextElement().getLastLeaf().getPath()));
			}
		}

		if (e.getSource().equals(this.saveAllMetadata)) {
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
