package net.grabsalot.business;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.XMLFormatter;

import net.grabsalot.business.Logger;

/**
 * A singletonized Logger class
 * 
 * @author madboyka
 * 
 */
public class Logger extends java.util.logging.Logger {

	private static final String FILE_NAME = "grabsalot.log.xml";

	private static Logger logger;

	/**
	 * Default singleton constructor
	 */
	private Logger() {
		super("grabsalot.util.MainLogger", null);
		try {
			FileHandler logFile = new FileHandler(FILE_NAME);
			logFile.setFormatter(new XMLFormatter() {
				@Override
				public String getHead(Handler h) {
					String text = super.getHead(h);
					text = text.replace("<log>", "");
					text += "<?xml-stylesheet type=\"text/xsl\" href=\"logviewer.xsl\"?>\n";
					text += "<log>\n";
					return text;
				}
			});
			this.addHandler(logFile);
		} catch (Exception ex) {
			// TODO
		}
	}

	/**
	 * Returns an instance to the singleton class
	 * 
	 * @return
	 */
	public static Logger _() {
		if (logger == null) {
			logger = new Logger();
		}
		return logger;
	}
	
	public void log(String message, String... substitutes) {
		for (int i = 0; i < substitutes.length; ++i) {
			message = message.replaceAll("\\$" + (i+1) + ";", substitutes[i]);
		}
		this.info(message);
	}

	public void close() {
		for (Handler handler : this.getHandlers()) {
			handler.close();
		}
	}
}
