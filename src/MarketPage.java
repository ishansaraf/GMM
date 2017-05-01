import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
/**
 * 
 * Page for market data viewing
 *
 * @author Alan Holman
 *         Created Apr 30, 2017.
 */
public class MarketPage implements GMMPage{

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
	boolean atBottomOnUnshow;
	
	public MarketPage() {
		this.atBottomOnUnshow = false;
		
		//create panels
		this.eastPanel = new JPanel();
		
		//set BG colors
		this.eastPanel.setBackground(Main.BG_COLOR2);
		this.eastPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
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
	
}
