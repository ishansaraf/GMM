import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Types;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;

public class ShopAddPage implements GMMPage {
	// Fields for GUI
	JPanel centerPanel;
	JTextField name;
	JComboBox<String> server;
	JTextField locationX;
	JTextField locationY;
	JTextField funds;

	public class SubmitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// Only try to add shop if inputs validate correctly
			if (validateInputs()) {
				// Parsing integer fields
				double lX = Double.parseDouble(locationX.getText());
				double lY = Double.parseDouble(locationY.getText());
				double funding = funds.getText().equals("") ? 0.00 : Double.parseDouble(funds.getText());
				
				addShop(name.getText(), (String) server.getSelectedItem(), lX, lY, funding);
			}
		}
	}

	public ShopAddPage() {
		// create panels
		this.centerPanel = new JPanel();
		JPanel namePanel = new JPanel();
		JPanel serverPanel = new JPanel();
		JPanel locationPanel = new JPanel();
		JPanel fundsPanel = new JPanel();
		JPanel submitPanel = new JPanel();

		// create Labels
		JLabel nameLabel = new JLabel(" *Name:   ");
		JLabel serverLabel = new JLabel("   *Server: ");
		JLabel locationLabelX = new JLabel(" *Location:  x=");
		JLabel locationLabelY = new JLabel(" y=");
		JLabel fundsLabel = new JLabel("  Current Funds:     ");

		// create Fields
		this.name = new JTextField(25);
		this.server = new JComboBox<>();
		this.locationX = new JTextField(8);
		this.locationY = new JTextField(8);
		this.funds = new JTextField(14);

		// set foregrounds
		nameLabel.setForeground(Main.TEXT_COLOR);
		serverLabel.setForeground(Main.TEXT_COLOR);
		locationLabelX.setForeground(Main.TEXT_COLOR);
		locationLabelY.setForeground(Main.TEXT_COLOR);
		fundsLabel.setForeground(Main.TEXT_COLOR);
		this.name.setForeground(Main.TEXT_COLOR);
		this.server.setForeground(Main.TEXT_COLOR);
		this.locationX.setForeground(Main.TEXT_COLOR);
		this.locationY.setForeground(Main.TEXT_COLOR);
		this.funds.setForeground(Main.TEXT_COLOR);

		// limit fields
		((AbstractDocument) this.name.getDocument()).setDocumentFilter(new LimitDocumentFilter(25));
		((AbstractDocument) this.locationX.getDocument()).setDocumentFilter(new LimitDocumentFilter(7));
		((AbstractDocument) this.locationY.getDocument()).setDocumentFilter(new LimitDocumentFilter(7));
		((AbstractDocument) this.funds.getDocument()).setDocumentFilter(new LimitDocumentFilter(14));

		// create button
		JButton submitButton = new MenuButton("Submit", new SubmitListener());

		// populate ComboBoxes
		List<String> tempArrList = Main.getServerList();
		String[] modelArray = new String[tempArrList.size() + 1];
		modelArray[0] = "                         ";
		for (int i = 0; i < modelArray.length - 1; i++) {
			modelArray[i + 1] = tempArrList.get(i);
		}
		ComboBoxModel<String> serverModel = new DefaultComboBoxModel<>(modelArray);
		this.server.setModel(serverModel);
		this.server.setRenderer(new CSCListCellRenderer(Main.BG_COLOR2));

		// set fonts
		nameLabel.setFont(Main.FIELD_FONT);
		serverLabel.setFont(Main.FIELD_FONT);
		locationLabelX.setFont(Main.FIELD_FONT);
		locationLabelY.setFont(Main.FIELD_FONT);
		fundsLabel.setFont(Main.FIELD_FONT);
		submitButton.setFont(Main.FIELD_FONT);
		this.name.setFont(Main.FIELD_FONT);
		this.server.setFont(Main.FIELD_FONT);
		this.locationX.setFont(Main.FIELD_FONT);
		this.locationY.setFont(Main.FIELD_FONT);
		this.funds.setFont(Main.FIELD_FONT);

		// add fields to panels
		namePanel.add(nameLabel);
		namePanel.add(this.name);
		serverPanel.add(serverLabel);
		serverPanel.add(this.server);
		locationPanel.add(locationLabelX);
		locationPanel.add(this.locationX);
		locationPanel.add(locationLabelY);
		locationPanel.add(this.locationY);
		fundsPanel.add(fundsLabel);
		fundsPanel.add(this.funds);
		submitPanel.add(submitButton);
		this.centerPanel.setLayout(new GridLayout(12, 3));
		JLabel header = new JLabel("Add a Storefront", SwingConstants.CENTER);
		header.setFont(Main.HEADER_FONT);
		header.setForeground(Main.TEXT_COLOR);
		this.centerPanel.add(header);
		this.centerPanel.add(namePanel);
		this.centerPanel.add(serverPanel);
		this.centerPanel.add(locationPanel);
		this.centerPanel.add(fundsPanel);
		this.centerPanel.add(submitPanel);

		// set BG colors
		this.centerPanel.setBackground(Main.BG_COLOR);
		namePanel.setBackground(Main.BG_COLOR);
		serverPanel.setBackground(Main.BG_COLOR);
		locationPanel.setBackground(Main.BG_COLOR);
		fundsPanel.setBackground(Main.BG_COLOR);
		submitPanel.setBackground(Main.BG_COLOR);
		this.name.setBackground(Main.FIELD_COLOR);
		this.server.setBackground(Main.FIELD_COLOR);
		this.locationX.setBackground(Main.FIELD_COLOR);
		this.locationY.setBackground(Main.FIELD_COLOR);
		this.funds.setBackground(Main.FIELD_COLOR);
	}

	@Override
	public void changeToPage() {
		if (Main.curPage != this) {
			Main.curPage.unShow();
			Main.mainframe.add(this.centerPanel, BorderLayout.CENTER);

			Main.curPage = this;
			Main.mainframe.revalidate();
			Main.mainframe.repaint();
			System.out.println("ShopAddPage Loaded");
		}
	}

	@Override
	public void shutDown() {
		// nothing special

	}

	@Override
	public void unShow() {
		if (this.centerPanel != null)
			Main.mainframe.remove(this.centerPanel);
		System.out.println("ShopAddPage Unloaded");
	}

	/**
	 * Uses the database connection to add a storefront in the database, using
	 * the given input fields
	 * 
	 * @param name:
	 *            name of the storefront
	 * @param server:
	 *            name of the server where the storefront is
	 * @param locX:
	 *            x coordinates of the storeront
	 * @param locY:
	 *            y coordinates of the server
	 * @param funds:
	 *            initial amount of funds the storefront has
	 */
	public void addShop(String name, String server, double locX, double locY, double funds) {
		try {
			CallableStatement proc = Main.conn.prepareCall("{ ? = call dbo.addStorefront(?, ?, ?, ?, ?, ?) }");
			System.out.println(server);
			// Registering parameters in the CallableStatement to fill values
			proc.setString(2, name);
			proc.setString(3, server);
			proc.setDouble(4, locX);
			proc.setDouble(5, locY);
			proc.setDouble(6, funds);
			proc.setString(7, Main.MerchantID);

			// Getting return code from stored procedure to indicate
			// success/error
			proc.registerOutParameter(1, Types.INTEGER);
			proc.execute();

			int returnVal = proc.getInt(1);

			// Checking that addition of store front returned successfully
			if (returnVal == 1)
				JOptionPane.showMessageDialog(null,
						"Storefront already exists in the database. Please input new parameters and try again.");
			else if (returnVal == 0) {
				JOptionPane.showMessageDialog(null, "Store " + name + " was successfully added!");
				
				//update loaded list
				//Main.shopList.add(name);
				// blank out fields after success
				this.name.setText("");
				this.server.setSelectedIndex(0);
				this.locationX.setText("");
				this.locationY.setText("");
				this.funds.setText("");
			} else
				JOptionPane.showMessageDialog(null, "Storefront addition failed. Error code is: " + returnVal);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if the text fields in the GUI contain valid inputs, gives
	 * appropriate error messages otherwise
	 * 
	 * @return true if inputs are valid, false otherwise
	 */
	public boolean validateInputs() {
		// Temporary Strings to hold text box data
		String shopname = name.getText();
		String servername = (String) server.getSelectedItem();
		String locX = locationX.getText();
		String locY = locationY.getText();
		String f = funds.getText();

		// Check to see if any of the required fields are empty, throw an
		// alert and return if so
		if (shopname.trim().length() == 0 || servername.trim().length() == 0 || locX.trim().length() == 0 || locY.trim().length() == 0) {
			JOptionPane.showMessageDialog(centerPanel.getComponent(0), "Please fill out all required field(s).");
			return false;
		}
		// Check that location is numeric
		if (!Main.isNumeric(locX) || !Main.isNumeric(locY)) {
			JOptionPane.showMessageDialog(centerPanel.getComponent(0),
					"Please enter numeric value(s) for the location.");
			return false;
		}
		// Check that funding is numeric
		if (!Main.isNumeric(f) && !f.isEmpty()) {
			JOptionPane.showMessageDialog(centerPanel.getComponent(0), "Please enter a valid number for funds.");
			return false;
		}
		// Check that funding is non-negative
		if (!f.isEmpty() && Double.parseDouble(f) < 0) {
			JOptionPane.showMessageDialog(centerPanel.getComponent(0), "Please enter a non-negative number for funds.");
			return false;
		}

		return true;
	}
}
