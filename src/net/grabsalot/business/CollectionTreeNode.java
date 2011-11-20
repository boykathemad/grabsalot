package net.grabsalot.business;

import net.grabsalot.dao.local.LocalElement;

import net.grabsalot.business.IconFactory;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * The node element for the {@link JTree} that displays the collection contents.
 * It holds a {@link LocalElement} field that has most of the data needed.
 * 
 * @author madboyka
 * 
 */
public class CollectionTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 4977709942788805726L;

	protected Icon icon;
	protected String iconName;

	public CollectionTreeNode() {
		this(null);
	}

	public CollectionTreeNode(Object userObject) {
		this(userObject, true, null);
	}

	public CollectionTreeNode(Object userObject, boolean allowsChildren, Icon icon) {
		super(userObject, allowsChildren);
		this.icon = icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	/**
	 * Gets the icon to display in the tree.
	 * 
	 * @return the icon of this node.
	 */
	public Icon getIcon() {
		if (icon == null) {
			this.icon = IconFactory.getIconForNode(this);
		}
		return icon;
	}

	/**
	 * Return the iconname to use while displaying this node in a tree.
	 * 
	 * @return name of the icon
	 */
	public String getIconName() {
		if (iconName != null) {
			return iconName;
		} else {
			switch (this.getElementType()) {
			case LocalElement.COLLECTION_ELEMENT_TYPE:
				return "collection";
			case LocalElement.ARTIST_ELEMENT_TYPE:
				return "artist";
			case LocalElement.ALBUM_ELEMENT_TYPE:
				return "album";
			case LocalElement.TRACK_ELEMENT_TYPE:
				return "track";
			default:
				return null;
			}
		}
	}

	/**
	 * Sets the iconname to name.
	 * 
	 * @param name
	 *            the name to use
	 */
	public void setIconName(String name) {
		iconName = name;
	}

	/**
	 * Gets the type of the element held in this node.
	 * 
	 * @return an integer representing the element type, possible values are on
	 *         {@link LocalElement}
	 */
	public int getElementType() {
		return this.getElement().getElementType();
	}

	/**
	 * Gets the {@link LocalElement} object held by this node.
	 * 
	 * @return the element held by this node.
	 */
	public LocalElement getElement() {
		return (LocalElement) this.userObject;
	}

	/**
	 * Sets the {@literal element} held by this node to element
	 * 
	 * @param element
	 *            the new element
	 */
	public void setElement(Object element) {
		this.userObject = element;
	}
}