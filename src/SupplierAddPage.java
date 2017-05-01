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

public class SupplierAddPage implements GMMPage {

	public class SubmitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub.

		}

	}

	JPanel centerPanel;
	JTextField name;
	JComboBox<String> server;
	JTextField locationX;
	JTextField locationY;
	JTextField discount;

	public SupplierAddPage() {
		// create panels
		this.centerPanel = new JPanel();
		JPanel namePanel = new JPanel();
		JPanel serverPanel = new JPanel();
		JPanel locationPanel = new JPanel();
		JPanel discountPanel = new JPanel();
		JPanel submitPanel = new JPanel();

		// create Labels
		JLabel nameLabel = new JLabel("*Name: ");
		JLabel serverLabel = new JLabel("*Server: ");
		JLabel locationLabelX = new JLabel("*Location: x=");
		JLabel locationLabelY = new JLabel(" y=");
		JLabel discountLabel = new JLabel(" Product Discount: ");
		JLabel afterDiscountLabel = new JLabel("%          ");

		// create TextFields
		this.name = new JTextField(26);
		this.server = new JComboBox<>();
		this.locationX = new JTextField(8);
		this.locationY = new JTextField(8);
		this.discount = new JTextField(2);

		// limit fields
		((AbstractDocument) this.name.getDocument()).setDocumentFilter(new LimitDocumentFilter(25));
		((AbstractDocument) this.locationX.getDocument()).setDocumentFilter(new LimitDocumentFilter(7));
		((AbstractDocument) this.locationY.getDocument()).setDocumentFilter(new LimitDocumentFilter(7));
		((AbstractDocument) this.discount.getDocument()).setDocumentFilter(new LimitDocumentFilter(2));

		// create button
		JButton submitButton = new MenuButton("Submit", new SubmitListener());

		// populate ComboBoxes
		List<String> tempArrList = Main.getServerList();
		String[] modelArray = new String[tempArrList.size() + 1];
		modelArray[0] = "                      ";
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
		discountLabel.setFont(Main.FIELD_FONT);
		afterDiscountLabel.setFont(Main.FIELD_FONT);
		submitButton.setFont(Main.FIELD_FONT);
		this.name.setFont(Main.FIELD_FONT);
		this.server.setFont(Main.FIELD_FONT);
		this.locationX.setFont(Main.FIELD_FONT);
		this.locationY.setFont(Main.FIELD_FONT);
		this.discount.setFont(Main.FIELD_FONT);

		// setForeground
		nameLabel.setForeground(Main.TEXT_COLOR);
		serverLabel.setForeground(Main.TEXT_COLOR);
		locationLabelX.setForeground(Main.TEXT_COLOR);
		locationLabelY.setForeground(Main.TEXT_COLOR);
		discountLabel.setForeground(Main.TEXT_COLOR);
		afterDiscountLabel.setForeground(Main.TEXT_COLOR);
		submitButton.setForeground(Main.TEXT_COLOR);
		this.name.setForeground(Main.TEXT_COLOR);
		this.server.setForeground(Main.TEXT_COLOR);
		this.locationX.setForeground(Main.TEXT_COLOR);
		this.locationY.setForeground(Main.TEXT_COLOR);
		this.discount.setForeground(Main.TEXT_COLOR);

		// add fields to panels
		namePanel.add(nameLabel);
		namePanel.add(this.name);
		serverPanel.add(serverLabel);
		serverPanel.add(this.server);
		locationPanel.add(locationLabelX);
		locationPanel.add(this.locationX);
		locationPanel.add(locationLabelY);
		locationPanel.add(this.locationY);
		discountPanel.add(discountLabel);
		discountPanel.add(this.discount);
		discountPanel.add(afterDiscountLabel);
		submitPanel.add(submitButton);
		this.centerPanel.setLayout(new GridLayout(12, 3));
		JLabel header = new JLabel("Add a Supplier", SwingConstants.CENTER);
		header.setFont(Main.HEADER_FONT);
		header.setForeground(Main.TEXT_COLOR);
		this.centerPanel.add(header);
		this.centerPanel.add(namePanel);
		this.centerPanel.add(serverPanel);
		this.centerPanel.add(locationPanel);
		this.centerPanel.add(discountPanel);
		this.centerPanel.add(submitPanel);

		// set BG colors
		this.centerPanel.setBackground(Main.BG_COLOR);
		namePanel.setBackground(Main.BG_COLOR);
		serverPanel.setBackground(Main.BG_COLOR);
		locationPanel.setBackground(Main.BG_COLOR);
		discountPanel.setBackground(Main.BG_COLOR);
		submitPanel.setBackground(Main.BG_COLOR);
		this.name.setBackground(Main.FIELD_COLOR);
		this.server.setBackground(Main.FIELD_COLOR);
		this.locationX.setBackground(Main.FIELD_COLOR);
		this.locationY.setBackground(Main.FIELD_COLOR);
		this.discount.setBackground(Main.FIELD_COLOR);
	}

	@Override
	public void changeToPage() {
		if (Main.curPage != this) {
			Main.curPage.unShow();
			Main.mainframe.add(this.centerPanel, BorderLayout.CENTER);
			Main.curPage = this;
			Main.mainframe.revalidate();
			Main.mainframe.repaint();
			System.out.println("SupplierAddPage Loaded");
		}
	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub.

	}

	@Override
	public void unShow() {
		if (this.centerPanel != null)
			Main.mainframe.remove(this.centerPanel);
		System.out.println("SupplierAddPage Unloaded");
	}

	/**
	 * Uses the database connection to add a supplier to the database, using
	 * given input fields
	 * 
	 * @param name:
	 *            name of the supplier
	 * @param server:
	 *            name of the server supplier is located on
	 * @param locX:
	 *            x coordinates of the supplier
	 * @param locY:
	 *            y coordinates of the supplier
	 * @param discount:
	 *            discount provided by the supplier
	 */
	public void addSupplier(String name, String server, double locX, double locY, double discount) {
		try {
			CallableStatement proc = Main.conn.prepareCall(" ? = call dbo.addSupplier(?, ?, ?, ?, ?, ?)");

			// Registering parameters in CallableStatement
			proc.setString(2, name);
			proc.setString(3, server);
			proc.setDouble(4, locX);
			proc.setDouble(5, locY);
			proc.setDouble(6, discount);
			proc.setString(7, Main.MerchantID);

			// Getting return code from stored procedure to indicate
			// success/error
			proc.registerOutParameter(1, Types.INTEGER);
			proc.execute();

			int returnVal = proc.getInt(1);

			// Checking that addition of supplier was successful
			if (returnVal == 1)
				JOptionPane.showMessageDialog(null,
						"Supplier already exists in database. Please input new parameters.");
			else if (returnVal == 0) {
				JOptionPane.showMessageDialog(null, "Supplier " + name + " was successfully added!");
				// Blanking out fields
				this.name.setText("");
				this.server.setSelectedIndex(0);
				this.locationX.setText("");
				this.locationY.setText("");
				this.discount.setText("");
			} else
				JOptionPane.showMessageDialog(null, "Supplier addition failed. Error code is: " + returnVal);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if text fields in GUI contain valid inputs, otherwise displays
	 * appropriate error messages
	 * 
	 * @return true if inputs are valid, false otherwise
	 */
	public boolean validateInputs() {
		// TODO
		return false;
	}
}
