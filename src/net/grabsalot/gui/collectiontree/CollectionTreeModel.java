package net.grabsalot.gui.collectiontree;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import net.grabsalot.business.CollectionTreeNode;

public class CollectionTreeModel extends DefaultTreeModel {

	public CollectionTreeModel() {
		super(new CollectionTreeNode());
	}

	@Override
	public CollectionTreeNode getRoot() {
		return (CollectionTreeNode)root;
	}





}
