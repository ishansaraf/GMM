import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
	JScrollPane shopScrollList;
	JTable table;
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
		
		//add shop display
		JPanel tablePanel = new JPanel();
		this.table = new JTable();
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
		JTableHeader header = this.table.getTableHeader();
		tablePanel.add(header);
		tablePanel.add(this.table);
		displayPanel.add(tablePanel);
		
		//set table properties
		this.table.setRowHeight(20);
		this.table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		this.table.setShowHorizontalLines(false);
		this.table.setShowVerticalLines(false);
		
		//set colors and font
		displayPanel.setBackground(Main.BG_COLOR2);
		tablePanel.setBackground(Main.BG_COLOR2);
		topPanel.setBackground(Main.BG_COLOR2);
		header.setBackground(Main.BG_COLOR2);
		header.setForeground(Main.TEXT_COLOR);
		header.setFont(Main.FIELD_FONT);
		this.table.setBackground(Main.BG_COLOR2);
		this.table.setForeground(Main.TEXT_COLOR);
		this.table.setFont(Main.FIELD_FONT);
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
		this.shopScrollList = new JScrollPane(this.shopList);
		this.shopScrollList.setViewportView(this.shopList);
		this.shopScrollList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		//set prefererred sizes
		this.eastPanel.setPreferredSize(new Dimension(600, 0));
		this.shopScrollList.setPreferredSize(new Dimension(295, 0));
		
		kickOffUpdateThreads();
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
		String shopName = this.shopList.getSelectedValue();
		CallableStatement cs = Main.conn.prepareCall("{call getLastTenOrders(?, ?)}");
		cs.setString(1, shopName);
		cs.setString(2, Main.MerchantID);
		ResultSet rs = cs.executeQuery();
		String[] names = {"Item", "Quantity", "Player", "Order Time"};
		TopOrdersTableModel model = new TopOrdersTableModel(rs, names);
		this.table.setModel(model);
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
