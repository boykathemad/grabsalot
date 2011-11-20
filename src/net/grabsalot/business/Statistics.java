/*
package net.grabsalot.business;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQItem;
import javax.xml.xquery.XQResultSequence;

import net.grabsalot.gui.MainFrame;

import net.grabsalot.business.XQueryHelper;

public class Statistics {

	private JComponent statComponents[];

	public Statistics(String type) {
		statComponents = new JComponent[5];
		if (type == "genre") {
			try {
				InputStream query = getClass().getResourceAsStream("/org/grabsalot/resources/queries/GenresFromTracks.xq");
				FileInputStream xmlFile = new FileInputStream(new File(MainFrame.getInstance().getSource(), "collection.xml"));
				XQResultSequence result = XQueryHelper.runQuery(query, xmlFile);
				showGenreStatistics(result);
//			XQueryHelper.writeResult(result, System.out);
			} catch (XQException ex) {
				if (ex.getMessage().endsWith("type declaration must be well-formed.")) {
					JOptionPane.showMessageDialog(MainFrame.getInstance(), "Invalid markup in metadata file.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				ex.printStackTrace();
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			}
		}
		if (type == "size") {
			try {
				InputStream query = getClass().getResourceAsStream("/org/grabsalot/resources/queries/TrackFilesizeSum.xq");
				FileInputStream xmlFile = new FileInputStream(new File(MainFrame.getInstance().getSource(), "collection.xml"));
				XQResultSequence result = XQueryHelper.runQuery(query, xmlFile);
				showSizeStatistics(result);
//			XQueryHelper.writeResult(result, System.out);
			} catch (XQException ex) {
				if (ex.getMessage().endsWith("type declaration must be well-formed.")) {
					JOptionPane.showMessageDialog(MainFrame.getInstance(), "Invalid markup in metadata file.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				ex.printStackTrace();
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			}
		}
		addStatComponents();
	}

	private void showGenreStatistics(XQResultSequence results) throws XQException {
		XQItem item = null;
		ArrayList<Object[]> genres = new ArrayList<Object[]>();
		while (results.next()) {
			item = results.getItem();
			String itemName = item.getNode().getTextContent();
			if (itemName.matches(".*[0-9].*")) {
				continue;
			}
			genres.add(new Object[]{itemName});
		}

		Object[][] data = new Object[genres.size()][];
		int i = 0;
		for (Object[] dataItem : genres) {
			data[i] = dataItem;
			i++;
		}
		JTable table = new JTable(data, new Object[]{"Genre"});
		JTableHeader header = new JTableHeader();
		table.setTableHeader(header);

		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);
		statComponents[0] = table;
	}

	private void showSizeStatistics(XQResultSequence results) throws XQException {
		XQItem item = null;
		ArrayList<Object[]> genres = new ArrayList<Object[]>();
		results.next();
			item = results.getItem();
			String itemName = item.getNode().getTextContent();
			genres.add(new Object[]{itemName});
		Object[][] data = new Object[genres.size()][];
		int i = 0;
		for (Object[] dataItem : genres) {
			data[i] = dataItem;
			i++;
		}
		JTable table = new JTable(data, new Object[]{"Size"});
		JTableHeader header = new JTableHeader();
		table.setTableHeader(header);

		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);
		statComponents[0] = table;
	}

	private void addStatComponents() {
		JPanel infoPanel = MainFrame.getInstance().getInfoPanel();
		infoPanel.removeAll();
		for (JComponent component : statComponents) {
			if (component == null) {
				continue;
			}
			infoPanel.add(component);
		}
		infoPanel.revalidate();
		infoPanel.repaint();
	}
}
*/