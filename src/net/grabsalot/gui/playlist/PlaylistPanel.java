package net.grabsalot.gui.playlist;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import net.grabsalot.business.PlaylistListener;
import net.grabsalot.gui.MainFrame;

public class PlaylistPanel extends JPanel implements PlaylistListener {

	private JTable list;
	private PlaylistTableModel model;

	public PlaylistPanel() {
		setupComponents();
	}

	private void setupComponents() {

		setLayout(new BorderLayout());

		model = new PlaylistTableModel(MainFrame.getInstance().getPlaylist());
		model.getPlaylist().addPlaylistListener(this);
		list = new JTable(model);
		list.createDefaultColumnsFromModel();
		list.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					model.getPlaylist().setTrack(list.getSelectedRow());
				}
			}
		});
		add(new JScrollPane(list),BorderLayout.CENTER);
	}

	@Override
	public void itemsChanged() {
//		list.setModel(new DefaultTableModel());
		list.setModel(new PlaylistTableModel(MainFrame.getInstance().getPlaylist()));
		list.createDefaultColumnsFromModel();
		list.revalidate();
		list.repaint();
	}

	@Override
	public void playbackChanged() {
	}
}
