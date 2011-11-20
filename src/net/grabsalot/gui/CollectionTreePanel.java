/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.grabsalot.gui;

import net.grabsalot.business.CollectionTreeNode;
import net.grabsalot.dao.local.LocalAlbum;
import net.grabsalot.dao.local.LocalArtist;
import net.grabsalot.dao.local.LocalCollection;
import net.grabsalot.dao.local.LocalTrack;

import net.grabsalot.gui.CollectionTreePopupMenu;
import net.grabsalot.gui.IconNodeRenderer;
import net.grabsalot.gui.MainFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author madboyka
 */
public class CollectionTreePanel extends JPanel {

	private MainFrame container;
	private JTextField txtSearch;
	private JTree lstCollection;
	private CollectionTreeNode treeRoot;
	private CollectionTreePopupMenu treePopupMenu;

	public CollectionTreePanel(MainFrame container) {
		this.setLayout(new BorderLayout());
		this.container = container;

		txtSearch = new JTextField();
		txtSearch.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				txtSearch.setText("Type to search...");
			}

			@Override
			public void focusGained(FocusEvent e) {
				txtSearch.setText("");
			}
		});
		txtSearch.setMaximumSize(new Dimension(12000, 30));
		this.add(txtSearch, BorderLayout.NORTH);
		txtSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				filterTreeNodes(e.getActionCommand());
			}
		});
		setupTree();
	}

	private void setupTree() {
		treeRoot = new CollectionTreeNode(new LocalCollection(this.container.getSource()));
		lstCollection = new JTree(treeRoot);
		treePopupMenu = new CollectionTreePopupMenu(lstCollection, this.container);
		lstCollection.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				//container.loadSelectionInfo((CollectionTreeNode) e.getPath().getLastPathComponent());
			}
		});
		lstCollection.setExpandsSelectedPaths(false);
		lstCollection.setRootVisible(true);
		lstCollection.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		lstCollection.setCellRenderer(new IconNodeRenderer());
		lstCollection.setRowHeight(32);
		lstCollection.setDragEnabled(true);
		lstCollection.setLargeModel(true);
		lstCollection.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getClickCount() == 2) {
					container.loadSelectionInfo((CollectionTreeNode) lstCollection.getLastSelectedPathComponent());
				}
				if (e.isPopupTrigger()) {
					treePopupMenu.show((CollectionTreeNode) lstCollection.getClosestPathForLocation(e.getX(), e.getY()).getLastPathComponent(), e.getX(), e.getY());
				}
				if (e.getButton() == MouseEvent.BUTTON1) {
					//lstCollection.setSelectionRow(lstCollection.getClosestRowForLocation(e.getX(), e.getY()));
				}
			}
		});

		this.add(new JScrollPane(lstCollection));
	}

	public void loadCollection(LocalCollection manager) {
		treeRoot.setUserObject(manager);
		LocalArtist[] artists = manager.getArtists();
		this.container.setStatusBarText("Loading collection...");
		for (int i = 0; i < artists.length; ++i) {
			CollectionTreeNode artistNode = new CollectionTreeNode(artists[i]);
			treeRoot.add(artistNode);
			LocalAlbum[] albums = artists[i].getAlbums();
			for (int j = 0; j < albums.length; ++j) {
				CollectionTreeNode albumNode = new CollectionTreeNode(albums[j]);
				artistNode.add(albumNode);
				LocalTrack[] tracks = albums[j].getTracks();
				for (int k = 0; k < tracks.length; ++k) {
					CollectionTreeNode trackNode = new CollectionTreeNode(tracks[k]);
					albumNode.add(trackNode);
				}
			}
		}
		lstCollection.scrollPathToVisible(new TreePath(((DefaultMutableTreeNode) treeRoot.getFirstChild()).getPath()));
		this.container.setStatusBarIdle();
	}

	/**
	 * Selects the first node thats name matches the specified filter. Matching
	 * is case sensitive.
	 *
	 * @param filter
	 *            the string to matched the node names against
	 */
	@SuppressWarnings("unchecked")
	public void filterTreeNodes(String filter) {
		CollectionTreeNode i;
		Enumeration<DefaultMutableTreeNode> en = treeRoot.children();
		while (en.hasMoreElements()) {
			i = (CollectionTreeNode) en.nextElement();
			if (((LocalArtist) i.getElement()).getName().startsWith(filter)) {
				lstCollection.expandPath(new TreePath(i.getPath()));
				lstCollection.scrollPathToVisible(new TreePath(i.getPath()));
			} else {
			}
		}
	}

	public void clear() {
		this.treeRoot.removeAllChildren();
	}
}
