package net.grabsalot.gui.settings;

import net.grabsalot.business.Configuration;
import net.grabsalot.gui.SettingsPanel;

import net.grabsalot.gui.settings.LookAndFeelSettings;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class LookAndFeelSettings extends SettingsPanel {
	private static final long serialVersionUID = -7379550452378132620L;
	private static final String defaultLookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();

	private ArrayList<JRadioButton> lafOptions;
	private ArrayList<String> lafDescriptions;
	private ButtonGroup buttonGroup;

	public LookAndFeelSettings() {
		this.setLayout(new GridBagLayout());
		this.loadLafOptions();

		GridBagConstraints gbc = new GridBagConstraints();
		buttonGroup = new ButtonGroup();

		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridwidth = 2;
		gbc.gridy = 0;
		this.add(new JLabel("Look and Feel:"), gbc);

		gbc.gridwidth = 1;

		for (JRadioButton btn : this.lafOptions) {
			gbc.gridy += 1;
			this.add(btn, gbc);
			buttonGroup.add(btn);

			gbc.gridx = 1;
			this.add(new JLabel(lafDescriptions.get(lafOptions.indexOf(btn))), gbc);
			gbc.gridx = 0;
		}

	}

	private void loadLafOptions() {
		lafOptions = new ArrayList<JRadioButton>();
		lafDescriptions = new ArrayList<String>();
		String current = Configuration.getInstance().getStringProperty("settings.lookandfeel",
				LookAndFeelSettings.defaultLookAndFeel);
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			JRadioButton rbtn = new JRadioButton(info.getName());
			rbtn.setActionCommand(info.getClassName());
			rbtn.setSelected(current.equals(info.getClassName()));
			lafDescriptions.add(this.getLafDescription(info.getName()));
			lafOptions.add(rbtn);
		}
	}

	private String getLafDescription(String lafName) {
		if (lafName.equals("Metal")) {
			return "Default java L&F(has themes)";
		}
		if (lafName.equals("Nimbus")) {
			return "A very nice looking L&F(not recommended due to bugs)";
		}
		if (lafName.equals("CDE/Motif")) {
			return "A solaris style L&F";
		}
		if (lafName.equals("Windows")) {
			return "A Windows style L&F";
		}
		if (lafName.equals("GTK")) {
			return "A Gnome(GTK) style L&F";
		}
		if (lafName.equals("Windows Classic")) {
			return "The classic Win95/98 style L&F(not recommended due to its ugliness";
		}
		return "";
	}

	@Override
	public boolean save() {
		try {
			String value = null;
			value = buttonGroup.getSelection().getActionCommand();
			Configuration.getInstance().setProperty("settings.lookandfeel", value);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

}
