package net.grabsalot.gui;

import java.awt.event.ActionListener;
import java.util.HashMap;

import java.util.List;
import net.grabsalot.business.Constants;
import net.grabsalot.dao.service.lastfm.LastFmMenu;
import net.grabsalot.i18n.Translator;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

public class MainMenuBar extends JMenuBar {
	private static final long serialVersionUID = 7739958073232720853L;

	private ActionListener actionListener;
	private ButtonGroup detailViewGroup;
	private HashMap<ButtonModel,EDetailViewMode> detailViewMap;

	public MainMenuBar() {
		actionListener = new MainMenuBarActions(this);
		this.add(Box.createHorizontalGlue());
		this.add(this.getFileMenu());
		this.add(this.getViewMenu());
		this.add(this.getToolsMenu());
		this.add(new LastFmMenu());
		this.add(this.getHelpMenu());
	}

	private JMenu getFileMenu() {
		JMenu menu = new JMenu(Translator.$("Menu.File"));
		JMenuItem miLoad = new JMenuItem(Translator.$("Menu.Load"));
		miLoad.setActionCommand(Constants.MAINMENU_ACTION_LOAD);
		miLoad.addActionListener(actionListener);
		menu.add(miLoad);
		JMenuItem miRestart = new JMenuItem(Translator.$("Menu.Restart"));
		miRestart.setActionCommand(Constants.MAINMENU_ACTION_RESTART);
		miRestart.addActionListener(actionListener);
		menu.add(miRestart);
		return menu;
	}

	private JMenu getToolsMenu() {
		JMenu menu = new JMenu(Translator.$("Menu.Options"));

		JMenu mnuLanguages = new JMenu(Translator.$("Menu.Languages"));
		List<String> languages = Translator.getAvailableLanguages();
		for (String language : languages) {
			JMenuItem item = new JMenuItem(Translator.$("Language."+language));
			item.setName(language);
			mnuLanguages.add(item);
		}
		menu.add(mnuLanguages);

		JCheckBoxMenuItem miHideConfig = new JCheckBoxMenuItem(Translator.$("Menu.HideConfig"));
		miHideConfig.setActionCommand(Constants.MAINMENU_ACTION_HIDE_CONFIG);
		miHideConfig.addActionListener(actionListener);
		menu.add(miHideConfig);

		JMenuItem miSettings = new JMenuItem(Translator.$("Menu.Settings"));
		miSettings.setActionCommand(Constants.MAINMENU_ACTION_SHOW_SETTINGS);
		miSettings.addActionListener(actionListener);
		menu.add(miSettings);

		JMenuItem miRules = new JMenuItem(Translator.$("Menu.Rules"));
		miRules.setActionCommand(Constants.MAINMENU_ACTION_SHOW_RULES);
		miRules.addActionListener(actionListener);
		menu.add(miRules);

		return menu;
	}

	@Override
	public final JMenu getHelpMenu() {
		JMenu menu = new JMenu(Translator.$("Menu.Help"));

		JMenuItem miAbout = new JMenuItem(Translator.$("Menu.About"));
		miAbout.setActionCommand(Constants.MAINMENU_ACTION_SHOW_ABOUT);
		miAbout.addActionListener(actionListener);

		menu.add(miAbout);
		return menu;
	}

	public final JMenu getViewMenu() {
		JMenu menu = new JMenu(Translator.$("Menu.View"));

		JMenuItem miInfoDetailView = new JMenu(Translator.$("Menu.View.DetailView"));
		detailViewGroup = new ButtonGroup();
		detailViewMap = new HashMap<ButtonModel, EDetailViewMode>();
		for (EDetailViewMode mode : EDetailViewMode.values()) {
			JMenuItem modeMenu = new JRadioButtonMenuItem(Translator.$(mode.toString()));
			if (DetailViewManager.getDetailViewMode() == mode) {
				modeMenu.setSelected(true);
			}
			modeMenu.setActionCommand(Constants.MAINMENU_ACTION_VIEWMODE_CHANGED);
			modeMenu.addActionListener(actionListener);
			detailViewMap.put(modeMenu.getModel(), mode);
			detailViewGroup.add(modeMenu);
			miInfoDetailView.add(modeMenu);
		}
		menu.add(miInfoDetailView);

		JMenuItem miShowStats = new JMenuItem(Translator.$("Menu.View.ShowGenreStats"));
		miShowStats.setActionCommand(Constants.MAINMENU_ACTION_SHOW_GENRE_STATS);
		miShowStats.addActionListener(actionListener);
		menu.add(miShowStats);

		JMenuItem miShowSizeStats = new JMenuItem(Translator.$("Menu.View.ShowSizeStats"));
		miShowSizeStats.setActionCommand(Constants.MAINMENU_ACTION_SHOW_SIZE_STATS);
		miShowSizeStats.addActionListener(actionListener);
		menu.add(miShowSizeStats);
		return menu;
	}

	 public EDetailViewMode getDetailView() {
		 return detailViewMap.get(detailViewGroup.getSelection());
	}
}
