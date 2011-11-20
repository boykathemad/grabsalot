package net.grabsalot.util;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.Toolkit;
import java.io.IOException;

import net.grabsalot.util.ClipBoard;

/**
 * Singleton clipboad utility
 * 
 * @author madboyka
 * 
 */
public final class ClipBoard implements ClipboardOwner {
	private static ClipBoard instance;

	/**
	 * Default singleton constructor, using the super classes constructor.
	 */
	private ClipBoard() {
		super();
	}

	/**
	 * Returns the instance of this singleton object
	 * 
	 * @return
	 */
	public static ClipBoard getInstance() {
		if (ClipBoard.instance == null) {
			ClipBoard.instance = new ClipBoard();
		}
		return ClipBoard.instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.datatransfer.ClipboardOwner#lostOwnership(java.awt.datatransfer
	 * .Clipboard, java.awt.datatransfer.Transferable)
	 */
	public void lostOwnership(Clipboard aClipboard, Transferable aContents) {
	}

	/**
	 * Place a text on the clipboard
	 * 
	 * @param text
	 */
	public void setClipboardContents(String text) {
		StringSelection stringSelection = new StringSelection(text);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, this);
	}

	/**
	 * Get the text from the clipboard.
	 * 
	 * @return text currently on clipboard, or an empty string otherwise
	 */
	public String getClipboardContents() {
		String result = "";
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = clipboard.getContents(null);
		boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		if (hasTransferableText) {
			try {
				result = (String) contents.getTransferData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException ex) {
				System.out.println(ex);
				ex.printStackTrace();
			} catch (IOException ex) {
				System.out.println(ex);
				ex.printStackTrace();
			}
		}
		return result;
	}
}
