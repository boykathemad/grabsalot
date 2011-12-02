package net.grabsalot.gui;

import net.grabsalot.gui.components.JImageBox;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class ImageViewerFrame extends JFrame {

	private JImageBox box;
	private JScrollPane panel;
	private String title = "";

	public ImageViewerFrame() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		box = new JImageBox();
		panel = new JScrollPane(box);
		panel.setBorder(new EmptyBorder(0, 0, 0, 0));
		add(panel, BorderLayout.CENTER);
		addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					dispose();
				}
			}
		});

//		setResizable(false);
	}

	public void showImage(Image image) {
		box.setImage(image);
		if (image != null) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension size = new Dimension(image.getWidth(null), image.getHeight(null));
			if (screenSize.height <= size.height || screenSize.width <= size.width) {
				setExtendedState(JFrame.MAXIMIZED_BOTH);
			} else {
				this.panel.setPreferredSize(size);
			}
			this.pack();
			setTitle(title + " (" + size.getWidth() + " x " + size.getHeight() + ")");
		}
		this.setLocationRelativeTo(MainFrame.getInstance());
		this.setVisible(true);
	}
}
