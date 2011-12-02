/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.grabsalot.gui.playlist;

import javax.swing.table.AbstractTableModel;
import net.grabsalot.business.Playlist;
import net.grabsalot.business.PlaylistListener;
import net.grabsalot.dao.local.LocalTrack;

/**
 *
 * @author madboyka
 */
public class PlaylistTableModel extends AbstractTableModel {

	protected Playlist playlist;

	public PlaylistTableModel(Playlist playlist) {
		this.playlist = playlist;
	}

	@Override
	public int getRowCount() {
		return playlist.getTrackCount();
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		LocalTrack track = playlist.getTrack(rowIndex);
		switch (columnIndex) {
			case 0:
				return rowIndex;
			case 1:
				return track.getArtist();
			case 2:
				return track.getAlbum();
			case 3:
				return track.getTrackNumber();
			case 4:
				return track.getName();
			default:
				return null;
		}
	}

	@Override
	public String getColumnName(int column) {
				switch (column) {
			case 0:
				return "#";
			case 1:
				return "Artist";
			case 2:
				return "Album";
			case 3:
				return "Track no.";
			case 4:
				return "Title";
			default:
				return null;
		}
	}

	public void setPlaylist(Playlist playlist) {
		this.playlist = playlist;
	}

	public Playlist getPlaylist() {
		return playlist;
	}

}
