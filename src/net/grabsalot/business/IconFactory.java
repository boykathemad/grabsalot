package net.grabsalot.business;

import net.grabsalot.dao.local.LocalAlbum;
import net.grabsalot.dao.local.LocalArtist;
import net.grabsalot.dao.local.LocalElement;
import net.grabsalot.dao.local.LocalTrack;
import net.grabsalot.util.ImageUtil;

import net.grabsalot.business.CollectionTreeNode;
import net.grabsalot.business.Constants;
import net.grabsalot.business.IconFactory;
import net.grabsalot.business.Rule;
import net.grabsalot.business.RuleSet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.plaf.metal.MetalIconFactory;

/**
 * Icon factory used to create icons for collection tree nodes.
 *
 * @author madboyka
 *
 */
public class IconFactory {

	private static Rule iconDetailRule = RuleSet.getRule(RuleSet.iconDetailLevelRule);

	/**
	 * Returns the icon for the collection element
	 *
	 * @return
	 */
	public static Icon getCollectionIcon() {
		if (Rule.ICON_DETAIL_SIMPLE.equals(iconDetailRule.getValue())) {
			return MetalIconFactory.getTreeHardDriveIcon();
		}
		if (Rule.ICON_DETAIL_NICE.equals(iconDetailRule.getValue())) {
			return new ImageIcon(ImageUtil.loadFromResource(Constants.TREE_ICON_FILES[Constants.COLLECTION_ICON_ID]));
		}
		if (Rule.ICON_DETAIL_EXTENDED.equals(iconDetailRule.getValue())) {
			Image i = ImageUtil.loadFromResource(Constants.TREE_ICON_FILES[Constants.COLLECTION_ICON_ID]);
			return new ImageIcon(i);
		}
		return null;
	}

	/**
	 * Returns the icon for a specific artist node.
	 *
	 * @param node
	 * @return
	 */
	public static Icon getArtistIcon(LocalArtist node) {
		Icon icon = null;
		if (iconDetailRule.getValue() == Rule.ICON_DETAIL_SIMPLE) {
			icon = MetalIconFactory.getTreeFolderIcon();
		}
		if (Rule.ICON_DETAIL_NICE.equals(iconDetailRule.getValue())) {
			try {
				icon = new ImageIcon(ImageUtil.loadFromResource(Constants.TREE_ICON_FILES[Constants.ARTIST_ICON_ID]));
			} catch (Exception ex) {
				icon = MetalIconFactory.getTreeFolderIcon();
			}
		}
		if (iconDetailRule.getValue().equals(Rule.ICON_DETAIL_EXTENDED)) {
			Image i = ImageUtil.loadFromResource(Constants.TREE_ICON_FILES[Constants.ARTIST_ICON_ID]);
			Graphics g = i.getGraphics();
			g.setColor(Color.BLACK);
			g.setFont(new Font("Segoe", Font.BOLD, 12));
			g.drawString(node.getName().substring(0, 1), 1, g.getFont().getSize());
			System.err.println("Drawn");
			icon = new ImageIcon(i);
		}
		return icon;
	}

	/**
	 * Returns the icon for a specific album node.
	 *
	 * @param node
	 * @return
	 */
	public static Icon getAlbumIcon(LocalAlbum node) {
		Icon icon = null;
		if (iconDetailRule.getValue().equals(Rule.ICON_DETAIL_SIMPLE)) {
			icon = MetalIconFactory.getTreeFolderIcon();
		}
		if (iconDetailRule.getValue().equals(Rule.ICON_DETAIL_NICE)) {
			try {
				icon = new ImageIcon(ImageUtil.loadFromResource(Constants.TREE_ICON_FILES[Constants.ALBUM_ICON_ID]));
			} catch (Exception ex) {
				icon = MetalIconFactory.getTreeFolderIcon();
			}
		}
		if (iconDetailRule.getValue().equals(Rule.ICON_DETAIL_EXTENDED)) {
			Image i = ImageUtil.loadFromResource(Constants.TREE_ICON_FILES[Constants.ALBUM_ICON_ID]);
			// Graphics g = i.getGraphics();
			// g.setColor(Color.BLACK);
			// g.setFont(new Font("Segoe", Font.BOLD,12));
			// g.drawString(node.getName().substring(0, 1), 1,
			// g.getFont().getSize());
			// System.err.println("Drawn");
			icon = new ImageIcon(i);
		}
		return icon;
	}

	/**
	 * Return the icon for a specific track node.
	 *
	 * @param node
	 * @return
	 */
	public static Icon getTrackIcon(LocalTrack node) {
		Icon icon = null;
		if (iconDetailRule.getValue().equals(Rule.ICON_DETAIL_SIMPLE)) {
			icon = MetalIconFactory.getTreeLeafIcon();
		}
		if (iconDetailRule.getValue().equals(Rule.ICON_DETAIL_NICE)) {
			try {
				icon = new ImageIcon(ImageUtil.loadFromResource(Constants.TREE_ICON_FILES[Constants.TRACK_ICON_ID]));
			} catch (Exception ex) {
				icon = MetalIconFactory.getTreeLeafIcon();
			}
		}
		if (iconDetailRule.getValue().equals(Rule.ICON_DETAIL_EXTENDED)) {
			Image i = ImageUtil.loadFromResource(Constants.TREE_ICON_FILES[Constants.TRACK_ICON_ID]);
			Graphics g = i.getGraphics();
			g.setColor(Color.BLACK);
			g.setFont(new Font("Segoe", Font.BOLD, 12));
			g.drawString(node.getFormat(), 1, g.getFont().getSize());
			icon = new ImageIcon(i);
		}
		return icon;
	}

	/**
	 * Returns an icon according to the node value and type.
	 *
	 * @param node
	 * @return
	 */
	public static Icon getIconForNode(CollectionTreeNode node) {
		IconFactory.iconDetailRule = RuleSet.getRule(RuleSet.iconDetailLevelRule);
		switch (node.getElementType()) {
			case LocalElement.COLLECTION_ELEMENT_TYPE:
				return IconFactory.getCollectionIcon();
			case LocalElement.ARTIST_ELEMENT_TYPE:
				return IconFactory.getArtistIcon((LocalArtist) node.getElement());
			case LocalElement.ALBUM_ELEMENT_TYPE:
				return IconFactory.getAlbumIcon((LocalAlbum) node.getElement());
			case LocalElement.TRACK_ELEMENT_TYPE:
				return IconFactory.getTrackIcon((LocalTrack) node.getElement());
		}
		return null;
	}
}
