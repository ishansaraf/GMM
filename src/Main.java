import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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

	public static final Color BG_COLOR = new Color(43, 43, 43);// new Color(100,
																// 100, 200);
	public static final Color BG_COLOR2 = new Color(33, 33, 33);// new Color(90,
																// 90, 180);
	public static final Color FIELD_COLOR = new Color(23, 23, 23);// new
																	// Color(150,
																	// 150,
																	// 250);
	public static final Color MENU_BUTTON_COLOR = new Color(13, 13, 13);// new
																		// Color(50,
																		// 50,
																		// 150);
	public static final Color TEXT_COLOR = new Color(220, 220, 220);// Color.BLACK;
	public static final JLabel EMPTY_CELL = new JLabel();
	public static final Font HEADER_FONT = new Font(null, Font.BOLD, 24);
	public static final Font FIELD_FONT = new Font(Font.MONOSPACED, Font.BOLD, 18);
	public static final Font LIST_FONT = new Font(Font.MONOSPACED, Font.BOLD, 12);
	public static final Font TABLE_FONT = new Font(Font.MONOSPACED, Font.BOLD, 16);
	public static final int WINDOW_SIZE_X = 1000;

	static String MerchantID;
	static Queue<String> updateQueue;
	static List<String> serverList;
	static List<String> supplierList;
	static List<String> itemList;

	static JPanel menuBar;
	static GMMPage curPage;
	static boolean relaunch;
	static boolean register;
	static JFrame mainframe;
	protected static GMMPage marketPage;
	static Connection conn;

	public static void main(String[] args) throws SQLException {
		relaunch = true;
		register = false; //default to false: set to true to swap to register page

		dbConnect connector = new dbConnect();
		conn = connector.connect();

		while (true) {
			if (relaunch) {
				mainframe = new JFrame();
				mainframe.setSize(WINDOW_SIZE_X, 600);
				mainframe.setTitle("GMM Marketing Solutions");
				mainframe.setResizable(false);
				mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				serverList = new ArrayList<>();
				supplierList = new ArrayList<>();
				itemList = new ArrayList<>();
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

		//setUp registration page
		JFrame regFrame = new JFrame("Register an GMM account");
		regFrame.setSize(600, 360);
		regFrame.setResizable(false);
		regFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		RegistrationPage regPage = new RegistrationPage(regFrame);
		
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
		JPanel ButtonPanel = new JPanel();

		// create labels
		JLabel logoImgLabel = new JLabel(new ImageIcon(logoImg));
		JLabel UsernameLabel = new JLabel("Username:");
		JLabel PasswordLabel = new JLabel("Password:");

		// setForeground
		UsernameLabel.setForeground(TEXT_COLOR);
		PasswordLabel.setForeground(TEXT_COLOR);

		// create fields
		JTextField UsernameField = new JTextField(15);
		JPasswordField PasswordField = new JPasswordField(15);

		// limit fields
		((AbstractDocument) UsernameField.getDocument()).setDocumentFilter(new LimitDocumentFilter(20));

		// misc construct & settings
		PasswordField.addKeyListener(new LoginListener(UsernameField, PasswordField));
		JButton LoginButton = new MenuButton("Login", new LoginListener(UsernameField, PasswordField));
		JButton RegisterButton = new MenuButton("Register", new RegisterListener());
		GridLayout fieldLayout = new GridLayout(2, 3);
		PasswordField.setEchoChar('*');
		CredentialPanel.setLayout(fieldLayout);
		ButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		// set Fonts
		UsernameLabel.setFont(FIELD_FONT);
		PasswordLabel.setFont(FIELD_FONT);
		UsernameField.setFont(FIELD_FONT);
		PasswordField.setFont(FIELD_FONT);
		LoginButton.setFont(FIELD_FONT);
		RegisterButton.setFont(FIELD_FONT);

		// add to panels
		UsernamePanel.add(UsernameLabel);
		UsernamePanel.add(UsernameField);
		PasswordPanel.add(PasswordLabel);
		PasswordPanel.add(PasswordField);
		CredentialPanel.add(UsernamePanel);
		CredentialPanel.add(PasswordPanel);
		ButtonPanel.add(LoginButton);
		ButtonPanel.add(RegisterButton);

		// setting backgrounds
		UsernamePanel.setBackground(BG_COLOR);
		PasswordPanel.setBackground(BG_COLOR);
		ButtonPanel.setBackground(BG_COLOR);
		
		// add to frame
		frame.add(logoImgLabel, BorderLayout.NORTH);
		frame.add(CredentialPanel, BorderLayout.CENTER);
		frame.add(ButtonPanel, BorderLayout.SOUTH);

		// center and show the window
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		// wait for verification
		while (MerchantID == null) {
			try {
				if (register) {
					frame.dispose();
					regPage.changeToPage();
					return;
				}
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
		menuBar.setLayout(new GridLayout(0, 6));
		menuBar.setBackground(Color.GRAY);
		menuBar.setPreferredSize(new Dimension(WINDOW_SIZE_X, 30));
		mainframe.add(menuBar, BorderLayout.NORTH);

		// construct pages
		marketPage = new MarketPage();
		GMMPage addShopPage = new ShopAddPage();
		GMMPage addSupplierPage = new SupplierAddPage();
		GMMPage restockPage = new RestockPage();
		GMMPage addItemPage = new AddItemPage();

		// construct menu buttons
		JButton marketPageButton = new MenuButton("Market View", new MenuListener(marketPage));
		JButton addShopPageButton = new MenuButton("Add New Shop", new MenuListener(addShopPage));
		JButton addSupplierPageButton = new MenuButton("Add New Supplier", new MenuListener(addSupplierPage));
		JButton restockPageButton = new MenuButton("Inform of Restock", new MenuListener(restockPage));
		JButton addItemPageButton = new MenuButton("Inform of New Item", new MenuListener(addItemPage));
		JButton logOutButton = new MenuButton("Log Out", new LogOutListener());

		// add menu buttons to menuBar
		menuBar.add(marketPageButton);
		menuBar.add(addShopPageButton);
		menuBar.add(addSupplierPageButton);
		menuBar.add(restockPageButton);
		menuBar.add(addItemPageButton);
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
	public static ArrayList<String> getShopList() {
		ArrayList<String> shopList = new ArrayList<>();
		try {
			CallableStatement proc = Main.conn.prepareCall("{call dbo.getShopList()}");
			ResultSet rs = proc.executeQuery();
			while (rs.next()) {
				shopList.add(rs.getString(1));
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		}

		return shopList;
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
		if (serverList.isEmpty()) {
			try {
				CallableStatement proc = Main.conn.prepareCall("{call dbo.getServerList()}");
				ResultSet rs = proc.executeQuery();
				while (rs.next()) {
					serverList.add(rs.getString(1));
				}
			} catch (SQLException exception) {
				exception.printStackTrace();
			}
		}
		return serverList;
	}

	public static List<String> getSupplierList() {
		if (supplierList.isEmpty()) {
			try {
				CallableStatement proc = Main.conn.prepareCall("{call dbo.getSupplierList()}");
				ResultSet rs = proc.executeQuery();
				while (rs.next()) {
					supplierList.add(rs.getString(1));
				}
			} catch (SQLException exception) {
				exception.printStackTrace();
			}
		}
		return supplierList;
	}

	public static ArrayList<String> getSupplierList(String shop) {
		ArrayList<String> list = new ArrayList<>();
		try {
			CallableStatement cs = Main.conn.prepareCall("{? = call getSuppliersByShop(?, ?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, shop);
			cs.setString(3, Main.MerchantID);
			ResultSet rs = cs.executeQuery();
			while (rs.next()) {
				list.add(rs.getString("Name") + " [" + rs.getDouble("Discount") + ", "
							+ Math.floor(Math.sqrt(rs.getDouble("DistanceSq")) * 100) / 100 + "]");
			}
			int status = cs.getInt(1);
			if (status == 1) {
				JOptionPane.showMessageDialog(null, "Could not retrieve supplier list for shop " + shop + ".");
				return new ArrayList<>();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<String> getItemList() {
		if (itemList.isEmpty()) {
			try {
				CallableStatement proc = Main.conn.prepareCall("{call dbo.getItemList()}");
				ResultSet rs = proc.executeQuery();
				while (rs.next()) {
					itemList.add(rs.getString(1));
				}
			} catch (SQLException exception) {
				exception.printStackTrace();
			}
		}
		return itemList;
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
