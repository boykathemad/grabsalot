package net.grabsalot.gui.components;

import javax.swing.JLabel;
import javax.swing.JMenuBar;

/** A simple implementation of a Statusbar using {@link JMenuBar}.
 * @author madboyka
 *
 */
public class JStatusBar extends JMenuBar {

	private static final long serialVersionUID = -9221710760107966096L;

	private JLabel label;

	/**
	 * Default constructor, that sets up the toolbar.
	 */
	public JStatusBar() {
		super();
		this.label = new JLabel();
		this.add(this.label);
	}

	@Override
	public void removeAll() {
		super.removeAll();
		this.add(this.label);
	}

	/** Changed the statusbars text to text.
	 * @param text
	 */
	public void setText(String text) {
		this.label.setText(text);
	}

}
