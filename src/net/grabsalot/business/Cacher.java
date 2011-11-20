package net.grabsalot.business;

import java.util.HashMap;
import net.grabsalot.gui.MainFrame;


import java.util.Hashtable;
import java.util.Map;

/** Static class that stores data and returns it.
 * @author madboyka
 *
 */
public class Cacher {

	private static Map<String, Object> items = new HashMap<String, Object>();
	public static String MAINFRAME_NAME = "frmMainFrame";
	public static String CONFIGFRAME_NAME = "frmConfigFrame";
	public static String RULESFRAME_NAME = "frmRulesFrame";

	/** Add some data to store.
	 * @param key name of the key,that is user to later access stored data
	 * @param object the data object to be stored
	 */
	public static void addItem(String key, Object object) {
		items.put(key, object);
	}

	/** Loads a stored object from this cache and returns it.
	 * @param key the key of the stored object
	 * @return the stored object
	 */
	public static Object getItem(String key) {
		return items.get(key);
	}

	/** Returns the Mainframe if store, null otherwise.
	 * @return
	 */
	public static MainFrame getMainFrame() {
		return (MainFrame) Cacher.getItem("frmMainFrame");
	}

	/**
	 * Disposes of all frames stored in the cache and deletes all other infomation
	 */
	public static void reset() {
		Cacher.items.clear();
		Runtime r = Runtime.getRuntime();
		r.gc();
	}
}
