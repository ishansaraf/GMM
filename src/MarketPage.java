import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
/**
 * 
 * Page for market data viewing
 *
 * @author Alan Holman
 *         Created Apr 30, 2017.
 */
public class MarketPage implements GMMPage{

	JScrollPane eastPanel;
	JButton refresh;
	JButton close;
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
	private JLabel ordersLabel;
	private JLabel itemsLabel;
	private JLabel fundsLabel;
	private String currShop = "";
	private JTable restocksTable;
	private JLabel restocksLabel;
	
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
	
	public class CloseListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				updateShop();
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(null, "Cannot update shop.");
				e1.printStackTrace();
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
		this.refresh = new MenuButton("Refresh", new RefreshListener());
		this.refresh.setVisible(false);
		JPanel topPanel = new JPanel();
		displayPanel.add(topPanel);
		topPanel.add(this.refresh);
		//add funds label
		this.fundsLabel = new JLabel("");
		this.fundsLabel.setForeground(Main.TEXT_COLOR);
		this.fundsLabel.setBackground(Main.BG_COLOR2);
		this.fundsLabel.setFont(Main.FIELD_FONT);
		this.fundsLabel.setVisible(false);
		displayPanel.add(this.fundsLabel);
		
		//add items table
		JPanel itemPanel = new JPanel();
		itemPanel.setBorder(BorderFactory.createMatteBorder(20, 0, 20, 0, Main.BG_COLOR2));
		this.itemsTable = new JTable() {
			
			@Override
			public void editingStopped(ChangeEvent e) {
				TableCellEditor editor = (TableCellEditor) e.getSource();
				int row = itemsTable.getEditingRow();
				int col = itemsTable.getEditingColumn();
				String value = (String) editor.getCellEditorValue();
				((ShopItemsTableModel) itemsTable.getModel()).update(row, col, value, shopList.getSelectedValue());
				editor.cancelCellEditing();
			}
		};
		this.itemsTable.putClientProperty("terminateEditOnFocusLost", true);
		itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
		JTableHeader itemsHeader = this.itemsTable.getTableHeader();
		this.itemsLabel = new JLabel("Inventory");
		this.itemsLabel.setForeground(Main.TEXT_COLOR);
		this.itemsLabel.setBackground(Main.BG_COLOR2);
		this.itemsLabel.setFont(Main.HEADER_FONT);
		this.itemsLabel.setVisible(false);
		itemPanel.add(this.itemsLabel);
		itemPanel.add(itemsHeader);
		itemPanel.add(this.itemsTable);
		displayPanel.add(itemPanel);
		
		//add orders table
		JPanel orderPanel = new JPanel();
		orderPanel.setBorder(BorderFactory.createMatteBorder(20, 0, 20, 0, Main.BG_COLOR2));
		this.ordersTable = new JTable();
		this.ordersTable.putClientProperty("terminateEditOnFocusLost", true);
		orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.Y_AXIS));
		JTableHeader ordersHeader = this.ordersTable.getTableHeader();
		this.ordersLabel = new JLabel("10 Most Recent Orders");
		this.ordersLabel.setForeground(Main.TEXT_COLOR);
		this.ordersLabel.setBackground(Main.BG_COLOR2);
		this.ordersLabel.setFont(Main.HEADER_FONT);
		this.ordersLabel.setVisible(false);
		orderPanel.add(this.ordersLabel);
		orderPanel.add(ordersHeader);
		orderPanel.add(this.ordersTable);
		displayPanel.add(orderPanel);
		
		//add restocks table
		JPanel restocksPanel = new JPanel();
		restocksPanel.setBorder(BorderFactory.createMatteBorder(20, 0, 20, 0, Main.BG_COLOR2));
		this.restocksTable = new JTable();
		this.restocksTable.putClientProperty("terminateEditOnFocusLost", true);
		restocksPanel.setLayout(new BoxLayout(restocksPanel, BoxLayout.Y_AXIS));
		JTableHeader restocksHeader = this.restocksTable.getTableHeader();
		this.restocksLabel = new JLabel("10 Most Recent Restocks");
		this.restocksLabel.setForeground(Main.TEXT_COLOR);
		this.restocksLabel.setBackground(Main.BG_COLOR2);
		this.restocksLabel.setFont(Main.HEADER_FONT);
		this.restocksLabel.setVisible(false);
		restocksPanel.add(this.restocksLabel);
		restocksPanel.add(restocksHeader);
		restocksPanel.add(this.restocksTable);
		displayPanel.add(restocksPanel);
		
		//close button
		this.close = new MenuButton("", new CloseListener());
		this.close.setVisible(false);
		JPanel botPanel = new JPanel();
		displayPanel.add(botPanel);
		botPanel.add(this.close);
		displayPanel.add(Box.createVerticalGlue());
		
		//set table properties
		this.ordersTable.setRowHeight(20);
		this.ordersTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.ordersTable.setShowHorizontalLines(false);
		this.ordersTable.setShowVerticalLines(false);
		this.restocksTable.setRowHeight(20);
		this.restocksTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.restocksTable.setShowHorizontalLines(false);
		this.restocksTable.setShowVerticalLines(false);
		this.itemsTable.setRowHeight(20);
		this.itemsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.itemsTable.setShowHorizontalLines(false);
		this.itemsTable.setShowVerticalLines(false);
		
		
		//set colors and font
		displayPanel.setBackground(Main.BG_COLOR2);
		orderPanel.setBackground(Main.BG_COLOR2);
		restocksPanel.setBackground(Main.BG_COLOR2);
		itemPanel.setBackground(Main.BG_COLOR2);
		topPanel.setBackground(Main.BG_COLOR2);
		botPanel.setBackground(Main.BG_COLOR2);
		ordersHeader.setBackground(Main.BG_COLOR2);
		ordersHeader.setForeground(Main.TEXT_COLOR);
		ordersHeader.setFont(Main.TABLE_FONT);
		restocksHeader.setBackground(Main.BG_COLOR2);
		restocksHeader.setForeground(Main.TEXT_COLOR);
		restocksHeader.setFont(Main.TABLE_FONT);
		itemsHeader.setBackground(Main.BG_COLOR2);
		itemsHeader.setForeground(Main.TEXT_COLOR);
		itemsHeader.setFont(Main.TABLE_FONT);
		this.ordersTable.setBackground(Main.BG_COLOR2);
		this.ordersTable.setForeground(Main.TEXT_COLOR);
		this.ordersTable.setFont(Main.TABLE_FONT);
		this.restocksTable.setBackground(Main.BG_COLOR2);
		this.restocksTable.setForeground(Main.TEXT_COLOR);
		this.restocksTable.setFont(Main.TABLE_FONT);
		this.itemsTable.setBackground(Main.BG_COLOR2);
		this.itemsTable.setForeground(Main.TEXT_COLOR);
		this.itemsTable.setFont(Main.TABLE_FONT);
		this.refresh.setFont(Main.FIELD_FONT);
		this.close.setFont(Main.FIELD_FONT);
		
		
		//create the updateFeed Listbox for displaying updates
		this.updateListModel = new DefaultListModel<>();
		this.updateFeed = new JList<>(this.updateListModel);
		this.updateFeed.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
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
		this.shopList.setFont(Main.LIST_FONT);
		
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
				// nothing
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
				//nothing
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
		this.eastPanel.setPreferredSize(new Dimension(Main.WINDOW_SIZE_X - 200, 0));
		this.shopScrollList.setPreferredSize(new Dimension(195, 0));
		
		kickOffUpdateThreads();
	}
	
	public void search() {
		String text = this.search.getText().trim().toLowerCase();
		this.shopListModel = new DefaultListModel<>();
		if (text.equals("") || text.equals("*")) {
			for (String ShopID : Main.getShopList()) {
				this.shopListModel.addElement(ShopID);
			}
		} else {
			for (String ShopID : Main.getShopList()) {
				if (ShopID.toLowerCase().contains(text)) {
					this.shopListModel.addElement(ShopID);
				}
			}
		}
		this.shopList.setModel(this.shopListModel);
	}
	
	@Override
	public void changeToPage() {
		if (Main.curPage != this) {
			if (Main.curPage != null) Main.curPage.unShow();
			this.currShop = "";
			Main.mainframe.add(this.eastPanel, BorderLayout.EAST);
			Main.mainframe.add(this.shopScrollList, BorderLayout.WEST);
			Main.mainframe.add(this.updateScrollFeed, BorderLayout.SOUTH);
			Main.curPage = this;
			Main.mainframe.revalidate();
			Main.mainframe.repaint();
			
			try {
				this.populateShopList();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Cannot populate shop list.");
				e.printStackTrace();
			}

			this.refresh.setVisible(false);
			this.close.setVisible(false);
			this.fundsLabel.setVisible(false);
			if (this.atBottomOnUnshow) this.updateFeed.ensureIndexIsVisible(this.updateListModel.size()-1);
			System.out.println("MarketPage Loaded");
		}
	}
	
	private void populateShopList() throws SQLException {
		this.shopListModel = new DefaultListModel<>();
		for (String ShopID : Main.getShopList()) {
			String shopName = ShopID;
			CallableStatement cs4;
			cs4 = Main.conn.prepareCall("{? = call getShopStatus(?, ?, ?)}");
			cs4.registerOutParameter(4, Types.BOOLEAN);
			cs4.registerOutParameter(1, Types.INTEGER);
			cs4.setString(2, shopName);
			cs4.setString(3, Main.MerchantID);
			cs4.execute();
			boolean closed = cs4.getBoolean(4);
			if (closed) {
				shopName = "[CLOSED] " + shopName;
			}
			System.out.println(shopName);
			this.shopListModel.addElement(shopName);
		}
		this.shopList.setModel(this.shopListModel);
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
	
	public void updateShop() throws SQLException {
		String shopName = this.currShop.replace("[CLOSED] ", "");
		CallableStatement cs = Main.conn.prepareCall("{? = call updateShop(?, ?, ?)}");
		cs.registerOutParameter(1, Types.INTEGER);
		cs.setString(2, shopName);
		cs.setString(3, Main.MerchantID);
		if (this.close.getText().equals("Close Shop")) {
			cs.setBoolean(4, true);
		} else if (this.close.getText().equals("Re-Open Shop")) {
			cs.setBoolean(4, false);
		} else {
			return;
		}
		cs.execute();
		this.populateShopList();
		this.shopList.revalidate();
		this.shopList.repaint();
	}
	
	public void refresh() throws SQLException {
		this.refresh.setVisible(true);
		this.ordersLabel.setVisible(false);
		this.restocksLabel.setVisible(false);
		this.itemsLabel.setVisible(false);
		this.ordersTable.setVisible(false);
		this.ordersTable.getTableHeader().setVisible(false);
		this.restocksTable.setVisible(false);
		this.restocksTable.getTableHeader().setVisible(false);
		this.itemsTable.setVisible(false);
		this.itemsTable.getTableHeader().setVisible(false);
		String shopName = this.currShop.replace("[CLOSED] ", "");
		CallableStatement cs1 = Main.conn.prepareCall("{call getLastNOrders(?, ?, ?)}");
		cs1.setString(1, shopName);
		cs1.setString(2, Main.MerchantID);
		cs1.setInt(3, Main.numOrdersShown);
		ResultSet ors = cs1.executeQuery();
		if (ors.isBeforeFirst()) {
			this.ordersLabel.setVisible(true);
			this.ordersTable.setVisible(true);
			this.ordersTable.getTableHeader().setVisible(true);
			String[] orderNames = {"Item", "Qty.", "Player", "Order Time"};
			TopOrdersTableModel ordersModel = new TopOrdersTableModel(ors, orderNames);
			this.ordersTable.setModel(ordersModel);
			TableColumnModel colModel = this.ordersTable.getColumnModel();
			colModel.getColumn(0).setPreferredWidth(160);
			colModel.getColumn(1).setPreferredWidth(5);
			colModel.getColumn(2).setPreferredWidth(160);
			colModel.getColumn(3).setPreferredWidth(120);
		}
		
		CallableStatement cs2 = Main.conn.prepareCall("{call getShopItems(?, ?)}");
		cs2.setString(1, shopName);
		cs2.setString(2, Main.MerchantID);
		ResultSet irs = cs2.executeQuery();
		if (irs.isBeforeFirst()) {
			this.itemsLabel.setVisible(true);
			this.itemsTable.setVisible(true);
			this.itemsTable.getTableHeader().setVisible(true);
			String[] itemNames = {"Item", "Qty.", "Price", "Description", "Disc."};
			ShopItemsTableModel itemsModel = new ShopItemsTableModel(irs, itemNames, this);
			this.itemsTable.setModel(itemsModel);
			TableColumnModel colModel = this.itemsTable.getColumnModel();
			colModel.getColumn(0).setPreferredWidth(160);
			colModel.getColumn(1).setPreferredWidth(5);
			colModel.getColumn(2).setPreferredWidth(5);
			colModel.getColumn(3).setPreferredWidth(300);
			colModel.getColumn(4).setPreferredWidth(5);
		}
		
		CallableStatement cs3 = Main.conn.prepareCall("{call getFunds(?, ?, ?)}");
		cs3.registerOutParameter(3, Types.DECIMAL);
		cs3.setString(1, shopName);
		cs3.setString(2, Main.MerchantID);
		cs3.execute();
		String funds = Double.toString(cs3.getDouble(3));
		funds = "Funds: " + funds;
		this.fundsLabel.setText(funds);
		this.fundsLabel.setVisible(true);

		CallableStatement cs4 = Main.conn.prepareCall("{? = call getShopStatus(?, ?, ?)}");
		cs4.registerOutParameter(4, Types.BOOLEAN);
		cs4.registerOutParameter(1, Types.INTEGER);
		cs4.setString(2, shopName);
		cs4.setString(3, Main.MerchantID);
		cs4.execute();
		boolean closed = cs4.getBoolean(4);
		if (closed) {
			this.close.setText("Re-Open Shop");
		} else {
			this.close.setText("Close Shop");
		}
		
		CallableStatement cs5 = Main.conn.prepareCall("{call getLastNRestocks(?, ?, ?)}");
		cs5.setString(1, shopName);
		cs5.setString(2, Main.MerchantID);
		cs5.setInt(3, Main.numOrdersShown);
		ResultSet rrs = cs5.executeQuery();
		if (rrs.isBeforeFirst()) {
			this.restocksLabel.setVisible(true);
			this.restocksTable.setVisible(true);
			this.restocksTable.getTableHeader().setVisible(true);
			String[] restockNames = {"Item", "Qty.", "Supplier", "Order Time"};
			TopRestocksTableModel restocksModel = new TopRestocksTableModel(rrs, restockNames);
			this.restocksTable.setModel(restocksModel);
			TableColumnModel colModel = this.restocksTable.getColumnModel();
			colModel.getColumn(0).setPreferredWidth(160);
			colModel.getColumn(1).setPreferredWidth(5);
			colModel.getColumn(2).setPreferredWidth(160);
			colModel.getColumn(3).setPreferredWidth(120);
		}
		this.close.setVisible(true);
		System.out.println("(re)loaded shop data!");
//		this.test.setText(shopName + " " + count);
//		count++;
		
	}
	
	class ShopListListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()){
				try {
					if (shopList.getSelectedValue() != null) {
						currShop = shopList.getSelectedValue();
					}
					refresh();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "Sorry, cannot display page.");
				}
			}
		}
		
	}
	
}
