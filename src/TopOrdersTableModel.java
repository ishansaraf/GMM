import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class TopOrdersTableModel extends AbstractTableModel{
	
	private String[] columnNames;
	private ArrayList<Order> list = new ArrayList<>();
	
	public TopOrdersTableModel(ResultSet rs, String[] names) throws SQLException {
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
		Order order = list.get(row);
		
		switch (col) {
		case 0:
			return order.item;
		case 1:
			return order.quantity;
		case 2:
			return order.player;
		case 3:
			return order.date;
		default:
			return order.item;
		}
	}
	
	public void convert(ResultSet rs) throws SQLException {
		while (rs.next()) {
			String item = rs.getString("Item");
			int quantity = rs.getInt("Quantity");
			String player = rs.getString("Player");
			Timestamp time = rs.getTimestamp("Order Time");
			Order order = new Order(item, Integer.toString(quantity), player, time.toString().substring(0, 19));
			this.list.add(order);
		}
	}

	class Order {
		
		String item;
		String quantity;
		String player;
		String date;
		
		public Order(String item, String quantity, String player, String date) {
			this.item = item;
			this.quantity = quantity;
			this.player = player;
			this.date = date;
		}
	}
	
}
