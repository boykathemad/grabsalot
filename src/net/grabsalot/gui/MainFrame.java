package net.grabsalot.gui;

import java.awt.BasicStroke;
import net.grabsalot.core.Application;
import net.grabsalot.business.Cacher;
import net.grabsalot.business.CollectionTreeNode;
import net.grabsalot.business.Configuration;
import net.grabsalot.business.Logger;
import net.grabsalot.business.Playlist;
import net.grabsalot.business.WindowClosingAdapter;
import net.grabsalot.business.WorkingMode;
import net.grabsalot.dao.local.LocalCollection;
import net.grabsalot.dao.local.LocalTrack;
import net.grabsalot.gui.progress.ProgressWindow;
import net.grabsalot.i18n.Translator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.StrokeBorder;

/**
 * The main frame for the application. This frame is the most important, as if
 * it is closed the application closes also.
 *
 * @author madboyka
 *
 */
public final class MainFrame extends JFrame implements LocalizableComponent {

	private static final long serialVersionUID = -2881416156872984622L;
	private static MainFrame _instance = null;

	private LocalCollection manager;
	private File path;
	private WorkingMode mode;
	private MainMenuBar mainMenu;
	private JStatusBar statusbar;
	private JSplitPane splitter;
	private JPanel panTree;
	private JScrollPane spnInfo;
	private JPanel panInfo;
	private PlayerPanel playerPanel;
	private Playlist playlist;

	/**
	 * The default constructor.
	 */
	public MainFrame() {
		this.setSize(960, 640);
		this.setLocationRelativeTo(null);
		this.setupComponents();
		this.setBehavior();
		this.setLabels();

		Cacher.addItem(Cacher.MAINFRAME_NAME, this);
	}

	/**
	 * The recommended constructor. All components are added to the frame here.
	 *
	 * @param workingMode
	 *            an integer representing the processing method.
	 * @param path
	 *            a path to an existing directory, that server as the root for
	 *            processing
	 */
	public MainFrame(WorkingMode workingMode, File path) {
		this();
		this.path = path;
		this.mode = workingMode;
	}

	protected void setBehavior() {
		this.addWindowListener(new WindowClosingAdapter());
		this.loadWindowState();
	}

	protected void setLabels() {
		this.setTitle(Translator.$("Application.Title"));
	}

	protected void setupComponents() {
		this.setLayout(new BorderLayout(0, 0));
		mainMenu = new MainMenuBar();
		add(mainMenu, BorderLayout.NORTH);

		this.statusbar = new JStatusBar();
		this.playerPanel = new PlayerPanel();
		this.statusbar.add(Box.createHorizontalGlue());
		this.statusbar.add(this.playerPanel);
		this.add(this.statusbar, BorderLayout.SOUTH);

		panInfo = new JPanel();
		panInfo.setLayout(new BoxLayout(panInfo, BoxLayout.PAGE_AXIS));
		JLabel firstLabel = new JLabel(Translator.$("InfoPanel.SelectionEmpty"));
		firstLabel.setAlignmentX(0.5F);
		panInfo.add(firstLabel);

		spnInfo = new JScrollPane(panInfo);

		panTree = new CollectionTreePanel(this);

		splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panTree, spnInfo);
		add(splitter, BorderLayout.CENTER);
	}

	/**
	 * This method shows the MainFrame and if possible load the collection data.
	 * This should be called on the first invocation of the MainFrame.
	 *
	 */
	public void display() {
		setVisible(true);
		if (Configuration.getInstance().getBoolean(Configuration.LOAD_LAST_WORKING_DIRECTORY, false)) {
			load(Application.workingPath, Application.workingMode);
		}
	}

	public JPanel getInfoPanel() {
		return panInfo;
	}

	/**
	 * Returns the mode selected to process the source
	 *
	 * @return
	 */
	public WorkingMode getMode() {
		return mode;
	}

	/**
	 * Sets the mode to process the source
	 *
	 * @param mode
	 *            an integer representing the mode
	 */
	public void setMode(WorkingMode mode) {
		this.mode = mode;
	}

	/**
	 * Returns the source path for processing
	 *
	 * @return
	 */
	public File getSource() {
		return this.path;
	}

	public void setSource(File path) {
		this.path = path;
	}

	/**
	 * Starts the playback of the specified track and updates the frame
	 * accordingly.
	 *
	 * @param track
	 *            the track to be played
	 */
	public void playTrack(LocalTrack track) {
		this.playerPanel.loadTrack(track);
		this.playerPanel.startPlayback();
	}

	/**
	 * Stops the playback of currently playing track, does nothing if no track
	 * is playing.
	 */
	public void stopTrack() {
		this.playerPanel.stopPlayback();
	}

	/**
	 * Sets the text of the frames status bar to the specified text
	 *
	 * @param text
	 */
	public void setStatusBarText(String text) {
		this.statusbar.setText(text);
	}

	/**
	 * Sets the status bar text to the idle string
	 */
	public void setStatusBarIdle() {
		this.statusbar.setText(Translator.$("StatusText.Idle"));
	}

	/**
	 * Returns the {@link CollectionManager} object that this frame used on
	 * processing
	 *
	 * @return
	 */
	public LocalCollection getCollectionManager() {
		return this.manager;
	}

	private void loadWindowState() {
		try {
			this.setExtendedState(Configuration.getInstance().getIntegerProperty(Configuration.MAIN_WINDOW_STATE, 0));
			Point location = (Point) Configuration.getInstance().getProperty(Configuration.MAIN_WINDOW_POSITION, null);
			Dimension size = (Dimension) Configuration.getInstance().getProperty(Configuration.MAIN_WINDOW_SIZE,
					new Dimension(800, 600));
			this.setSize(size);
			if (location == null) {
				this.setLocationRelativeTo(null);
			} else {
				this.setLocation(location);
			}
			splitter.setDividerLocation(Configuration.getInstance().getIntegerProperty(
					Configuration.MAIN_WINDOW_SPLITTER_POSITION, 200));
		} catch (ClassCastException ex) {
			Logger._().severe("Invalid object stored by Configurator");
		}
	}

	public void saveWindowState() {
		Configuration.getInstance().setProperty(Configuration.MAIN_WINDOW_STATE, this.getExtendedState());
		Configuration.getInstance().setProperty(Configuration.MAIN_WINDOW_POSITION, this.getLocation());
		Configuration.getInstance().setProperty(Configuration.MAIN_WINDOW_SIZE, this.getSize());
		Configuration.getInstance().setProperty(Configuration.MAIN_WINDOW_SPLITTER_POSITION,
				splitter.getDividerLocation());
	}

	public void setPlaylist(Playlist playlist) {
		this.playlist = playlist;
		this.playlist.dumpTracks();
	}

	public void playTracks(LocalTrack[] tracks) {
		this.playerPanel.clearPlaylist();
		for (LocalTrack track : tracks) {
			this.playerPanel.enqueueTrack(track);
		}
		this.playerPanel.startPlayback();
	}

	public void enqueueTrack(LocalTrack track) {
		this.playerPanel.enqueueTrack(track);
	}

	public void loadSelectionInfo(CollectionTreeNode node) {
		DetailViewManager.loadSelectionInfo(node);
	}

	public static MainFrame getInstance() {
		return _instance;
	}

	public ProgressWindow createProgressWindow(String title) {
		ProgressWindow progress = new ProgressWindow(this, title);
		return progress;
	}

	public void setAlwaysLoseFocus(boolean b) {
		this.setEnabled(!b);
	}

	void load(File workingPath, WorkingMode workingMode) {
		this.mode = workingMode;
		this.path = workingPath;
		if (this.mode == WorkingMode.COLLECTION) {
			manager = new LocalCollection(this.path);
			//TODO might be buggy
			((CollectionTreePanel) panTree).clear();
			((CollectionTreePanel) panTree).loadCollection(manager);
		}
	}

	@Override
	public void updateLabels() {
		this.setLabels();
	}


	public static MainFrame instantiate() {
		if (_instance != null) {
			_instance.dispose();
		}
		_instance = new MainFrame();
		return _instance;
	}
}
