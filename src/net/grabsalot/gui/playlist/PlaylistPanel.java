package net.grabsalot.gui.playlist;

import javax.swing.JPanel;
import javax.swing.JTable;

public class PlaylistPanel extends JPanel {

	private JTable list;

	public PlaylistPanel() {
		list = new JTable(new PlaylistTableModel());
	}



}
