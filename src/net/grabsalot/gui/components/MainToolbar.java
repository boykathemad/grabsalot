package net.grabsalot.gui.components;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

/** The toolbar displayed on the MainFrame. All elements are added outside of this class.
 * @author madboyka
 *
 */
public class MainToolbar extends JToolBar {

	private static final long serialVersionUID = 864456501099638792L;

	private ActionListener buttonsActionListener;

	/** Invokes the super constuctor
	 * @param string
	 */
	public MainToolbar(String string) {
		super(string);
	}

	/** Sets the action listener that handles all button action events on this toolbar
	 * @param listener the actionlistener
	 */
	public void setActionsListener(ActionListener listener) {
		this.buttonsActionListener = listener;
	}

	/** Adds a JButton to this toolbar. It will be added to the end of the toolbar.
	 * @param button
	 */
	public void addButton(JButton button) {
		button.addActionListener(buttonsActionListener);
		this.add(button);
	}

	/** Adds a JButton to this toolbar and also specifies the actionCommand for it
	 * @param button the button to add
	 * @param actionCommand the actionCommand
	 */
	public void addButton(JButton button, String actionCommand) {
		button.setActionCommand(actionCommand);
		this.addButton(button);
	}

	/** Adds a button with the label buttonLabel and action command actionCommand
	 * @param buttonLabel label of the new button
	 * @param actionCommand the action command
	 */
	public void addButton(String buttonLabel, String actionCommand) {
		JButton button = new JButton(buttonLabel);
		button.setActionCommand(actionCommand);
		this.addButton(button);
	}
}
