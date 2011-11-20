package net.grabsalot.dao;

/** The basic interface for all local and non-local elements(artists, albums...).
 * @author madboyka
 *
 */
public interface ICollectionElement {
	
	/** Return the name of the element.
	 * @return
	 */
	public abstract String getName();
}
