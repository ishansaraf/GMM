import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

/**
 * 
 * Main Interface Application for the GMM (Generic MMORPG Merchant) marketing solutions database
 *
 * @author Alan Holman
 *         Created Apr 14, 2017.
 */
public class Main {
	
	static String MerchantID;
	static Queue<String> updateQueue = new LinkedList<>();
	
	public static void main(String[] args) {
		//login
		login();
		
		//set up frame
		JFrame frame = new JFrame();
		frame.setSize(900, 600);
		frame.setTitle("GMM Marketing Solutions");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//create panels
		JPanel northPanel = new JPanel();
		JPanel eastPanel = new JPanel();
		
		//set BG colors
		northPanel.setBackground(Color.GRAY);
		eastPanel.setBackground(Color.lightGray);
		eastPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		//create the updateFeed Listbox for displaying updates
		DefaultListModel<String> updateListModel = new DefaultListModel<>();
		JList<String>updateFeed = new JList<>(updateListModel);
		updateListModel.addElement("initilaized update feed!");
		
		//put the update feed into a JScrollPane for scrolling
		JScrollPane updateScrollFeed = new JScrollPane(updateFeed);
		updateScrollFeed.setViewportView(updateFeed);
		updateScrollFeed.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		//create the shopList Listbox for displaying updates
		DefaultListModel<String> shopListModel = new DefaultListModel<>();
		JList<String>shopList = new JList<>(shopListModel);
		for (String ShopID : getShopList()) {
			shopListModel.addElement(ShopID);
		}
		
		//put the shopList into a JScrollPane for scrolling
		JScrollPane shopScrollList = new JScrollPane(shopList);
		shopScrollList.setViewportView(shopList);
		shopScrollList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		//set prefererred sizes
		northPanel.setPreferredSize(new Dimension(900, 30));
		eastPanel.setPreferredSize(new Dimension(600, 0));
		shopScrollList.setPreferredSize(new Dimension(295, 0));
		
		//add all elements into the frame
		frame.add(northPanel, BorderLayout.NORTH);
		frame.add(eastPanel, BorderLayout.EAST);
		frame.add(shopScrollList, BorderLayout.WEST);
		frame.add(updateScrollFeed, BorderLayout.SOUTH);
		
		//center and show the window
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		//kick off a thread for processing updates
		Thread dataUpdater = new Thread(new Updater(frame, updateListModel, updateFeed));
		dataUpdater.start();
		
		//kick off a thread for collecting updates from the MMORPG
		Thread gameThread = new Thread(new GameHandler(MerchantID));
		gameThread.start();
	}
	
	/**
	 *  TODO gets a list of shops owned by the Merchant using query
	 *
	 * @return List<String>
	 */
	public static List<String> getShopList() {
		//DEBUG CODE START
		List<String> dummyList = new ArrayList<>();
		for (int i = 1; i <= 30; i++) {
			dummyList.add("Shop" + i);
		}
		return dummyList;
		//DEBUG CODE END
	}
	
	/**
	 * login prompt, sets MerchantID TODO from server query
	 *
	 */
	private static void login() {
	
		//set up frame
		JFrame frame = new JFrame("GMM Marketing Solutions");
		frame.setSize(300, 150);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel UsernameLabel = new JLabel("Username:");
		JTextField UsernameField = new JTextField(15);
		JPanel UsernamePanel = new JPanel();
		UsernamePanel.add(UsernameLabel, BorderLayout.CENTER);
		UsernamePanel.add(UsernameField, BorderLayout.SOUTH);
		
		JLabel PasswordLabel = new JLabel("Password:");
		JPasswordField PasswordField = new JPasswordField(15);
		PasswordField.setEchoChar('*');
		JPanel PasswordPanel = new JPanel();
		PasswordPanel.add(PasswordLabel, BorderLayout.CENTER);
		PasswordPanel.add(PasswordField, BorderLayout.SOUTH);
		
		JButton LoginButton = new JButton("Login");
		LoginButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//TODO queries the Merchant Table for the Username and Password
				//	if it finds an entry, sets the MerchantID
				MerchantID = "";
			}
		});
		
		frame.add(UsernamePanel, BorderLayout.NORTH);
		frame.add(PasswordPanel, BorderLayout.CENTER);
		frame.add(LoginButton, BorderLayout.SOUTH);
		
		//center and show the window
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		while(MerchantID == null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException exception) {
				exception.printStackTrace();
			}
		}
		frame.dispose();
	}

}
