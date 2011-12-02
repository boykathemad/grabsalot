package net.grabsalot.gui.collectiontree;

import net.grabsalot.business.CollectionTreeNode;

import java.awt.Component;
import java.util.HashMap;
import java.util.Hashtable;

import java.util.Map;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * The default renderer for the tree nodes. Only necessary to render the icons
 * accordingly.
 *
 * @author madboyka
 *
 */
class IconNodeRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = -918607865765619611L;

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		Icon icon = ((CollectionTreeNode) value).getIcon();
		if (icon == null) {
			Map<String, Icon> icons = (HashMap<String, Icon>) tree.getClientProperty("JTree.icons");
			String name = ((CollectionTreeNode) value).getIconName();
			if ((icons != null) && (name != null)) {
				icon = (Icon) icons.get(name);
				if (icon != null) {
					setIcon(icon);
				}
			}
		} else {
			setIcon(icon);
		}

		return this;
	}

}