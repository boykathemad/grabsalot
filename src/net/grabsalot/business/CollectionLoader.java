package net.grabsalot.business;

import net.grabsalot.business.task.Task;
import net.grabsalot.dao.local.LocalAlbum;
import net.grabsalot.dao.local.LocalArtist;
import net.grabsalot.dao.local.LocalCollection;
import net.grabsalot.dao.local.LocalTrack;
import net.grabsalot.gui.CollectionTreePanel;

public class CollectionLoader extends Task {

	private CollectionTreeNode result = null;
	private final LocalCollection manager;

	public CollectionLoader(LocalCollection manager) {
		super("Collection loader");
		this.manager = manager;
	}

	private void fullLoad() {
		result = new CollectionTreeNode(manager);
		LocalArtist[] artists = manager.getArtists();

		for (int i = 0; i < artists.length; ++i) {
			CollectionTreeNode artistNode = new CollectionTreeNode(artists[i]);
			result.add(artistNode);
			LocalAlbum[] albums = artists[i].getAlbums();
			for (int j = 0; j < albums.length; ++j) {
				CollectionTreeNode albumNode = new CollectionTreeNode(albums[j]);
				artistNode.add(albumNode);
				LocalTrack[] tracks = albums[j].getTracks();
				for (int k = 0; k < tracks.length; ++k) {
					CollectionTreeNode trackNode = new CollectionTreeNode(tracks[k]);
					albumNode.add(trackNode);
				}
			}
		}
	}

	private void lazyLoad() {
	}

	@Override
	public void run() {
		//TODO create setting
		if (true) {
			fullLoad();
		} else {
			lazyLoad();
		}
	}

	/**
	 * @return the result
	 */
	public CollectionTreeNode getResult() {
		return result;
	}
}
