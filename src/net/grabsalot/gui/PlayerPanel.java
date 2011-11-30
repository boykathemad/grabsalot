package net.grabsalot.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.event.ChangeEvent;

import net.grabsalot.business.Playlist;
import net.grabsalot.dao.local.LocalTrack;
import net.grabsalot.util.GenericUtil;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeListener;
import net.grabsalot.business.Configuration;
import net.grabsalot.util.TickListener;
import net.grabsalot.util.TickTimer;

public class PlayerPanel extends JPanel implements TickListener {

	private static final long serialVersionUID = -6250331952952474748L;
	public JProgressBar prbSeek;
	private JButton btnPlay;
	private JLabel lblBla;
	public JLabel lblTime;
	private JLabel lblDuration;
	public Playlist playlist;
	private JSlider volumeSlider;
	private TickTimer timer;

	public PlayerPanel() {
		this.playlist = new Playlist();
		this.playlist.setPlayerPanel(this);
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

//		this.setBorder(new StrokeBorder(new BasicStroke(1)));
		this.setBorder(new BevelBorder(BevelBorder.RAISED));
		setPreferredSize(new Dimension(400, 40));
		setMaximumSize(new Dimension(400, 40));

		lblTime = new JLabel("0:00");
		this.add(lblTime);

		prbSeek = new JProgressBar();
		prbSeek.setPreferredSize(new Dimension(100, 30));
		this.add(prbSeek);

		lblDuration = new JLabel("0:00");
		this.add(lblDuration);

		btnPlay = new JButton(new String(Character.toChars(0x25FC)));
		this.btnPlay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (playlist.isPlaying()) {
					stopPlayback();
				} else {
					startPlayback();
				}
			}
		});
		this.add(btnPlay);

		int volume = Configuration.getInstance().getIntegerProperty("player.volume", 0);
		playlist.getPlayer().setVolume(volume);
		volumeSlider = new JSlider(-70, 0, volume);
		volumeSlider.setPreferredSize(new Dimension(200, 30));
		volumeSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				Configuration.getInstance().setProperty("player.volume", volumeSlider.getValue());
				playlist.getPlayer().setVolume(volumeSlider.getValue());
			}
		});
		this.add(volumeSlider);

		lblBla = new JLabel("bla");
//		this.add(lblBla);

		timer = new TickTimer(1000, this);
	}

	public void loadTrack(LocalTrack track) {
		if (playlist.isPlaying()) {
			this.stopPlayback();
		}
		playlist.clear();
		playlist.add(track);
	}

	public void updateTime() {
		int position = playlist.getPosition();
		String time = GenericUtil.getFormattedTime(position);
		lblTime.setText(time);
		prbSeek.setValue(position/1000);
	}

	public void updateDuration() {
		updateDuration(-1);
	}

	public void updateDuration(long duration) {
		long length = (duration > 0) ? duration : playlist.getTrackLength();
		lblDuration.setText(GenericUtil.getFormattedTime(length * 1000));
		prbSeek.setMaximum((int) length);
	}

	public void startPlayback() {
		playlist.play();
		updateDuration();
		timer.start();
	}

	public void stopPlayback() {
		playlist.stop();
		timer.stop();
	}

	public void clearPlaylist() {
		playlist.clear();
	}

	public void enqueueTrack(LocalTrack track) {
		this.playlist.add(track);
	}

	public void setTrack(LocalTrack track, long length) {
		this.lblBla.setText(track.getArtist() + " - " + track.getName());
		updateDuration(length);
	}

	@Override
	public void tick() {
		updateTime();
	}
}
