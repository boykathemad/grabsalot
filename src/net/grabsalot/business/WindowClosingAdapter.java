package net.grabsalot.business;

import net.grabsalot.core.Application;
import net.grabsalot.gui.MainFrame;

import net.grabsalot.business.Cacher;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * A window listener that checks the state of frames, and acts accordingly.
 *
 * @author madboyka
 *
 */
public class WindowClosingAdapter extends WindowAdapter {

	/**
	 * Checks if the close action should terminate the application.
	 *
	 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		if (Cacher.getMainFrame() == null || !Cacher.getMainFrame().isVisible()
				|| e.getSource().getClass() == MainFrame.class) {
			Application.close(e.getSource().getClass() == MainFrame.class);
		}
	}

}
