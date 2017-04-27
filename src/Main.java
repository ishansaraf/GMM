import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * 
 * Main Interface Application for the GMM (Generic MMORPG Merchant) marketing solutions database
 *
 * @author Alan Holman
 *         Created Apr 14, 2017.
 */
public class Main {
	
	static String MerchantID;
	static Queue<String> updateQueue;
	static JPanel northPanel;
	static GMMPage curPage;
	static boolean relaunch;
	static JFrame mainframe;
	protected static MarketPage marketPage;
	
	public static void main(String[] args) {
		relaunch = true;
		while(true) {
			if (relaunch) {
				mainframe = new JFrame();
				mainframe.setSize(900, 600);
				mainframe.setTitle("GMM Marketing Solutions");
				mainframe.setResizable(false);
				mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				login();
				relaunch = false;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException exception) {
				exception.printStackTrace();
			}
		}
	}
	
	/**
	 * login prompt, sets MerchantID TODO from server query
	 *
	 */
	static void login() {
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
		
		runProgram();
	}
	
	static void runProgram() {
		updateQueue = new LinkedList<>();
		
		//construct north panel
		northPanel = new JPanel();
		northPanel.setBackground(Color.GRAY);
		northPanel.setPreferredSize(new Dimension(900, 30));
		mainframe.add(northPanel, BorderLayout.NORTH);
		
		//construct pages
		marketPage = new MarketPage(MerchantID);
		
		//add northPanel menu buttons
		JButton marketPageButton = new JButton("Market View");
		marketPageButton.addActionListener(new MenuListener(marketPage));
		
		JButton logOutButton = new JButton("Log Out");
		logOutButton.addActionListener(new LogOutListener());
		
		//add things to northPanel
		northPanel.add(marketPageButton);
		northPanel.add(logOutButton);
		
		//show market page on startup
		marketPage.changeToPage();
		curPage = marketPage;
		
		//center and show the window
		mainframe.setLocationRelativeTo(null);
		mainframe.setVisible(true);
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
	
	
	public static class LogOutListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Main.marketPage.shutDown();
			Main.mainframe.dispose();
			Main.MerchantID = null;
			Main.updateQueue = null;
			Main.northPanel = null;
			Main.curPage = null;
			Main.relaunch = true;
		}
	}
}
