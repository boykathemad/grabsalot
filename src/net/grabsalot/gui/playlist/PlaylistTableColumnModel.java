package net.grabsalot.gui.playlist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

class PlaylistTableColumnModel extends DefaultTableColumnModel {

	private List<String> columnNames;
	private List<TableColumn> columns;

	public PlaylistTableColumnModel() {
		columnNames = new ArrayList<String>(Arrays.asList("#", "Artist", "Album", "Track no.", "Title"));
		columns = new ArrayList<TableColumn>();
		int i = 0;
		for (String colName : columnNames) {
			columns.add(new TableColumn(++i, 100));
		}
	}

	@Override
	public void addColumn(TableColumn aColumn) {
	}

	@Override
	public void removeColumn(TableColumn column) {
	}

	@Override
	public void moveColumn(int columnIndex, int newIndex) {
	}

	@Override
	public void setColumnMargin(int newMargin) {
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public Enumeration<TableColumn> getColumns() {
		return Collections.enumeration(columns);
	}

	@Override
	public int getColumnIndex(Object columnIdentifier) {
		return 0;
	}

	@Override
	public TableColumn getColumn(int columnIndex) {
		return columns.get(columnIndex);
	}
}
