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


public class ShopItemsTableModel extends AbstractTableModel{
	
	private String[] columnNames;
	private ArrayList<Item> list = new ArrayList<>();
	private MarketPage page;
	
	public ShopItemsTableModel(ResultSet rs, String[] names, MarketPage page) throws SQLException {
		this.columnNames = names;
		this.page = page;
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
	
	
	public void update(int row, int col, String value, String shop) {
		if (value.equals(this.getValueAt(row, col))) {
			return;
		}
		try {
			if (col != 4 && this.list.get(row).name.contains("[DISC.]")) {
				JOptionPane.showMessageDialog(null, "Please set Disc. to \"N\" before editing the item.");
				return;
			}
			String name = this.list.get(row).name.replace(" [DISC.]", "");
			//sql.append("{? = call updateItem (name => ?, shop => ?, merUID => ?");
			CallableStatement cs = Main.conn.prepareCall("{? = call updateItem(?, ?, ?, ?, ?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString("item", name);
			cs.setString("shop", shop);
			cs.setString("merUID", Main.MerchantID);

			switch(col) {
			case 2:
				double price = 0;
				try {
					price = Double.parseDouble((String) value);
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Please enter an valid price.");
					return;
				}
				cs.setDouble("price", price);
				cs.setNull("disc", Types.NULL);
				break;
			case 4:
				String disc = ((String) value).toUpperCase();
				if (!disc.equals("Y") && !disc.equals("N")) {
					JOptionPane.showMessageDialog(null, "Please only enter \"Y\" or \"N\" for the Discontinued field.");
					return;
				}
				boolean d = disc.equals("Y") ? true : false;
				cs.setBoolean("disc", d);
				cs.setNull("price", Types.NULL);
				break;
			default:
				return;
			}
			
			cs.execute();
			int status = cs.getInt(1);
			if (status != 0) {
				if (status == 1) {
					JOptionPane.showMessageDialog(null, "The item or shop selected no longer exists. Try refreshing.");
					return;
				}
				if (status == 2) {
					JOptionPane.showMessageDialog(null, "An item with the same name already exists."
							+ "\nIf need to place a stock order, please go to the Inform of Restock page."
							+ "\nIf need to add new item, please go to the Inform of New Item page.");
					return;
				}
			}
			this.page.refresh();
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int column){  
		if (column == 2 || column == 4) {
			return true;
		}
        return false;  
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
			return item.name;
		case 1:
			return item.quantity;
		case 2:
			return item.price;
		case 3:
			return item.description;
		case 4:
			return item.disc;
		default:
			return item.name;
		}
	}
	
//	@Override
//	public void setValueAt(Object value, int row, int col) {
//		Item item = list.get(row);
//		switch (col) {
//		case 2:
//			item.price = (String) value;
//			break;
//		case 4:
//			item.disc = (String) value;
//			if (item.disc.equals("Y")) {
//				item.name = item.name + " [Disc.]";
//			} else {
//				item.name = item.name.replace(" [Disc.]", "");
//			}
//			break;
//		default:
//			break;
//		}
//		try {
//			this.page.refresh();
//		} catch (SQLException e) {
//			JOptionPane.showMessageDialog(null, "Failed to refresh page after editing table.");
//			e.printStackTrace();
//		}
//	}
	
	public void convert(ResultSet rs) throws SQLException {
		while (rs.next()) {
			String name = rs.getString("Item");
			int quantity = rs.getInt("Quantity");
			double price = rs.getDouble("Unit Price");
			String description = rs.getString("Description");
			boolean disc = rs.getBoolean("Discontinued");
			Item item = new Item(name, Integer.toString(quantity), Double.toString(price), description, disc);
			list.add(item);
			
		}
	}

	class Item {
		
		String name, quantity, price, description, disc;
		
		public Item(String name, String quantity, String price, String description, boolean disc) {
			this.name = name;
			this.quantity = quantity;
			this.price = price;
			if (description.equals("")) {
				this.description = "None";
			} else {
				this.description = description;
			}
			if (disc) {
				this.disc = "Y";
				this.name += " [DISC.]";
			} else {
				this.disc = "N";
			}
		}
	}
	
}
