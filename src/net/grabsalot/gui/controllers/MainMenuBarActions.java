package net.grabsalot.gui.controllers;

import net.grabsalot.core.Application;
import net.grabsalot.business.Cacher;
import net.grabsalot.business.Configuration;
import net.grabsalot.business.Constants;

import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import net.grabsalot.gui.AboutDialog;
import net.grabsalot.gui.LoadDialog;
import net.grabsalot.gui.components.MainMenuBar;
import net.grabsalot.gui.SettingsFrame;

public class MainMenuBarActions implements ActionListener {
	private MainMenuBar menuBar;

	public MainMenuBarActions(MainMenuBar menuBar) {
		this.menuBar = menuBar;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(Constants.MAINMENU_ACTION_SHOW_ABOUT)) {
			new AboutDialog();
		}

		if (e.getActionCommand().equals(Constants.MAINMENU_ACTION_SHOW_RULES)) {
			//Cacher.getMainFrame().scanTracks();
		}

		if (e.getActionCommand().equals(Constants.MAINMENU_ACTION_SHOW_SETTINGS)) {
			new SettingsFrame().setVisible(true);
		}

		if (e.getActionCommand().equals(Constants.MAINMENU_ACTION_LOAD)) {
			new LoadDialog();
		}

		if (e.getActionCommand().equals(Constants.MAINMENU_ACTION_RESTART)) {
			Application.restart();
		}

		if (e.getActionCommand().equals(Constants.MAINMENU_ACTION_HIDE_CONFIG)) {
			Configuration.getInstance().setProperty(Configuration.LOAD_LAST_WORKING_DIRECTORY,
					((JCheckBoxMenuItem) e.getSource()).isSelected());
		}
		if (e.getActionCommand().equals(Constants.MAINMENU_ACTION_VIEWMODE_CHANGED)) {
			DetailViewManager.setDetailViewMode(menuBar.getDetailView());
		}

		if (e.getActionCommand().equals(Constants.MAINMENU_ACTION_SHOW_GENRE_STATS)) {
//			new Statistics("genre");
		}

		if (e.getActionCommand().equals(Constants.MAINMENU_ACTION_SHOW_SIZE_STATS)) {
//			new Statistics("size");
		}
	}

}
