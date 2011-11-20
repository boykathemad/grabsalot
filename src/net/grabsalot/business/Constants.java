package net.grabsalot.business;

/**
 * Class containing constants that are not part of the applications logic, and
 * are only used for programming.
 *
 * @author madboyka
 *
 */
public final class Constants {
	public static final String MAINMENU_ACTION_SHOW_RULES = "showRules";
	public static final String MAINMENU_ACTION_SHOW_ABOUT = "showAbout";
	public static final String MAINMENU_ACTION_SHOW_SETTINGS = "showSettings";
	public static final String MAINMENU_ACTION_LOAD = "load";
	public static final String MAINMENU_ACTION_RESTART = "restart";
	public static final String MAINMENU_ACTION_HIDE_CONFIG = "hideConfig";
	public static final String MAINMENU_ACTION_VIEWMODE_CHANGED = "viewChanged";
	public static final String MAINMENU_ACTION_SHOW_GENRE_STATS = "showGenreStats";
	public static final String MAINMENU_ACTION_SHOW_SIZE_STATS = "showSizeStats";
	public static final int COLLECTION_AS_SOURCE = 1;
	public static final int ARTIST_AS_SOURCE = 2;
	public static final int ALBUM_AS_SOURCE = 3;
	public static final String[] TREE_ICON_IDS = {"collection","artist", "album", "track"};
	public static final String[] TREE_ICON_FILES = {"net/grabsalot/icon-collection.png","net/grabsalot/icon-artist.png","net/grabsalot/icon-album.png","net/grabsalot/icon-track.png"};
	public static final int COLLECTION_ICON_ID = 0;
	public static final int ARTIST_ICON_ID = 1;
	public static final int ALBUM_ICON_ID = 2;
	public static final int TRACK_ICON_ID = 3;
}
