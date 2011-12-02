package net.grabsalot.gui;

import net.grabsalot.core.Application;
import net.grabsalot.business.Cacher;
import net.grabsalot.business.Configuration;
import net.grabsalot.business.Logger;
import net.grabsalot.business.WindowClosingAdapter;
import net.grabsalot.business.WorkingMode;
import net.grabsalot.i18n.Translator;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
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
public class LoadDialog extends JDialog {

	private JComboBox cmbPath;
	private JButton btnBrowse, btnProcess;
	private JRadioButton rbtCollection, rbtArtist, rbtAlbum;
	private ButtonGroup btgSource;
	private JCheckBox chkLoadLastOnStartup,chkUnloadCurrentCollections;
	private JFileChooser chooser;
	private List<String> collectionHistory;
	private File source;
	private static final long serialVersionUID = 2479242868400814549L;

	/**
	 * The constructor for the class. It creates a fully set up frame, but only
	 * shows it if the configuration is set to do so.
	 *
	 */
	public LoadDialog() {
		super(MainFrame.getInstance(), ModalityType.DOCUMENT_MODAL);
		getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.PAGE_AXIS));
		this.setResizable(false);
		this.setTitle(Translator.$("LoadDialog.Title"));
		this.addWindowListener(new WindowClosingAdapter());

		collectionHistory = Configuration.getInstance().getSourcesHistory();
		source = new File(Configuration.getInstance().getStringProperty(Configuration.WORKING_PATH));


		chooser = new JFileChooser(source);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		JPanel panPath = new JPanel();

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
		panPath.add(cmbPath);

		btnBrowse = new JButton(Translator.$("BrowseButton.Label"));
		btnBrowse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (chooser.showOpenDialog((JButton) e.getSource()) == JFileChooser.APPROVE_OPTION) {
					source = chooser.getSelectedFile();
					cmbPath.setSelectedItem(source.getAbsolutePath());
				}
			}
		});
		panPath.add(btnBrowse);

		add(panPath);

		rbtCollection = new JRadioButton(Translator.$("WorkingMode.Collection"));
		rbtArtist = new JRadioButton(Translator.$("WorkingMode.Artist"));
		rbtAlbum = new JRadioButton(Translator.$("WorkingMode.Album"));
		rbtCollection.setSelected(true);

		btgSource = new ButtonGroup();
		btgSource.add(rbtCollection);
		btgSource.add(rbtArtist);
		btgSource.add(rbtAlbum);

		JPanel panSource = new JPanel();
		panSource.add(new JLabel(Translator.$("WorkingMode.Text")));
		panSource.add(rbtCollection);
		panSource.add(rbtArtist);
		panSource.add(rbtAlbum);

		add(panSource);

		chkLoadLastOnStartup = new JCheckBox(Translator.$("config.loadLastOnStartup"), Configuration.getInstance().getBoolean(Configuration.LOAD_LAST_WORKING_DIRECTORY, false));
		chkLoadLastOnStartup.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				Configuration.getInstance().setProperty(Configuration.LOAD_LAST_WORKING_DIRECTORY, LoadDialog.this.chkLoadLastOnStartup.isSelected());
			}
		});
		add(chkLoadLastOnStartup);
		chkUnloadCurrentCollections = new JCheckBox(Translator.$("config.unloadOthers"), Configuration.getInstance().getBoolean("config.unloadOthersWhenLoading", false));
		chkUnloadCurrentCollections.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				Configuration.getInstance().setProperty("config.unloadOthersWhenLoading", LoadDialog.this.chkUnloadCurrentCollections.isSelected());
			}
		});
		add(chkUnloadCurrentCollections);

		btnProcess = new JButton(Translator.$("LoadButton.Label"));
		btnProcess.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Application.workingPath = source;
				Application.workingMode = LoadDialog.this.getSelectedMode();
				if (Application.workingMode != null) {
					Configuration.getInstance().addHistory(source);
					LoadDialog.this.setVisible(false);
					MainFrame.getInstance().load(Application.workingPath, Application.workingMode);
				}
			}
		});
		add(btnProcess);

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

}
