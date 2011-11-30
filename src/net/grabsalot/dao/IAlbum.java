package net.grabsalot.dao;

/**
 * An extension of the collection element interface that is specific to albums.
 *
 * @author madboyka
 *
 */
public interface IAlbum extends ICollectionElement {

	/**
	 * Returns the artist interface for this album.
	 *
	 * @return the artist of this album.
	 */
	public IArtist getArtist();
}
