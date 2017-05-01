import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

/**
 * 
 * Main Interface Application for the GMM (Generic MMORPG Merchant) marketing
 * solutions database
 *
 * @author Alan Holman Created Apr 14, 2017.
 */
public class Main {

	public static final Color BG_COLOR = new Color(43, 43, 43);//new Color(100, 100, 200);
	public static final Color BG_COLOR2 = new Color(33, 33, 33);//new Color(90, 90, 180);
	public static final Color FIELD_COLOR = new Color(23, 23, 23);//new Color(150, 150, 250);
	public static final Color MENU_BUTTON_COLOR = new Color(13, 13, 13);//new Color(50, 50, 150);
	public static final Color TEXT_COLOR = new Color(220, 220, 220);//Color.BLACK;
	public static final JLabel EMPTY_CELL = new JLabel();
	public static final Font HEADER_FONT = new Font(null, Font.BOLD, 24);
	public static final Font FIELD_FONT = new Font(Font.MONOSPACED, Font.BOLD, 18);

	static String MerchantID;
	static Queue<String> updateQueue;
	static JPanel menuBar;
	static GMMPage curPage;
	static boolean relaunch;
	static JFrame mainframe;
	protected static GMMPage marketPage;
	static Connection conn;

	public static void main(String[] args) throws SQLException {
		relaunch = true;
		
		dbConnect connector = new dbConnect();
		conn = connector.connect();
		
		while (true) {
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
	 * login prompt
	 *
	 */
	static void login() {
		// set up frame
		JFrame frame = new JFrame("GMM Marketing Solutions");
		frame.setSize(600, 360);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// load logo image
		BufferedImage logoImg = null;
		try {
			logoImg = ImageIO.read(new File("logo.png"));
		} catch (IOException exception1) {
			exception1.printStackTrace();
		}

		// create panels
		JPanel UsernamePanel = new JPanel();
		JPanel PasswordPanel = new JPanel();
		JPanel CredentialPanel = new JPanel();
		JPanel LoginPanel = new JPanel();

		// create labels
		JLabel logoImgLabel = new JLabel(new ImageIcon(logoImg));
		JLabel UsernameLabel = new JLabel("Username:");
		JLabel PasswordLabel = new JLabel("Password:");
		
		//setForeground
		UsernameLabel.setForeground(TEXT_COLOR);
		PasswordLabel.setForeground(TEXT_COLOR);

		// create fields
		JTextField UsernameField = new JTextField(15);
		JPasswordField PasswordField = new JPasswordField(15);
		
		//limit fields
		((AbstractDocument)UsernameField.getDocument()).setDocumentFilter(new LimitDocumentFilter(20));

		// misc construct & settings
		PasswordField.addKeyListener(new LoginListener(UsernameField, PasswordField));
		JButton LoginButton = new MenuButton("Login", new LoginListener(UsernameField, PasswordField));
		GridLayout fieldLayout = new GridLayout(2, 3);
		PasswordField.setEchoChar('*');
		CredentialPanel.setLayout(fieldLayout);

		// set Fonts
		UsernameLabel.setFont(FIELD_FONT);
		PasswordLabel.setFont(FIELD_FONT);
		UsernameField.setFont(FIELD_FONT);
		PasswordField.setFont(FIELD_FONT);
		LoginButton.setFont(FIELD_FONT);

		// add to panels
		UsernamePanel.add(UsernameLabel);
		UsernamePanel.add(UsernameField);
		PasswordPanel.add(PasswordLabel);
		PasswordPanel.add(PasswordField);
		CredentialPanel.add(UsernamePanel);
		CredentialPanel.add(PasswordPanel);
		LoginPanel.add(LoginButton);

		// setting backgrounds
		UsernamePanel.setBackground(BG_COLOR);
		PasswordPanel.setBackground(BG_COLOR);
		LoginPanel.setBackground(BG_COLOR);

		// add to frame
		frame.add(logoImgLabel, BorderLayout.NORTH);
		frame.add(CredentialPanel, BorderLayout.CENTER);
		frame.add(LoginPanel, BorderLayout.SOUTH);

		// center and show the window
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		// wait for verification
		while (MerchantID == null) {
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

		// construct menuBar
		menuBar = new JPanel();
		menuBar.setLayout(new GridLayout(0, 5));
		menuBar.setBackground(Color.GRAY);
		menuBar.setPreferredSize(new Dimension(900, 30));
		mainframe.add(menuBar, BorderLayout.NORTH);

		// construct pages
		marketPage = new MarketPage();
		GMMPage addShopPage = new ShopAddPage();
		GMMPage addSupplierPage = new SupplierAddPage();
		GMMPage restockPage = new RestockPage();

		// construct menu buttons
		JButton marketPageButton = new MenuButton("Market View", new MenuListener(marketPage));
		JButton addShopPageButton = new MenuButton("Add New Shop", new MenuListener(addShopPage));
		JButton addSupplierPageButton = new MenuButton("Add New Supplier", new MenuListener(addSupplierPage));
		JButton restockPageButton = new MenuButton("Inform of Shop Restock", new MenuListener(restockPage));
		JButton logOutButton = new MenuButton("Log Out", new LogOutListener());

		// add menu buttons to menuBar
		menuBar.add(marketPageButton);
		menuBar.add(addShopPageButton);
		menuBar.add(addSupplierPageButton);
		menuBar.add(restockPageButton);
		menuBar.add(logOutButton);

		// show market page on startup
		marketPage.changeToPage();

		// center and show the window
		mainframe.setLocationRelativeTo(null);
		mainframe.setVisible(true);
	}

	/**
	 * TODO gets a list of shops owned by the Merchant using query
	 *
	 * @return List<String>
	 */
	public static List<String> getShopList() {
		// DEBUG CODE START
		List<String> dummyList = new ArrayList<>();
		for (int i = 1; i <= 7; i++) {
			dummyList.add("Shop" + i);
		}
		return dummyList;
		// DEBUG CODE END
	}

	private static class LogOutListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Main.marketPage.shutDown();
			Main.mainframe.dispose();
			Main.MerchantID = null;
			Main.updateQueue = null;
			Main.menuBar = null;
			Main.curPage = null;
			Main.relaunch = true;
		}
	}

	public static List<String> getServerList() {
		// DEBUG CODE START
		List<String> dummyList = new ArrayList<>();
		dummyList.add("Rose");
//		for (int i = 1; i <= 7; i++) {
//			dummyList.add("Server" + i);
//		}
		return dummyList;
		// DEBUG CODE END
	}

	public static List<String> getSupplierList() {
		// DEBUG CODE START
		List<String> dummyList = new ArrayList<>();
		for (int i = 1; i <= 17; i++) {
			dummyList.add("Supplier" + i);
		}
		return dummyList;
		// DEBUG CODE END
	}

	public static List<String> getItemList() {
		// DEBUG CODE START
		List<String> dummyList = new ArrayList<>();
		for (int i = 1; i <= 50; i++) {
			dummyList.add("Item" + i);
		}
		return dummyList;
		// DEBUG CODE END
	}
	

	/**
	 * Method to check if given string is numeric or contains other characters
	 * 
	 * @param str
	 * @return true if numeric, false otherwise
	 */
	public static boolean isNumeric(String str) {
		try {
			double tmp = Double.parseDouble(str);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
