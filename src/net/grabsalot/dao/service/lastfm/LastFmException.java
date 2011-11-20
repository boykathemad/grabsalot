package net.grabsalot.dao.service.lastfm;

/** The exception class used by last.fm service objects
 * @author madboyka
 *
 */
public class LastFmException extends Exception {

	private static final long serialVersionUID = -4861698677206448338L;
	public static final int ERROR_API_UNKOWN = 1;
	public static final int ERROR_API_WRONG_URL = 2;
	public static final int ERROR_API_READ_FAILED = 3;
	public static final int ERROR_API_DOCUMENT_PARSE_FAILED = 4;
	public static final int ERROR_API_LAST_FM_STATUS_FAILED = 5;

	private int errorCode;

	/** Constructor with error code.
	 * @param errorCode
	 */
	public LastFmException(int errorCode) {
		this.errorCode = errorCode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		switch (this.errorCode) {
		case ERROR_API_UNKOWN:
			return "Unknown error";
		case ERROR_API_WRONG_URL:
			return "Malformed request URL";
		case ERROR_API_READ_FAILED:
			return "Failed to read response";
		case ERROR_API_DOCUMENT_PARSE_FAILED:
			return "Failed to parse response document";
		case ERROR_API_LAST_FM_STATUS_FAILED:
			return "Last.fm status wasn't ok";
		default:
			return "No or unknown error!";
		}
	}
}
