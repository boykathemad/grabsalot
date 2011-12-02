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
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import net.grabsalot.business.CollectionLoader;
import net.grabsalot.business.task.Task;
import net.grabsalot.business.task.TaskListener;
import net.grabsalot.business.task.TaskManager;
import net.grabsalot.gui.components.LocalizableComponent;
import net.grabsalot.gui.MainFrame;

public class CollectionTreePanel extends JPanel implements LocalizableComponent {

	private MainFrame container;
	private JTextField txtSearch;
	private volatile JTree lstCollection;
	private volatile CollectionTreeNode treeRoot;
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
		treeRoot = new CollectionTreeNode(null);
		lstCollection = new JTree(treeRoot);
		treePopupMenu = new CollectionTreePopupMenu(lstCollection, this.container);
		lstCollection.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				//container.loadSelectionInfo((CollectionTreeNode) e.getPath().getLastPathComponent());
			}
		});
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
				if (e.getClickCount() == 1) {
//					container.loadSelectionInfo((CollectionTreeNode) lstCollection.getLastSelectedPathComponent());
				}
				if (e.getButton() == MouseEvent.BUTTON1) {
//					lstCollection.setSelectionRow(lstCollection.getClosestRowForLocation(e.getX(), e.getY()));
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
				treeRoot.add(((CollectionLoader)e.getTaskObject()).getResult());
				container.setStatusBarIdle();
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
//		this.treeRoot.removeAllChildren();
	}

	@Override
	public void updateLabels() {
		treePopupMenu.updateLabels();
	}
}
