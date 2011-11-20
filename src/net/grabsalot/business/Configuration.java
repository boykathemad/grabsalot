package net.grabsalot.business;

import java.util.logging.Level;
import net.grabsalot.util.Serializer;

import net.grabsalot.business.Configuration;
import net.grabsalot.business.Logger;
import net.grabsalot.business.WorkingMode;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.LogManager;

/**
 * Singleton class that holds configuration settings.
 *
 * @author madboyka
 *
 */
public class Configuration {

	public static final String WORKING_PATH = "main.workingpath";
	public static final String WORKING_PATH_HISTORY = "main.history.";
	public static final String WORKING_MODE = "main.workingmode";
	public static final String LOAD_LAST_WORKING_DIRECTORY = "config.loadlastworkingdir";
	public static final String MAIN_WINDOW_STATE = "mainframe.state";
	public static final String MAIN_WINDOW_POSITION = "mainframe.position";
	public static final String MAIN_WINDOW_SIZE = "mainframe.size";
	public static final String MAIN_WINDOW_SPLITTER_POSITION = "mainframe.splitterposition";
	private static final String FILE_NAME = "grabsalot.ini";
	private static final String FILE_COMMENTS = "Properties file for the application Grabsalot";
	private static final int HISTORY_LENGHT = 6;
	private static Configuration instance;
	private Properties properties;
	private boolean savable = false;
	private boolean immediateSaving = true;
	private File configFile;

	/**
	 * Default singleton constructor;
	 */
	private Configuration() {
		properties = new Properties();
		this.load();
		try {
			LogManager.getLogManager().readConfiguration(
					new ByteArrayInputStream("org.jaudiotagger.level = OFF".getBytes()));
		} catch (Exception e) {
			Logger._().log(Level.WARNING, "Configuration: could not turn off jaudiotagger's logging:{0}", e.getMessage());
		}
	}

	/**
	 * Returns the instance of the singleton class
	 *
	 * @return
	 */
	public static Configuration getInstance() {
		if (instance == null) {
			instance = new Configuration();
		}
		return instance;
	}

	/**
	 * Set the default values for settings. Not yet implemented.
	 */
	private Object getDefaultValue(String propertyName) {
		return null;
	}

	/**
	 * Returns the path names of previously selected collection sources
	 *
	 * @return
	 */
	public List<String> getSourcesHistory() {
		List<String> folders = new ArrayList<String>();
		for (int i = 0; i < HISTORY_LENGHT; ++i) {
			String history = this.getStringProperty(WORKING_PATH_HISTORY + i);
			if (history.length() > 0) {
				folders.add(history);
			}
		}
		return folders;
	}

	/**
	 * Adds a new path that was used as collection source
	 *
	 * @param workingPath
	 *            the name of the directory
	 */
	public void addHistory(File workingPath) {
		this.setProperty(Configuration.WORKING_PATH, workingPath.getAbsolutePath());
		if (workingPath.getAbsolutePath().equals(this.getStringProperty(WORKING_PATH_HISTORY + 0))) {
			return;
		}
		for (int i = HISTORY_LENGHT; i > 1; --i) {
			this.setProperty(WORKING_PATH_HISTORY + (i), this.getStringProperty(WORKING_PATH_HISTORY + (i - 1)));
		}
		this.setProperty(WORKING_PATH_HISTORY + 1, workingPath.getAbsolutePath());
	}

	/**
	 * Loads the configuration settings.
	 *
	 * @return true on success and false on error
	 */
	private boolean load() {
		this.configFile = this.getConfigurationFile();
		if (this.configFile != null) {
			try {
				properties.load(new FileInputStream(this.configFile));
				this.savable = this.configFile.canWrite();
				return true;
			} catch (Exception e) {
				Logger._().log(Level.SEVERE, "Configuration.load():{0}", e.getMessage());
			}
		}
		return false;
	}

	private File getConfigurationFile() {
		File file = new File(FILE_NAME);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException ex) {
				file = null;
				String userHome = System.getProperty("user.home");
				if (userHome != null) {
					File settingsDirectory = new File(new File(userHome), ".grabsalot");
					if (settingsDirectory.exists() || settingsDirectory.mkdir()) {
						file = new File(settingsDirectory, FILE_NAME);
						try {
							if (!file.exists() && !file.createNewFile()) {
								throw new Exception();
							}
						} catch (Exception ex1) {
							file = null;
						}
					}
				}
			}
		}
		return file;
	}

	/**
	 * Reloads the configuration from the file. Not yet implemented.
	 */
	public void reload() {
		this.load();
	}

	/**
	 * Sets a setting denoted by name, to value.
	 *
	 * @param name
	 * @param value
	 */
	public void setProperty(String name, Object object) {
		String value = "";
		value = Serializer.toString(object);
		properties.setProperty(name, value);
		if (this.immediateSaving) {
			this.save();
		}
	}

	/**
	 * Saves the configuration file.
	 */
	public void save() {
		if (this.savable) {
			try {
				if (!configFile.exists()) {
					configFile.createNewFile();
				}
				properties.store(new FileOutputStream(configFile), FILE_COMMENTS);
			} catch (IOException e) {
				Logger._().log(Level.SEVERE, "Could''nt save configuration: {0}", e.getMessage());
			}
		}
	}

	/**
	 * Returns a configuration value denoted by key as boolean, or b if the
	 * configuration does not exist.
	 *
	 * @param key
	 * @param b
	 * @return
	 */
	public boolean getBoolean(String key, Boolean defaultValue) {
		return (Boolean) this.getProperty(key, defaultValue);
	}

	public boolean getBoolean(String key) {
		return this.getBoolean(key, false);
	}

	/**
	 * Returns the object stored as the setting for key, or defaultValue if the
	 * configuration does not exist. In the later case also saves defaultValue
	 * as the value for the setting.
	 *
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public Object getProperty(String key, Object defaultValue) {
		Object property = null;
		String value = this.properties.getProperty(key);
		if (value != null) {
			property = Serializer.fromString(value);
		}
		if (property == null) {
			if (defaultValue != null) {
				property = defaultValue;
			} else {
				property = this.getDefaultValue(key);
			}
			this.setProperty(key, property);
		}
		return property;
	}

	public String getStringProperty(String key, String defaultValue) {
		Object value = getProperty(key, defaultValue);
		if (value == null) {
			return defaultValue;
		}
		return value.toString();
	}

	public String getStringProperty(String key) {
		return this.getStringProperty(key, "");
	}

	public int getIntegerProperty(String key) {
		return this.getIntegerProperty(key, 0);
	}

	public int getIntegerProperty(String key, int defaultValue) {
		return (Integer) this.getProperty(key, defaultValue);
	}

	public File getLastWorkingPath() {
		return new File(this.getStringProperty(Configuration.WORKING_PATH));
	}

	public WorkingMode getLastWorkingMode() {
		return WorkingMode.valueOf(this.getStringProperty(Configuration.WORKING_MODE, "COLLECTION"));
	}
}
