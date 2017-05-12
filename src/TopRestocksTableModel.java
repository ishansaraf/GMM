import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class TopRestocksTableModel extends AbstractTableModel{
	
	private String[] columnNames;
	private ArrayList<Restock> list = new ArrayList<>();
	
	public TopRestocksTableModel(ResultSet rs, String[] names) throws SQLException {
		this.columnNames = names;
		convert(rs);
//		for (Order order : list) {
//			System.out.println(order.item);
//		}
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
		Restock restock = list.get(row);
		
		switch (col) {
		case 0:
			return restock.item;
		case 1:
			return restock.quantity;
		case 2:
			return restock.supplier;
		case 3:
			return restock.date;
		default:
			return restock.item;
		}
	}
	
	public void convert(ResultSet rs) throws SQLException {
		while (rs.next()) {
			String item = rs.getString("Item");
			int quantity = rs.getInt("Quantity");
			String supplier = rs.getString("Supplier");
			Timestamp time = rs.getTimestamp("Order Time");
			Restock restock = new Restock(item, Integer.toString(quantity), supplier, time.toString().substring(0, 19));
			this.list.add(restock);
		}
	}

	class Restock {
		
		String item;
		String quantity;
		String supplier;
		String date;
		
		public Restock(String item, String quantity, String supplier, String date) {
			this.item = item;
			this.quantity = quantity;
			this.supplier = supplier;
			this.date = date;
		}
	}
	
}
