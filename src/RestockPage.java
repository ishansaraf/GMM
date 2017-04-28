import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

public class RestockPage implements GMMPage {

	public class SubmitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub.

		}

	}

	JPanel centerPanel;
	JComboBox<String> Shop;
	JComboBox<String> Supplier;
	JList<JPanel> Orders;
	DefaultListModel<JPanel> OrdersModel;
	
	public RestockPage() {
		//create panels
		this.centerPanel = new JPanel();
		JPanel shopPanel = new JPanel();
		JPanel supplierPanel = new JPanel();
		JPanel ordersPanel = new JPanel();
		JPanel submitPanel = new JPanel();

		//create Labels
		JLabel shopLabel = new JLabel("*Shop:     ");
		JLabel supplierLabel = new JLabel("*Supplier: ");
		
		//create button
		JButton submitButton = new MenuButton("Submit", new SubmitListener());
		
		//create Other
		this.Shop = new JComboBox<>();
		this.Supplier = new JComboBox<>();
		
		this.OrdersModel = new DefaultListModel<>();
		this.Orders = new JList<>(this.OrdersModel);
		
		JScrollPane ordersScrollPane = new JScrollPane(this.Orders);
		ordersScrollPane.setViewportView(this.Orders);
		ordersScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		ordersScrollPane.setPreferredSize(new Dimension(295, 400));
		this.addOrder();
		
		//populate Shop ComboBox
		List<String> tempArrList = Main.getShopList();
		String[] modelArray = new String[tempArrList.size()+1];
		modelArray[0] = "                      ";
		for (int i = 0; i < modelArray.length-1; i++) {
			modelArray[i+1] = tempArrList.get(i);
		}
		this.Shop.setModel(new DefaultComboBoxModel<>(modelArray));
		
		//populate Supplier ComboBox
		tempArrList = Main.getSupplierList();
		modelArray = new String[tempArrList.size()+1];
		modelArray[0] = "                      ";
		for (int i = 0; i < modelArray.length-1; i++) {
			modelArray[i+1] = tempArrList.get(i);
		}
		this.Supplier.setModel(new DefaultComboBoxModel<>(modelArray));
		
		//set fonts
		shopLabel.setFont(Main.FIELD_FONT);
		supplierLabel.setFont(Main.FIELD_FONT);
		submitButton.setFont(Main.FIELD_FONT);
		this.Shop.setFont(Main.FIELD_FONT);
		this.Supplier.setFont(Main.FIELD_FONT);
		this.Orders.setFont(Main.FIELD_FONT);		
		
		//add stuff to stuff
		shopPanel.add(shopLabel);
		shopPanel.add(this.Shop);
		supplierPanel.add(supplierLabel);
		supplierPanel.add(this.Supplier);
		ordersPanel.add(this.Orders);
		submitPanel.add(submitButton);
		this.centerPanel.setLayout(new BoxLayout(this.centerPanel, BoxLayout.PAGE_AXIS));
		JLabel header = new JLabel("Add a Supplier", SwingConstants.CENTER);
		header.setFont(Main.HEADER_FONT);
		this.centerPanel.add(header);
		this.centerPanel.add(shopPanel);
		this.centerPanel.add(supplierPanel);
		this.centerPanel.add(ordersScrollPane);
		this.centerPanel.add(submitPanel);
		
		//set BG colors
		this.centerPanel.setBackground(Main.BG_COLOR);
		shopPanel.setBackground(Main.BG_COLOR);
		supplierPanel.setBackground(Main.BG_COLOR);
		ordersPanel.setBackground(Main.BG_COLOR);
		submitPanel.setBackground(Main.BG_COLOR);
	}
	
	private void addOrder() {
		JPanel orderPanel = new JPanel();
		
		JComboBox<String> Item = new JComboBox<>();
		JTextField Quantity = new JTextField();
		
		Item.setFont(Main.FIELD_FONT);
		Quantity.setFont(Main.FIELD_FONT);
		
		orderPanel.add(Item);
		orderPanel.add(Quantity);
		
		orderPanel.setBackground(Main.BG_COLOR);
		Item.setBackground(Main.FIELD_COLOR);
		Quantity.setBackground(Main.FIELD_COLOR);
		
		this.OrdersModel.addElement(orderPanel);
		Main.mainframe.repaint();
	}

	@Override
	public void changeToPage() {
		if (Main.curPage != this) {
			Main.curPage.unShow();
			Main.mainframe.add(this.centerPanel, BorderLayout.CENTER);
			Main.curPage = this;
			Main.mainframe.revalidate();
			Main.mainframe.repaint();
			System.out.println("RestockPage Loaded");
		}
	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub.

	}

	@Override
	public void unShow() {
		if (this.centerPanel != null) Main.mainframe.remove(this.centerPanel);
		System.out.println("RestockPage Unloaded");
	}

}
