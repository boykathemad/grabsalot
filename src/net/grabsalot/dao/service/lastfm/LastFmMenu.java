package net.grabsalot.dao.service.lastfm;

import java.awt.Dimension;
import java.awt.TextArea;

import net.grabsalot.business.Cacher;
import net.grabsalot.business.XmlTransformHelper;
import net.grabsalot.business.task.Task;
import net.grabsalot.business.task.TaskManager;
import net.grabsalot.gui.MainFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.BorderFactory;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import org.dom4j.Element;
import net.grabsalot.dao.service.lastfm.User;

public class LastFmMenu extends JMenu implements ActionListener {

	private static final long serialVersionUID = 2842835676026434254L;
	private JMenuItem mnuMimickUser;
	private JMenuItem mnuRecentTracks;

	public LastFmMenu() {
		super("Last.fm");
		this.setupItems();
	}

	private void setupItems() {
		this.mnuMimickUser = new JMenuItem("Mimick user");
		this.mnuMimickUser.addActionListener(this);
		this.add(this.mnuMimickUser);
		this.mnuRecentTracks = new JMenuItem("Show users recent tracks");
		this.mnuRecentTracks.addActionListener(this);
		this.add(this.mnuRecentTracks);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.mnuMimickUser)) {
			String username = JOptionPane.showInputDialog("Enter the last.fm username of user to mimick:", "madboyka");
			if (username != null) {
				try {
					User user = new User(username);
					Cacher.getMainFrame().setPlaylist(user.getRecentTracksPlaylist());
				} catch (Exception ex) {
				}
			}
		}
		if (e.getSource().equals(this.mnuRecentTracks)) {
			final String username = JOptionPane.showInputDialog("Enter the last.fm username:", "madboyka");
			if (username != null) {
				Task task = new Task() {

					@Override
					public void run() {
						try {
							User user = new User(username);
							Element recentTracks = user.getRecentTracks(20);
							InputStream styleSheet = getClass().getResourceAsStream("/org/grabsalot/resources/transforms/RecentTracks.xsl");
							InputStream sourceXml = new ByteArrayInputStream(recentTracks.asXML().getBytes());
							ByteArrayOutputStream output = (ByteArrayOutputStream) XmlTransformHelper.transform(styleSheet, sourceXml);
							JPanel infoPanel = MainFrame.getInstance().getInfoPanel();
							infoPanel.removeAll();
							JTextPane textPane = new JTextPane();
							textPane.setBorder(BorderFactory.createEmptyBorder());
							textPane.setContentType("text/html");
							textPane.setEditable(false);
							textPane.setPreferredSize(new Dimension(400, 200));
							textPane.setCaretPosition(0);
							textPane.setText(output.toString());
							infoPanel.add(new JScrollPane(textPane));
							infoPanel.revalidate();
							infoPanel.repaint();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				};
				TaskManager.getInstance().add(task);
				TaskManager.getInstance().start();
			}
		}
	}
}
