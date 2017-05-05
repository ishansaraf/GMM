import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;


public class ShopItemsTableModel extends AbstractTableModel{
	
	private String[] columnNames;
	private ArrayList<Item> list = new ArrayList<>();
	
	public ShopItemsTableModel(ResultSet rs, String[] names) throws SQLException {
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
		Item item = list.get(row);
		
		switch (col) {
		case 0:
			return item.item;
		case 1:
			return item.quantity;
		case 2:
			return item.price;
		default:
			return item.item;
		}
	}
	
	public void convert(ResultSet rs) throws SQLException {
		while (rs.next()) {
			String name = rs.getString("Item");
			int quantity = rs.getInt("Quantity");
			double price = rs.getDouble("Unit Price");
			Item item = new Item(name, Integer.toString(quantity), Double.toString(price));
			list.add(item);
			
		}
	}

	class Item {
		
		String item, quantity, price;
		
		public Item(String item, String quantity, String price) {
			this.item = item;
			this.quantity = quantity;
			this.price = price;
		}
	}
	
}
