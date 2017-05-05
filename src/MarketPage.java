import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
/**
 * 
 * Page for market data viewing
 *
 * @author Alan Holman
 *         Created Apr 30, 2017.
 */
public class MarketPage implements GMMPage{

	JScrollPane eastPanel;
	DefaultListModel<String> updateListModel;
	JList<String> updateFeed;
	JScrollPane updateScrollFeed;
	DefaultListModel<String> shopListModel;
	JList<String> shopList;
	JTextField search;
	JScrollPane shopScrollList;
	JTable ordersTable;
	JTable itemsTable;
	JScrollPane shopDisplayScroll;
	Thread dataUpdater;
	Thread gameThread;
	private Updater updater;
	private GameHandler gameHandler;
	boolean atBottomOnUnshow;
	int count = 0;
	
	public class RefreshListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// refreshes current shop displayed
			try {
				refresh();
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(null, "Sorry, cannot display page.");
			}	
		}
	}
	
	public MarketPage() {
		this.atBottomOnUnshow = false;
		
		//create panels
		JPanel displayPanel = new JPanel();
		displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
		this.eastPanel = new JScrollPane(displayPanel);
		this.eastPanel.setViewportView(displayPanel);
		this.eastPanel.getViewport().setBackground(Main.BG_COLOR2);
		this.eastPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.eastPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.eastPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		//panel with refresh button
		JButton refresh = new MenuButton("Refresh", new RefreshListener());
		JPanel topPanel = new JPanel();
		displayPanel.add(topPanel);
		topPanel.add(refresh);
		
		//add orders table
		JPanel orderPanel = new JPanel();
		orderPanel.setBorder(BorderFactory.createMatteBorder(20, 0, 20, 0, Main.BG_COLOR2));
		this.ordersTable = new JTable();
		orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.Y_AXIS));
		JTableHeader ordersHeader = this.ordersTable.getTableHeader();
		orderPanel.add(ordersHeader);
		orderPanel.add(this.ordersTable);
		displayPanel.add(orderPanel);
		
		//add items table
		JPanel itemPanel = new JPanel();
		itemPanel.setBorder(BorderFactory.createMatteBorder(20, 0, 20, 0, Main.BG_COLOR2));
		this.itemsTable = new JTable();
		itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
		JTableHeader itemsHeader = this.itemsTable.getTableHeader();
		itemPanel.add(itemsHeader);
		itemPanel.add(this.itemsTable);
		displayPanel.add(itemPanel);
		
		displayPanel.add(Box.createVerticalGlue());
		
		//set table properties
		this.ordersTable.setRowHeight(20);
		this.ordersTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.ordersTable.setShowHorizontalLines(false);
		this.ordersTable.setShowVerticalLines(false);
		this.itemsTable.setRowHeight(20);
		this.itemsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.itemsTable.setShowHorizontalLines(false);
		this.itemsTable.setShowVerticalLines(false);
		
		
		//set colors and font
		displayPanel.setBackground(Main.BG_COLOR2);
		orderPanel.setBackground(Main.BG_COLOR2);
		topPanel.setBackground(Main.BG_COLOR2);
		ordersHeader.setBackground(Main.BG_COLOR2);
		ordersHeader.setForeground(Main.TEXT_COLOR);
		ordersHeader.setFont(Main.FIELD_FONT);
		itemsHeader.setBackground(Main.BG_COLOR2);
		itemsHeader.setForeground(Main.TEXT_COLOR);
		itemsHeader.setFont(Main.FIELD_FONT);
		this.ordersTable.setBackground(Main.BG_COLOR2);
		this.ordersTable.setForeground(Main.TEXT_COLOR);
		this.ordersTable.setFont(Main.FIELD_FONT);
		this.itemsTable.setBackground(Main.BG_COLOR2);
		this.itemsTable.setForeground(Main.TEXT_COLOR);
		this.itemsTable.setFont(Main.FIELD_FONT);
		refresh.setFont(Main.FIELD_FONT);
		
		
		//create the updateFeed Listbox for displaying updates
		this.updateListModel = new DefaultListModel<>();
		this.updateFeed = new JList<>(this.updateListModel);
		this.updateFeed.setBackground(Main.BG_COLOR);
		this.updateFeed.setForeground(Main.TEXT_COLOR);
		this.updateFeed.setCellRenderer(new CSCListCellRenderer(Main.BG_COLOR));
		this.updateListModel.addElement("initilaized update feed!");
		
		//put the update feed into a JScrollPane for scrolling
		this.updateScrollFeed = new JScrollPane(this.updateFeed);
		this.updateScrollFeed.setViewportView(this.updateFeed);
		this.updateScrollFeed.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		//create the shopList Listbox for displaying updates
		this.shopListModel = new DefaultListModel<>();
		this.shopList = new JList<>(this.shopListModel);
		
		ListSelectionModel lsm = this.shopList.getSelectionModel();
		lsm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lsm.addListSelectionListener(new ShopListListener());
		
		this.shopList.setBackground(Main.BG_COLOR);
		this.shopList.setForeground(Main.TEXT_COLOR);
		this.shopList.setCellRenderer(new CSCListCellRenderer(Main.FIELD_COLOR));
		for (String ShopID : Main.getShopList()) {
			this.shopListModel.addElement(ShopID);
		}
		
		//put the shopList into a JScrollPane for scrolling
		JPanel shopsPanel = new JPanel();
		this.search = new JTextField();
		this.search.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER) {
					search();
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		shopsPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		shopsPanel.add(this.search, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		shopsPanel.add(this.shopList, c);
		c.weighty = 1;
		c.gridy = 2;
		JPanel filler = new JPanel();
		filler.setBackground(Main.BG_COLOR);
		shopsPanel.add(filler, c);
		shopsPanel.setBackground(Main.BG_COLOR);
		this.shopScrollList = new JScrollPane(shopsPanel);
		this.shopScrollList.setViewportView(shopsPanel);
		this.shopScrollList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		//set prefererred sizes
		this.eastPanel.setPreferredSize(new Dimension(600, 0));
		this.shopScrollList.setPreferredSize(new Dimension(295, 0));
		
		kickOffUpdateThreads();
	}
	
	public void search() {
		String text = this.search.getText().trim();
		this.shopListModel = new DefaultListModel<>();
		if (text.equals("") || text.equals("*")) {
			for (String ShopID : Main.getShopList()) {
				this.shopListModel.addElement(ShopID);
			}
		} else {
			for (String ShopID : Main.getShopList()) {
				if (ShopID.contains(text)) {
					this.shopListModel.addElement(ShopID);
				}
			}
		}
	}
	
	@Override
	public void changeToPage() {
		if (Main.curPage != this) {
			if (Main.curPage != null) Main.curPage.unShow();
			Main.mainframe.add(this.eastPanel, BorderLayout.EAST);
			Main.mainframe.add(this.shopScrollList, BorderLayout.WEST);
			Main.mainframe.add(this.updateScrollFeed, BorderLayout.SOUTH);
			Main.curPage = this;
			Main.mainframe.revalidate();
			Main.mainframe.repaint();
			
			this.shopListModel = new DefaultListModel<>();
			for (String ShopID : Main.getShopList()) {
				this.shopListModel.addElement(ShopID);
			}
			this.shopList.setModel(this.shopListModel);
			
			if (this.atBottomOnUnshow) this.updateFeed.ensureIndexIsVisible(this.updateListModel.size()-1);
			System.out.println("MarketPage Loaded");
		}
	}
	
	private void kickOffUpdateThreads() {
		//kick off a thread for processing updates
		this.updater = new Updater(this.updateListModel, this.updateFeed);
		this.dataUpdater = new Thread(this.updater);
		this.dataUpdater.start();
		
		//kick off a thread for collecting updates from the MMORPG
		this.gameHandler = new GameHandler(Main.MerchantID);
		this.gameThread = new Thread(this.gameHandler);
		this.gameThread.start();
	}
	
	@Override
	public void shutDown() {
		this.updater.shutDown();
		this.gameHandler.shutDown();
		try {
			this.dataUpdater.join();
			this.gameThread.join();
		} catch (InterruptedException exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void unShow() {
		if (this.updateFeed.getLastVisibleIndex() == this.updateListModel.size()-1) this.atBottomOnUnshow = true;
		else this.atBottomOnUnshow = false;
		if (this.eastPanel != null) Main.mainframe.remove(this.eastPanel);
		if (this.shopScrollList != null) Main.mainframe.remove(this.shopScrollList);
		if (this.updateScrollFeed != null) Main.mainframe.remove(this.updateScrollFeed);
		System.out.println("MarketPage Unloaded");
	}
	
	public void refresh() throws SQLException {
		this.ordersTable.setVisible(false);
		this.ordersTable.getTableHeader().setVisible(false);
		this.itemsTable.setVisible(false);
		this.itemsTable.getTableHeader().setVisible(false);
		String shopName = this.shopList.getSelectedValue();
		CallableStatement cs1 = Main.conn.prepareCall("{call getLastTenOrders(?, ?)}");
		cs1.setString(1, shopName);
		cs1.setString(2, Main.MerchantID);
		ResultSet ors = cs1.executeQuery();
		if (ors.isBeforeFirst()) {
			this.ordersTable.setVisible(true);
			this.ordersTable.getTableHeader().setVisible(true);
			String[] orderNames = {"Item", "Quantity", "Player", "Order Time"};
			TopOrdersTableModel ordersModel = new TopOrdersTableModel(ors, orderNames);
			this.ordersTable.setModel(ordersModel);
		}
		
		CallableStatement cs2 = Main.conn.prepareCall("{call getShopItems(?, ?)}");
		cs2.setString(1, shopName);
		cs2.setString(2, Main.MerchantID);
		ResultSet irs = cs2.executeQuery();
		if (irs.isBeforeFirst()) {
			this.itemsTable.setVisible(true);
			this.itemsTable.getTableHeader().setVisible(true);
			String[] itemNames = {"Item", "Quantity", "Unit Price"};
			ShopItemsTableModel itemsModel = new ShopItemsTableModel(irs, itemNames);
			this.itemsTable.setModel(itemsModel);
		}
//		this.test.setText(shopName + " " + count);
//		count++;
	}
	
	class ShopListListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()){
				try {
					refresh();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "Sorry, cannot display page.");
				}
			}
		}
		
	}
	
}
