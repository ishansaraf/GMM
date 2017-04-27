import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
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
	JFrame frame;
	
	public MarketPage(String MerchantID, JFrame frame) {
		this.MerchantID = MerchantID;
		this.frame = frame;
		
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
		//add all elements into the frame
		this.frame.add(this.eastPanel, BorderLayout.EAST);
		this.frame.add(this.shopScrollList, BorderLayout.WEST);
		this.frame.add(this.updateScrollFeed, BorderLayout.SOUTH);
	}
	
	private void kickOffUpdateThreads() {
		//kick off a thread for processing updates
		Thread dataUpdater = new Thread(new Updater(this.frame, this.updateListModel, this.updateFeed));
		dataUpdater.start();
		
		//kick off a thread for collecting updates from the MMORPG
		Thread gameThread = new Thread(new GameHandler(this.MerchantID));
		gameThread.start();
	}
	
}
