package net.grabsalot.business;

import net.grabsalot.gui.LoadFrame;
import net.grabsalot.gui.RulesFrame;

import net.grabsalot.business.Cacher;
import net.grabsalot.business.Constants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** The ActionListener for the MainToolbar.
 * @author madboyka
 * @see ActionListener
 */
public class MainToolbarActions implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "dsa") {
			LoadFrame config = (LoadFrame)Cacher.getItem("frmConfigFrame");
			if (config == null) {
				config = new LoadFrame();
			}
			config.setVisible(true);
		} else if (e.getActionCommand() == Constants.MAINMENU_ACTION_SHOW_RULES) {
			RulesFrame rules = (RulesFrame)Cacher.getItem("frmRulesFrame");
			if (rules == null) {
				rules = new RulesFrame();
			}
			rules.setVisible(true);
		} else if (true) {

		}
	}

}
