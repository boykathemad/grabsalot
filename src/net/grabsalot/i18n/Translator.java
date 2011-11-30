package net.grabsalot.i18n;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import net.grabsalot.business.Logger;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Translator {

	public static final String DEFAULT_LANGUAGE = "en";
	private static ResourceBundle resource;

	static {
		setLanguage(DEFAULT_LANGUAGE);
	}

	public static boolean setLocale(Locale locale) {
		try {
			resource = ResourceBundle.getBundle("net.grabsalot.i18n.lang", locale);
			return true;
		} catch (MissingResourceException ex) {
			Logger._().severe(ex.getMessage());
			return false;
		}
	}

	private Translator() {
	}

	public static String $(String key) {
		try {
			return resource.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	public static String $(String key, String... substitutes) {
		try {
			String text = resource.getString(key);
			for (int i = 0; i < substitutes.length; ++i) {
				String replace = (substitutes[i] != null) ? substitutes[i] : "";
				text = text.replaceAll("\\{" + i + "\\}", replace);
			}
			return text;
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	public static boolean setLanguage(String languageCode) {
		return setLocale(new Locale(languageCode));
	}

	public static List<String> getAvailableLanguages() {
		List<String> languages = new ArrayList<String>();
		try {
			ResourceBundle resource = ResourceBundle.getBundle("net.grabsalot.i18n.languages");
			Enumeration<String> keys = resource.getKeys();
			while (keys.hasMoreElements()) {
				languages.add(keys.nextElement());
			}
		} catch (MissingResourceException ex) {
			Logger._().severe(ex.getMessage());
		}
		return languages;
	}
}
