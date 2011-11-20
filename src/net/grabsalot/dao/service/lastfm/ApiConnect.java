package net.grabsalot.dao.service.lastfm;

import net.grabsalot.business.Logger;

import net.grabsalot.dao.service.lastfm.ApiConnect;
import net.grabsalot.dao.service.lastfm.LastFmException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/** The class used to connect and download information from the last.fm webservice.
 * @author madboyka
 *
 */
public abstract class ApiConnect {

	protected Element info;

	private static String apiUrl = "http://ws.audioscrobbler.com/2.0/";
	private static String apiKey = "b1e7ce795ce107299236cb62b1ced720";

	public ApiConnect() {
		info = null;
	}

	/** Call a webservice method and sets the info field according to the result.
	 * @param methodName name of the method to call
	 * @param parameters list of parameter names and values
	 * @throws LastFmException
	 */
	protected void callMethod(String methodName, HashMap<String, String> parameters) throws LastFmException {
		try {
			String urlString = ApiConnect.apiUrl + "?api_key=" + ApiConnect.apiKey + "&method="
					+ URLEncoder.encode(methodName, "UTF-8");
			for (String i : parameters.keySet()) {
				urlString += "&" + i + "=" + URLEncoder.encode(parameters.get(i), "UTF-8");
			}
			Logger._().warning("ApiConnect:callMethod:URL:" + urlString);
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(2000);
			String str = new String(), line = new String();
			BufferedReader rdr = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while ((line = rdr.readLine()) != null) {
				str += line;
			}
			info = DocumentHelper.parseText(str).getRootElement();
			if (!info.attribute("status").getValue().toString().equals("ok")) {
				throw new LastFmException(LastFmException.ERROR_API_LAST_FM_STATUS_FAILED);
			}
		} catch (DocumentException ex) {
			throw new LastFmException(LastFmException.ERROR_API_DOCUMENT_PARSE_FAILED);
		} catch (MalformedURLException ex) {
			throw new LastFmException(LastFmException.ERROR_API_WRONG_URL);
		} catch (IOException ex) {
			throw new LastFmException(LastFmException.ERROR_API_READ_FAILED);
		} catch (Exception ex) {
			throw new LastFmException(LastFmException.ERROR_API_UNKOWN);
		}
	}

	/** Returns an xml element from the info field
	 * @param key
	 * @return
	 */
	protected Element getProperty(String key) {
		return info.element(key);
	}
	
	/** Returns the tags for the service element
	 * @return
	 */
	public abstract String getTags();
	
	/** Returns a list of xml elements from the info field
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<Element> getProperties(String key) {
		return info.elements(key);
	}
}
