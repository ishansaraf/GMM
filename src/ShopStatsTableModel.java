import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.table.AbstractTableModel;


public class ShopStatsTableModel extends AbstractTableModel{
	
	private String[] columnNames;
	private ArrayList<Stats> list = new ArrayList<>();
	
	public ShopStatsTableModel(ResultSet rs, String[] names) throws SQLException {
		this.columnNames = names;
		convert(rs);
	}

	@Override
	public int getColumnCount() {
		return this.columnNames.length;
	}

	@Override
	public int getRowCount() {
		return list.size();
	}
	
	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col) {
		Stats stats = list.get(row);
		
		switch (col) {
		case 0:
			return stats.name;
		case 1:
			return stats.sold;
		case 2:
			return stats.stock;
		case 3:
			return stats.unsold;
		default:
			return stats.name;
		}
	}

	public void convert(ResultSet rs) throws SQLException {
		while (rs.next()) {
			String name = rs.getString("Name");
			int sold = rs.getInt("QSold");
			int total = rs.getInt("STotal");
			int unsold = rs.getInt("QNotSold");
			Stats stats = new Stats(name, Integer.toString(sold), Integer.toString(total), Integer.toString(unsold));
			list.add(stats);
		}
	}

	class Stats {
		
		String name, sold, stock, unsold;
		
		public Stats(String name, String sold, String stock, String unsold) {
			this.name = name;
			this.sold = sold;
			this.stock = stock;
			this.unsold = unsold;
		}
	}
	
}
