package net.grabsalot.core;

import java.io.File;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import javax.swing.JFrame;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import net.grabsalot.business.Cacher;
import net.grabsalot.business.Configuration;
import net.grabsalot.business.Logger;
import net.grabsalot.business.WorkingMode;
import net.grabsalot.gui.LoadDialog;
import net.grabsalot.gui.MainFrame;
import net.grabsalot.i18n.Translator;

/**
 * This is the main class for the application
 *
 * @author madboyka
 */
public class Application {

	public static final String name = "GrabsALot";
	public static final String version = "0.2.0";
	public static final String author = "Gaspar Portik (madboyka)";
	public static final String released = "2011-11-23";
	public static File workingPath = null;
	public static WorkingMode workingMode = null;
	public static MainFrame window = null;
	private static String[] arguments;

	public static void start(String[] args) {
		arguments = args;
		try {
			Logger._().info("The application is starting up. First log entry.");
			Application.setup();
			Application.showMainFrame();
			Logger._().info("Application started successfully!");
		} catch (ApplicationException ex) {
			Logger._().log(Level.SEVERE, "Error on startup:{0}", ex.getMessage());
			JOptionPane.showMessageDialog(
					null,
					Translator.$("Error.FailedToStartApplication", ex.getMessage()),
					Translator.$("ErrorDialog.Title"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private Application() {
	}

	private static void setup() throws ApplicationException {
		setProxy();

		setLookAndFeel(Configuration.getInstance().getStringProperty("settings.lookandfeel",
				UIManager.getCrossPlatformLookAndFeelClassName()));

		if (Configuration.getInstance().getBoolean(Configuration.LOAD_LAST_WORKING_DIRECTORY, false)) {
			workingPath = Configuration.getInstance().getLastWorkingPath();
			workingMode = Configuration.getInstance().getLastWorkingMode();
		}
	}

	/**
	 * Display the MainFrame and auto-load the collection.
	 */
	public static void showMainFrame() throws ApplicationException {
//		if (Application.workingPath != null) {
//			Configuration.getInstance().addHistory(Application.workingPath);
//		}
		window = MainFrame.instantiate();
		window.setSource(workingPath);
		window.setMode(workingMode);
		window.display();
	}

	public static void setLookAndFeel(String lafClassName) {
		try {
			UIManager.setLookAndFeel(lafClassName);
		} catch (Exception ex) {
			Logger._().log(Level.WARNING, "Could not change Look and Feel:{0}", ex.getMessage());
		}
	}

	private static void setProxy() {
		boolean useProxy = Configuration.getInstance().getBoolean("net.proxy.use", false);
		if (useProxy) {
			System.setProperty("http.proxyHost", Configuration.getInstance().getStringProperty("net.proxy.host"));
			System.setProperty("http.proxyPort", Configuration.getInstance().getStringProperty("net.proxy.port"));
		}
	}

	/**
	 * Destroys all data and classes except for static and singleton classes,
	 * and restarts the application.
	 */
	public static void restart() {
		window.prepareForClose();
		window.dispose();
		window = null;
		Cacher.reset();
		Thread mainThread = new Thread() {

			@Override
			public void run() {
				Application.start(arguments);
			}
		;
		};
		mainThread.start();
	}

	public static void close(boolean mainFrameClosing) {
		if (mainFrameClosing) {
			Cacher.getMainFrame().saveWindowState();
		}
		Configuration.getInstance().save();
		Logger._().fine("Application is closing. Last log entry.");
		Logger._().close();
		System.exit(0);
	}
}
