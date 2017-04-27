import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class MarketPage implements GMMPage{

	String MerchantID;
	JPanel eastPanel;
	DefaultListModel<String> updateListModel;
	JList<String> updateFeed;
	JScrollPane updateScrollFeed;
	DefaultListModel<String> shopListModel;
	JList<String> shopList;
	JScrollPane shopScrollList;
	Thread dataUpdater;
	Thread gameThread;
	private Updater updater;
	private GameHandler gameHandler;
	
	public MarketPage(String MerchantID) {
		this.MerchantID = MerchantID;
		
		//create panels
		this.eastPanel = new JPanel();
		
		//set BG colors
		this.eastPanel.setBackground(Color.lightGray);
		this.eastPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		//create the updateFeed Listbox for displaying updates
		this.updateListModel = new DefaultListModel<>();
		this.updateFeed = new JList<>(this.updateListModel);
		this.updateListModel.addElement("initilaized update feed!");
		
		//put the update feed into a JScrollPane for scrolling
		this.updateScrollFeed = new JScrollPane(this.updateFeed);
		this.updateScrollFeed.setViewportView(this.updateFeed);
		this.updateScrollFeed.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		//create the shopList Listbox for displaying updates
		this.shopListModel = new DefaultListModel<>();
		this.shopList = new JList<>(this.shopListModel);
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
			Main.mainframe.add(this.eastPanel, BorderLayout.EAST);
			Main.mainframe.add(this.shopScrollList, BorderLayout.WEST);
			Main.mainframe.add(this.updateScrollFeed, BorderLayout.SOUTH);
			System.out.println("MarketPage Loaded");	
		}
	}
	
	private void kickOffUpdateThreads() {
		//kick off a thread for processing updates
		this.updater = new Updater(this.updateListModel, this.updateFeed);
		this.dataUpdater = new Thread(this.updater);
		this.dataUpdater.start();
		
		//kick off a thread for collecting updates from the MMORPG
		this.gameHandler = new GameHandler(this.MerchantID);
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
	
}
