/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.grabsalot.util;

import java.util.Properties;

/**
 *
 * @author madboyka
 */
public class LibLoader {

	private static final Properties libs = new Properties();
	private static String classPath = System.getProperty("java.class.path");

	public static boolean isLibAvailable(String name, String version) {
		
		if (libs.get(name) == null) return false;
		return true;
	}
}
