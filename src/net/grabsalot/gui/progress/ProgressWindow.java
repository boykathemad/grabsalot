/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.grabsalot.gui.progress;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import net.grabsalot.gui.MainFrame;

import net.grabsalot.gui.progress.ProgressWindow;

/**
 *
 * @author madboyka
 */
public class ProgressWindow extends JFrame {

	private JProgressBar progressBar;
	private JList list;
	private DefaultListModel listModel;
	private JPanel buttonsPanel;
	private MainFrame parent;
	private JButton closeButton;

	public ProgressWindow(MainFrame parent, String title) {
		this.parent = parent;
		parent.setAlwaysLoseFocus(true);
		this.setTitle(title);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		JPanel panel = new JPanel();
		BoxLayout layout = new BoxLayout(panel, WIDTH);
		panel.setLayout(layout);
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		listModel = new DefaultListModel();
		list = new JList(listModel);
		list.setVisibleRowCount(5);
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setSize(500, 300);
		panel.add(listScroller);
		progressBar = new JProgressBar();
		progressBar.setSize(600, 40);
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		progressBar.setValue(50);
		panel.add(progressBar);
		buttonsPanel = new JPanel(new FlowLayout());
		buttonsPanel.setSize(600, 30);
		buttonsPanel.setMaximumSize(new Dimension(9000, 50));
		closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ProgressWindow.this.close();
			}
		});
		buttonsPanel.add(closeButton);
		panel.add(buttonsPanel);

		add(panel);
		setSize(new Dimension(700, 500));
		setLocationRelativeTo(null);
	}

	public void display() {
		setVisible(true);
	}

	public void close() {
		parent.setAlwaysLoseFocus(false);
		setVisible(false);
		dispose();
	}

	public void showMessage(String string) {
		listModel.addElement(string);
	}
}
