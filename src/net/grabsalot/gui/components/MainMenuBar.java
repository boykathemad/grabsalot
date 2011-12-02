package net.grabsalot.gui.components;

import net.grabsalot.gui.controllers.EDetailViewMode;
import net.grabsalot.gui.controllers.DetailViewManager;
import net.grabsalot.gui.controllers.MainMenuBarActions;
import net.grabsalot.gui.components.LocalizableComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import java.util.List;
import java.util.Locale;
import net.grabsalot.business.Constants;
import net.grabsalot.dao.service.lastfm.LastFmMenu;
import net.grabsalot.i18n.Translator;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import net.grabsalot.gui.MainFrame;

public class MainMenuBar extends JMenuBar implements LocalizableComponent {

	private static final long serialVersionUID = 7739958073232720853L;
	private ActionListener actionListener, languagesMenuActionListener;
	private ButtonGroup detailViewGroup;
	private HashMap<ButtonModel, EDetailViewMode> detailViewMap;
	private JMenu mFile, mView, mOptions, mLanguages, mDetailView, mLastFm, mHelp;

	public MainMenuBar() {
		actionListener = new MainMenuBarActions(this);
		languagesMenuActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Locale locale = new Locale(e.getActionCommand());
				Translator.setLocale(locale);
				MainFrame.getInstance().updateLabels();
			}
		};
		this.add(Box.createHorizontalGlue());
		this.add(this.getFileMenu());
		this.add(this.getViewMenu());
		this.add(this.getOptionsMenu());
		this.add(new LastFmMenu());
		this.add(this.getHelpMenu());
		setLabels();
	}

	private void setLabels() {
		mFile.setText(Translator.$("menu.file"));
		mView.setText(Translator.$("menu.view"));
		mOptions.setText(Translator.$("menu.tools"));
		mHelp.setText(Translator.$("menu.help"));
		mLanguages.setText(Translator.$("menu.languages"));
		mDetailView.setText(Translator.$("menu.detailView"));
	}

	private JMenu getFileMenu() {
		mFile = new JMenu();
		JMenuItem miLoad = new JMenuItem(Translator.$("menu.load"));
		miLoad.setActionCommand(Constants.MAINMENU_ACTION_LOAD);
		miLoad.addActionListener(actionListener);
		mFile.add(miLoad);
		JMenuItem miRestart = new JMenuItem(Translator.$("menu.restart"));
		miRestart.setActionCommand(Constants.MAINMENU_ACTION_RESTART);
		miRestart.addActionListener(actionListener);
		mFile.add(miRestart);
		return mFile;
	}

	private JMenu getOptionsMenu() {
		mOptions = new JMenu();

		mLanguages = new JMenu();
		List<String> languages = Translator.getAvailableLanguages();
		ButtonGroup languageGroup = new ButtonGroup();
		for (String language : languages) {
			JMenuItem item = new JRadioButtonMenuItem(Translator.$("language." + language));
			languageGroup.add(item);
			if (Translator.DEFAULT_LANGUAGE.equals(language)) {
				item.setSelected(true);
			}
			item.setActionCommand(language);
			item.addActionListener(languagesMenuActionListener);
			mLanguages.add(item);
		}
		mOptions.add(mLanguages);

//		JCheckBoxMenuItem miHideConfig = new JCheckBoxMenuItem(Translator.$("menu.HideConfig"));
//		miHideConfig.setActionCommand(Constants.MAINMENU_ACTION_HIDE_CONFIG);
//		miHideConfig.addActionListener(actionListener);
//		mOptions.add(miHideConfig);

		JMenuItem miSettings = new JMenuItem(Translator.$("menu.settings"));
		miSettings.setActionCommand(Constants.MAINMENU_ACTION_SHOW_SETTINGS);
		miSettings.addActionListener(actionListener);
		mOptions.add(miSettings);

		JMenuItem miRules = new JMenuItem(Translator.$("menu.rules"));
		miRules.setActionCommand(Constants.MAINMENU_ACTION_SHOW_RULES);
		miRules.addActionListener(actionListener);
		mOptions.add(miRules);

		return mOptions;
	}

	@Override
	public final JMenu getHelpMenu() {
		mHelp = new JMenu();

		JMenuItem miAbout = new JMenuItem(Translator.$("menu.about"));
		miAbout.setActionCommand(Constants.MAINMENU_ACTION_SHOW_ABOUT);
		miAbout.addActionListener(actionListener);

		mHelp.add(miAbout);
		return mHelp;
	}

	public final JMenu getViewMenu() {
		mView = new JMenu();

		mDetailView = new JMenu();
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
			mDetailView.add(modeMenu);
		}
		mView.add(mDetailView);
		return mView;
	}

	public EDetailViewMode getDetailView() {
		return detailViewMap.get(detailViewGroup.getSelection());
	}

	@Override
	public void updateLabels() {
		setLabels();
	}
}
