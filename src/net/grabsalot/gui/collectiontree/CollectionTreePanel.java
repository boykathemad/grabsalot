package net.grabsalot.gui.collectiontree;

import java.awt.event.KeyEvent;
import net.grabsalot.business.CollectionTreeNode;
import net.grabsalot.business.task.TaskEvent;
import net.grabsalot.dao.local.LocalArtist;
import net.grabsalot.dao.local.LocalCollection;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
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
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import net.grabsalot.business.CollectionLoader;
import net.grabsalot.business.task.Task;
import net.grabsalot.business.task.TaskListener;
import net.grabsalot.business.task.TaskManager;
import net.grabsalot.dao.local.LocalElement;
import net.grabsalot.gui.components.LocalizableComponent;
import net.grabsalot.gui.MainFrame;

public class CollectionTreePanel extends JPanel implements LocalizableComponent {

	private MainFrame container;
	private JTextField txtSearch;
	private volatile JTree lstCollection;
	private volatile CollectionTreeNode treeRoot;
	private CollectionTreePopupMenu treePopupMenu;
	private CollectionTreeModel treeModel;
	private String lngTypeToSearch = "Type to search...";

	public CollectionTreePanel(MainFrame container) {
		this.setLayout(new BorderLayout());
		this.container = container;

		txtSearch = new JTextField();
		txtSearch.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				if ("".equals(txtSearch.getText())) {
					txtSearch.setText(lngTypeToSearch);
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				if (lngTypeToSearch.equals(txtSearch.getText())) {
					txtSearch.setText("");
				}
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
		treeModel = new CollectionTreeModel();
		treeRoot = treeModel.getRoot();
		lstCollection = new JTree(treeModel);
		treePopupMenu = new CollectionTreePopupMenu(lstCollection, this.container);
		lstCollection.setExpandsSelectedPaths(false);
		lstCollection.setRootVisible(false);
		lstCollection.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		lstCollection.setCellRenderer(new IconNodeRenderer());
		lstCollection.setRowHeight(32);
		lstCollection.setDragEnabled(true);
		lstCollection.setLargeModel(true);
		lstCollection.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				container.loadSelectionInfo((CollectionTreeNode) e.getNewLeadSelectionPath().getLastPathComponent());
			}
		});
		lstCollection.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 525) {
					CollectionTreeNode selection = (CollectionTreeNode) lstCollection.getLastSelectedPathComponent();
					Rectangle bounds = lstCollection.getRowBounds(lstCollection.getSelectionRows()[0]).getBounds();
//					lstCollection.getRowForPath(selection)).getLocation();
					treePopupMenu.show(selection, bounds.x, bounds.y + bounds.height);
				}
			}
		});
		lstCollection.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					treePopupMenu.show((CollectionTreeNode) lstCollection.getClosestPathForLocation(e.getX(), e.getY()).getLastPathComponent(), e.getX(), e.getY());
					return;
				}
			}
		});

		this.add(new JScrollPane(lstCollection));
	}

	public void loadCollection(final LocalCollection manager) {
		container.setStatusBarText("Loading collection...");
		Task loader = new CollectionLoader(manager);
		loader.addTaskListener(new TaskListener() {

			@Override
			public void taskError(TaskEvent e) {
				container.setStatusBarText("Failed to load collection...");
			}

			@Override
			public void taskComplete(TaskEvent e) {
//				myTreeModel.insertNodeInto(newChildNode, parentNode, index);
//myTreeModel.
//myTreeModel.setRoot(newRootNode);
				treeModel.insertNodeInto(((CollectionLoader) e.getTaskObject()).getResult(), treeRoot,0);
//				treeRoot.add();
				container.setStatusBarIdle();
				refreshTree();
				lstCollection.scrollPathToVisible(new TreePath(((DefaultMutableTreeNode) treeRoot.getFirstChild()).getPath()));
			}
		});
		TaskManager.getInstance().add(loader);
		TaskManager.getInstance().start();
	}

	/**
	 * Selects the first node thats name matches the specified filter. Matching
	 * is case sensitive.
	 *
	 * @param filter
	 *            the string to matched the node names against
	 */
	public void filterTreeNodes(String filter) {
		filterTreeNodes(treeRoot, filter);
//		refreshTree();
	}

	private void filterTreeNodes(CollectionTreeNode node, String filter) {
		CollectionTreeNode i;
		Enumeration<DefaultMutableTreeNode> en = node.children();
		while (en.hasMoreElements()) {
			i = (CollectionTreeNode) en.nextElement();
			switch (i.getElementType()) {
				case LocalElement.COLLECTION_ELEMENT_TYPE:
					filterTreeNodes(i, filter);
					break;
				case LocalElement.ARTIST_ELEMENT_TYPE:
					if (((LocalArtist) i.getElement()).getName().startsWith(filter)) {
						lstCollection.expandPath(new TreePath(i.getPath()));
					} else {
						treeModel.removeNodeFromParent(i);
					}
//					lstCollection.scrollPathToVisible(new TreePath(i.getPath()));
					break;
				case LocalElement.ALBUM_ELEMENT_TYPE:

					break;
				case LocalElement.TRACK_ELEMENT_TYPE:

					break;
			}
		}
	}

	public void refreshTree() {
		lstCollection.setModel(new DefaultTreeModel(null));
		lstCollection.setModel(treeModel);
//		lstCollection.revalidate();
//		lstCollection.repaint();
	}

	public void clear() {
//		this.treeRoot.removeAllChildren();
	}

	@Override
	public void updateLabels() {
		treePopupMenu.updateLabels();
	}
}
