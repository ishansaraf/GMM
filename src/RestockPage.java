import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;

public class RestockPage implements GMMPage {

	public class SubmitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			placeOrder();
		}

	}

	private static int NUM_ROWS = 20;
	JPanel centerPanel;
	JComboBox<String> Shop;
	JComboBox<String> Supplier;
	JPanel Orders;

	public RestockPage() {
		// create panels
		this.centerPanel = new JPanel();
		JPanel shopPanel = new JPanel();
		JPanel supplierPanel = new JPanel();
		JPanel ordersPanel = new JPanel();
		JPanel submitPanel = new JPanel();
		JPanel headerPanel = new JPanel();

		// create Labels
		JLabel shopLabel = new JLabel("*Shop:     ");
		JLabel supplierLabel = new JLabel("*Supplier: ");
		JLabel ordersHeaderLabel = new JLabel("Item:                      Quantity:         ");

		// create button
		JButton submitButton = new MenuButton("Submit", new SubmitListener());

		// create Other
		this.Shop = new JComboBox<>();
		this.Shop.addActionListener(new ShopListener());
		this.Supplier = new JComboBox<>();

		this.Orders = new JPanel(new GridLayout(NUM_ROWS, 1));

		JScrollPane ordersScrollPane = new JScrollPane(this.Orders);
		// ordersScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		ordersScrollPane.setPreferredSize(new Dimension(800, 308));
		for (int i = 0; i < NUM_ROWS; i++) {
			this.addOrder();
		}
		ordersScrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());

		// populate Shop ComboBox
		List<String> tempArrList = Main.getShopList();
		String[] modelArray = new String[tempArrList.size() + 1];
		modelArray[0] = "                         ";
		for (int i = 0; i < modelArray.length - 1; i++) {
			modelArray[i + 1] = tempArrList.get(i);
		}
		this.Shop.setModel(new DefaultComboBoxModel<>(modelArray));
		this.Shop.setRenderer(new CSCListCellRenderer(Main.BG_COLOR2));

		// populate Supplier ComboBox
		tempArrList = Main.getSupplierList();
		modelArray = new String[tempArrList.size() + 1];
		modelArray[0] = "                         ";
		for (int i = 0; i < modelArray.length - 1; i++) {
			modelArray[i + 1] = tempArrList.get(i);
		}
		this.Supplier.setModel(new DefaultComboBoxModel<>(modelArray));
		this.Supplier.setRenderer(new CSCListCellRenderer(Main.BG_COLOR2));

		// set fonts
		shopLabel.setFont(Main.FIELD_FONT);
		supplierLabel.setFont(Main.FIELD_FONT);
		ordersHeaderLabel.setFont(Main.FIELD_FONT);
		submitButton.setFont(Main.FIELD_FONT);
		this.Shop.setFont(Main.FIELD_FONT);
		this.Supplier.setFont(Main.FIELD_FONT);
		this.Orders.setFont(Main.FIELD_FONT);

		// set foreground
		shopLabel.setForeground(Main.TEXT_COLOR);
		supplierLabel.setForeground(Main.TEXT_COLOR);
		ordersHeaderLabel.setForeground(Main.TEXT_COLOR);
		submitButton.setForeground(Main.TEXT_COLOR);
		this.Shop.setForeground(Main.TEXT_COLOR);
		this.Supplier.setForeground(Main.TEXT_COLOR);
		this.Orders.setForeground(Main.TEXT_COLOR);

		// add stuff to stuff
		shopPanel.add(shopLabel);
		shopPanel.add(this.Shop);
		supplierPanel.add(supplierLabel);
		supplierPanel.add(this.Supplier);
		ordersPanel.add(ordersScrollPane);
		submitPanel.add(submitButton);
		this.centerPanel.setLayout(new BoxLayout(this.centerPanel, BoxLayout.PAGE_AXIS));
		JLabel header = new JLabel("   Restock a Store", SwingConstants.CENTER);
		header.setFont(Main.HEADER_FONT);
		header.setForeground(Main.TEXT_COLOR);
		headerPanel.add(header);
		this.centerPanel.add(headerPanel);
		this.centerPanel.add(shopPanel);
		this.centerPanel.add(supplierPanel);
		this.centerPanel.add(ordersHeaderLabel);
		this.centerPanel.add(ordersPanel);
		this.centerPanel.add(submitPanel);

		// set BG colors
		this.centerPanel.setBackground(Main.BG_COLOR);
		shopPanel.setBackground(Main.BG_COLOR);
		supplierPanel.setBackground(Main.BG_COLOR);
		ordersPanel.setBackground(Main.BG_COLOR);
		submitPanel.setBackground(Main.BG_COLOR);
		headerPanel.setBackground(Main.BG_COLOR);
		this.Orders.setBackground(Main.BG_COLOR);
		this.Shop.setBackground(Main.FIELD_COLOR);
		this.Supplier.setBackground(Main.FIELD_COLOR);
	}

	private void addOrder() {
		JPanel orderPanel = new JPanel();

		JComboBox<String> Item = new JComboBox<>();
		JLabel Spacer = new JLabel("     ");
		JTextField Quantity = new JTextField(4);
		((AbstractDocument) Quantity.getDocument()).setDocumentFilter(new LimitDocumentFilter(4));

		// populate ComboBox
		List<String> tempArrList = Main.getItemList();
		String[] modelArray = new String[tempArrList.size() + 1];
		modelArray[0] = "                    ";
		for (int i = 0; i < modelArray.length - 1; i++) {
			modelArray[i + 1] = tempArrList.get(i);
		}
		ComboBoxModel<String> ItemModel = new DefaultComboBoxModel<>(modelArray);
		Item.setModel(ItemModel);
		Item.setRenderer(new CSCListCellRenderer(Main.BG_COLOR2));

		// set fonts
		Item.setFont(Main.FIELD_FONT);
		Quantity.setFont(Main.FIELD_FONT);
		Spacer.setFont(Main.FIELD_FONT);

		// set foreground color
		Item.setForeground(Main.TEXT_COLOR);
		Quantity.setForeground(Main.TEXT_COLOR);

		// add to panel
		orderPanel.add(Item);
		orderPanel.add(Spacer);
		orderPanel.add(Quantity);

		// alternate panel colors
		if (this.Orders.getComponentCount() % 2 == 0) {
			orderPanel.setBackground(Main.BG_COLOR2);
		} else {
			orderPanel.setBackground(Main.BG_COLOR);
		}

		// set background colors
		Item.setBackground(Main.FIELD_COLOR);
		Quantity.setBackground(Main.FIELD_COLOR);

		// add and refresh
		this.Orders.add(orderPanel);
		Main.mainframe.repaint();
	}

	private void placeOrder() {
		String shop = ((String) this.Shop.getSelectedItem()).trim();
		String supplier = ((String) this.Supplier.getSelectedItem()).trim();
		if (shop.equals("")) {
			JOptionPane.showMessageDialog(null, "Please select a shop");
			return;
		}
		if (supplier.equals("")) {
			JOptionPane.showMessageDialog(null, "Please select a supplier");
			return;
		}
		int index = supplier.lastIndexOf("[");
		supplier = supplier.substring(0, index - 1);
		ArrayList<String> items = new ArrayList<>();
		ArrayList<Integer> quantities = new ArrayList<>();
		for (int i = 0; i < NUM_ROWS; i++) {
			JPanel order = (JPanel) this.Orders.getComponent(i);
			JComboBox<String> box = (JComboBox<String>) order.getComponent(0);
			String item = ((String) box.getSelectedItem()).trim();
			if (item.equals("")) {
				continue;
			}
			JTextField text = (JTextField) order.getComponent(2);
			String q = text.getText().trim();
			if (q.equals("")) {
				JOptionPane.showMessageDialog(null, "Please enter a quantity for item " + item + ".");
				return;
			}
			int quantity = 0;
			try {
				quantity = Integer.parseInt(q);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Please enter a whole number quantity for item " + item + ".");
				return;
			}
			items.add(item);
			quantities.add(quantity);
		}
		if (items.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No items selected.\nNo stock orders placed.");
			return;
		}
		
		for (int i = 0; i < items.size(); i++) {
			try {
				CallableStatement cs = Main.conn.prepareCall("{? = call addStockOrder(?, ?, ?, ?, ?)}");
				cs.registerOutParameter(1, Types.INTEGER);
				cs.setString(2, shop);
				cs.setString(3, supplier);
				cs.setString(4, items.get(i));
				cs.setInt(5, quantities.get(i));
				cs.setString(6, Main.MerchantID);
				cs.executeUpdate();
				int status = cs.getInt(1);
				if (status != 0) {
					if (status == 1) {
						JOptionPane.showMessageDialog(null,
								"Error RP1" + "\nShop: " + shop + "\nSupplier: " + supplier + "\nItem: " + items.get(i)
										+ "\nQuantity: " + quantities.get(i) + "\nOrder may be partially completed.");
						return;
					}
					if (status == 2) {
						JOptionPane.showMessageDialog(null,
								"Insufficient funds in shop.\nOrder may be partially completed.");
						return;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Error RP0" + "\nShop: " + shop + "\nSupplier: " + supplier + "\nItem: " + items.get(i)
								+ "\nQuantity: " + quantities.get(i) + "\nOrder may be partially completed.");
				return;
			}

		}
		JOptionPane.showMessageDialog(null, "Orders completed!");
		this.changeToPage();
	}

	private void updateComboBoxes() {
		// update Shop ComboBox
		List<String> tempArrList = Main.getShopList();
		if (this.Shop.getModel().getSize() - 1 != tempArrList.size()) {
			String[] modelArray = new String[tempArrList.size() + 1];
			modelArray[0] = "                      ";
			for (int i = 0; i < modelArray.length - 1; i++) {
				modelArray[i + 1] = tempArrList.get(i);
			}
			this.Shop.setModel(new DefaultComboBoxModel<>(modelArray));
			this.Shop.setRenderer(new CSCListCellRenderer(Main.BG_COLOR2));
		}
		// update Supplier ComboBox
		tempArrList = Main.getSupplierList();
		if (this.Supplier.getModel().getSize() - 1 != tempArrList.size()) {
			String[] modelArray = new String[tempArrList.size() + 1];
			modelArray[0] = "                      ";
			for (int i = 0; i < modelArray.length - 1; i++) {
				modelArray[i + 1] = tempArrList.get(i);
			}
			this.Supplier.setModel(new DefaultComboBoxModel<>(modelArray));
			this.Supplier.setRenderer(new CSCListCellRenderer(Main.BG_COLOR2));
		}
	}

	@Override
	public void changeToPage() {
		if (Main.curPage != this) {
			Main.curPage.unShow();
			Main.mainframe.add(this.centerPanel, BorderLayout.CENTER);
			Main.curPage = this;
			updateComboBoxes();
			clearOrderPanel();
			Main.mainframe.revalidate();
			Main.mainframe.repaint();
			System.out.println("RestockPage Loaded");
		}
	}
	
	public void clearOrderPanel() {
		for (int i = 0; i < NUM_ROWS; i++) {
			JPanel order = (JPanel) this.Orders.getComponent(i);
			JComboBox<String> box = (JComboBox<String>) order.getComponent(0);
			box.setSelectedIndex(0);
			JTextField text = (JTextField) order.getComponent(2);
			text.setText("");
		}
	}

	@Override
	public void shutDown() {
		// nothing special

	}

	@Override
	public void unShow() {
		if (this.centerPanel != null)
			Main.mainframe.remove(this.centerPanel);
		System.out.println("RestockPage Unloaded");
	}

	class ShopListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JComboBox cb = (JComboBox) e.getSource();
			String shopName = ((String) cb.getSelectedItem()).trim();
			ArrayList<String> supplierList;
			String[] modelArray;
			if (shopName.equals("")) {
				supplierList = (ArrayList) Main.getSupplierList();
			} else {
				supplierList = (ArrayList) Main.getSupplierList(shopName);
			}
			modelArray = new String[supplierList.size() + 1];
			modelArray[0] = "                      ";
			for (int i = 0; i < modelArray.length - 1; i++) {
				modelArray[i + 1] = supplierList.get(i);
			}
			Supplier.setModel(new DefaultComboBoxModel<>(modelArray));
			Supplier.setRenderer(new CSCListCellRenderer(Main.BG_COLOR2));
//			Supplier.revalidate();
//			Supplier.repaint();
		}

	}

}
