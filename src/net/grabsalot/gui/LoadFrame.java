package net.grabsalot.gui;

import net.grabsalot.core.Application;
import net.grabsalot.business.Cacher;
import net.grabsalot.business.Configuration;
import net.grabsalot.business.Logger;
import net.grabsalot.business.WindowClosingAdapter;
import net.grabsalot.business.WorkingMode;
import net.grabsalot.i18n.Translator;

import net.grabsalot.gui.LoadFrame;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * The configuration frame for the application
 *
 * @author madboyka
 *
 */
public class LoadFrame extends JFrame {

	private JComboBox cmbPath;
	private JButton btnChoose, btnProcess;
	private JRadioButton rbtCollection, rbtArtist, rbtAlbum;
	private ButtonGroup btgSource;
	private JCheckBox chkLoadLastOnStartup;
	private JFileChooser chooser;
	private List<String> collectionHistory;
	private File source;
	private boolean loadLast;
	private Application waitObject;
	private static final long serialVersionUID = 2479242868400814549L;

	/**
	 * The constructor for the class. It creates a fully set up frame, but only
	 * shows it if the configuration is set to do so.
	 *
	 */
	public LoadFrame() {
		this.setLayout(new GridBagLayout());
		this.setResizable(false);
		this.setTitle(Translator.$("ConfigFrameTitle"));
		this.addWindowListener(new WindowClosingAdapter());

		this.loadLast = Configuration.getInstance().getBoolean(Configuration.LOAD_LAST_WORKING_DIRECTORY, false);
		collectionHistory = Configuration.getInstance().getSourcesHistory();
		source = new File(Configuration.getInstance().getStringProperty(Configuration.WORKING_PATH));

		chooser = new JFileChooser(source);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridwidth = 4;

		cmbPath = new JComboBox(collectionHistory.toArray());
		cmbPath.setPreferredSize(new Dimension(300, 20));
		cmbPath.setEditable(true);
		cmbPath.setSelectedItem(source.getAbsolutePath());
		cmbPath.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				source = new File((String) cmbPath.getSelectedItem());
			}
		});
		add(cmbPath, gbc);

		btnChoose = new JButton(Translator.$("BrowseButtonText"));
		btnChoose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (chooser.showOpenDialog((JButton) e.getSource()) == JFileChooser.APPROVE_OPTION) {
					source = chooser.getSelectedFile();
					cmbPath.setSelectedItem(source.getAbsolutePath());
				}
			}
		});
		gbc.gridx = 4;
		gbc.gridwidth = 1;
		add(btnChoose, gbc);

		rbtCollection = new JRadioButton(Translator.$("CollectionWorkingMode"));
		rbtArtist = new JRadioButton(Translator.$("ArtistWorkingMode"));
		rbtAlbum = new JRadioButton(Translator.$("AlbumWorkingMode"));
		rbtCollection.setSelected(true);

		btgSource = new ButtonGroup();
		btgSource.add(rbtCollection);
		btgSource.add(rbtArtist);
		btgSource.add(rbtAlbum);

		JPanel panSource = new JPanel();
		panSource.add(new JLabel(Translator.$("WorkingModeText")));
		panSource.add(rbtCollection);
		panSource.add(rbtArtist);
		panSource.add(rbtAlbum);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 5;
		add(panSource, gbc);

		chkLoadLastOnStartup = new JCheckBox(Translator.$("LoadLastOnStartup"), this.loadLast);
		chkLoadLastOnStartup.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				Configuration.getInstance().setProperty(Configuration.LOAD_LAST_WORKING_DIRECTORY, LoadFrame.this.chkLoadLastOnStartup.isSelected());
			}
		});
		gbc.gridy = 2;
		add(chkLoadLastOnStartup, gbc);

		btnProcess = new JButton(Translator.$("LoadButtonText"));
		btnProcess.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Application.workingPath = source;
				Application.workingMode = LoadFrame.this.getSelectedMode();
				if (Application.workingMode != null) {
					Configuration.getInstance().addHistory(source);
					LoadFrame.this.setVisible(false);
					Cacher.getMainFrame().load(Application.workingPath, Application.workingMode);
				}
			}
		});
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(btnProcess, gbc);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	/**
	 * Checks if the selected processing method is correct and available, and
	 * return an integer representing that method.
	 *
	 * @return an integer representing the selected method, or 0 on error.
	 *         Possible values on {@link Constraints}
	 */
	public WorkingMode getSelectedMode() {
		if (source == null || !source.isDirectory()) {
			JOptionPane.showMessageDialog(this, Translator.$("InvalidSourceMessageText"), Translator.$("InvalidSourceMessageTitle"), JOptionPane.ERROR_MESSAGE);
			return null;
		}
		boolean xmlCollection = new File(source, "collection.xml").exists();
		boolean xmlArtist = new File(source, "artist.xml").exists();
		boolean xmlAlbum = new File(source, "album.xml").exists();
		WorkingMode mode = null;
		if (btgSource.getSelection() == rbtCollection.getModel()) {
			mode = WorkingMode.COLLECTION;
		} else if (btgSource.getSelection() == rbtArtist.getModel()) {
			mode = WorkingMode.ARTIST;
		} else if (btgSource.getSelection() == rbtAlbum.getModel()) {
			mode = WorkingMode.ALBUM;
		}
		Logger._().log("Processing $1; using the $2; mode.", source.getAbsolutePath(), Translator.$("WorkingMode" + mode));
		if (((xmlArtist || xmlAlbum) && mode == WorkingMode.COLLECTION)
				|| ((xmlCollection || xmlAlbum) && mode == WorkingMode.ARTIST)
				|| ((xmlCollection || xmlArtist) && mode == WorkingMode.ALBUM)) {
			if (JOptionPane.showConfirmDialog(this, Translator.$("ConfigFrame.15") //$NON-NLS-1$
					+ (xmlArtist ? Translator.$("ConfigFrame.16") : (xmlAlbum ? Translator.$("ConfigFrame.17") : Translator.$("ConfigFrame.18"))) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ Translator.$("ConfigFrame.19"), Translator.$("ConfigFrame.20"), //$NON-NLS-1$ //$NON-NLS-2$
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
				return null;
			}
		}
		return mode;
	}

	public void setWaitObject(Application waitObj) {
		this.waitObject = waitObj;
	}
}
