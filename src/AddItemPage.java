import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Types;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

public class AddItemPage implements GMMPage {

	// Fields for GUI
	JPanel centerPanel;
	private JTextField item;
	private JTextArea description;
	private JTextField baseValue;

	public class SubmitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// Only try to add item if inputs validated correctly
			if (validateInputs()) {
				// Parsing numeric fields
				double base = Double.parseDouble(baseValue.getText());

				addItem(item.getText(), description.getText(), base);
			}
		}
	}

	public AddItemPage() {
		// create panels
		this.centerPanel = new JPanel();
		JPanel itemPanel = new JPanel();
		JPanel descriptionPanel = new JPanel();
		JPanel baseValuePanel = new JPanel();
		JPanel submitPanel = new JPanel();
		JPanel headerPanel = new JPanel();

		// create Labels
		JLabel itemLabel = new JLabel("   *Item Name:  ");
		JLabel descriptionLabel = new JLabel(" Description: ");
		JLabel baseValueLabel = new JLabel("   *Base Value: ");
		JLabel baseValueLabelAfter = new JLabel("                ");

		// create Fields
		this.item = new JTextField(25);
		this.description = new JTextArea(6, 50);
		this.baseValue = new JTextField(8);

		// set foregrounds
		itemLabel.setForeground(Main.TEXT_COLOR);
		descriptionLabel.setForeground(Main.TEXT_COLOR);
		baseValueLabel.setForeground(Main.TEXT_COLOR);
		baseValueLabelAfter.setForeground(Main.TEXT_COLOR);
		this.item.setForeground(Main.TEXT_COLOR);
		this.description.setForeground(Main.TEXT_COLOR);
		this.baseValue.setForeground(Main.TEXT_COLOR);

		// limit fields
		((AbstractDocument) this.item.getDocument()).setDocumentFilter(new LimitDocumentFilter(25));
		((AbstractDocument) this.description.getDocument()).setDocumentFilter(new LimitDocumentFilter(300));
		this.description.setLineWrap(true);
		((AbstractDocument) this.baseValue.getDocument()).setDocumentFilter(new LimitDocumentFilter(7));

		// create button
		JButton submitButton = new MenuButton("Submit", new SubmitListener());

		// set fonts
		itemLabel.setFont(Main.FIELD_FONT);
		descriptionLabel.setFont(Main.FIELD_FONT);
		baseValueLabel.setFont(Main.FIELD_FONT);
		baseValueLabelAfter.setFont(Main.FIELD_FONT);
		submitButton.setFont(Main.FIELD_FONT);
		this.item.setFont(Main.FIELD_FONT);
		this.description.setFont(Main.FIELD_FONT);
		this.baseValue.setFont(Main.FIELD_FONT);

		// add fields to panels
		itemPanel.add(itemLabel);
		itemPanel.add(this.item);
		descriptionPanel.add(descriptionLabel);
		descriptionPanel.add(this.description);
		baseValuePanel.add(baseValueLabel);
		baseValuePanel.add(this.baseValue);
		baseValuePanel.add(baseValueLabelAfter);
		submitPanel.add(submitButton);
		this.centerPanel.setLayout(new BoxLayout(this.centerPanel, BoxLayout.Y_AXIS));
		JLabel header = new JLabel("Inform of a New Item");
		header.setFont(Main.HEADER_FONT);
		header.setForeground(Main.TEXT_COLOR);
		headerPanel.add(header);
		this.centerPanel.add(headerPanel);
		this.centerPanel.add(itemPanel);
		this.centerPanel.add(baseValuePanel);
		this.centerPanel.add(descriptionPanel);
		this.centerPanel.add(submitPanel);
		this.centerPanel.add(new JLabel(" "));
		this.centerPanel.add(new JLabel(" "));
		this.centerPanel.add(new JLabel(" "));
		this.centerPanel.add(new JLabel(" "));
		this.centerPanel.add(new JLabel(" "));
		this.centerPanel.add(new JLabel(" "));
		this.centerPanel.add(new JLabel(" "));
		this.centerPanel.add(new JLabel(" "));
		this.centerPanel.add(new JLabel(" "));

		// set BG colors
		this.centerPanel.setBackground(Main.BG_COLOR);
		itemPanel.setBackground(Main.BG_COLOR);
		descriptionPanel.setBackground(Main.BG_COLOR);
		baseValuePanel.setBackground(Main.BG_COLOR);
		submitPanel.setBackground(Main.BG_COLOR);
		headerPanel.setBackground(Main.BG_COLOR);
		this.item.setBackground(Main.FIELD_COLOR);
		this.description.setBackground(Main.FIELD_COLOR);
		this.baseValue.setBackground(Main.FIELD_COLOR);
	}

	@Override
	public void changeToPage() {
		if (Main.curPage != this) {
			Main.curPage.unShow();
			Main.mainframe.add(this.centerPanel, BorderLayout.CENTER);
			Main.curPage = this;
			Main.mainframe.revalidate();
			Main.mainframe.repaint();
			System.out.println("AddItemPage Loaded");
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
		System.out.println("AddItemPage Unloaded");
	}

	public void addItem(String name, String desc, double val) {
		try {
			CallableStatement proc = Main.conn.prepareCall("{ ? = call dbo.addItem(?, ?, ?) }");
			
			// Registering parameters in CallableStatement to fill values
			proc.setString(2, name);
			proc.setDouble(3, val);
			proc.setString(4, desc);
			
			// Getting return code from stored procedure to indicate success/error
			proc.registerOutParameter(1, Types.INTEGER);
			proc.execute();
			
			int returnVal = proc.getInt(1);
			
			// Checking that item was successfully added
			if (returnVal == 1)
				JOptionPane.showMessageDialog(null, "Item already exists in database. Please input new parameters.");
			else if (returnVal == 0) {
				JOptionPane.showMessageDialog(null, name + " was successfully added!");
				
				// blank out fields upon success
				this.item.setText("");
				this.baseValue.setText("");
				this.description.setText("");
			}
			else
				JOptionPane.showMessageDialog(null, "Item addition failed. Error code is: " + returnVal);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean validateInputs() {
		// Temporary string to validate input
		String baseVal = baseValue.getText();
		String name = item.getText();

		// Check if any required fields are empty
		if (baseVal.trim().length() == 0 || name.trim().length() == 0) {
			JOptionPane.showMessageDialog(centerPanel.getComponent(0), "Please fill out all required field(s).");
			return false;
		}
		// Check that base value is numeric
		if (!Main.isNumeric(baseVal) && !baseVal.isEmpty()) {
			JOptionPane.showMessageDialog(centerPanel.getComponent(0),
					"Please enter a valid number for the item value.");
			return false;
		}
		// Check that base value is positive
		if (!baseVal.isEmpty() && Double.parseDouble(baseVal) <= 0) {
			JOptionPane.showMessageDialog(centerPanel.getComponent(0),
					"Please enter a positive number for item value.");
			return false;
		}

		return true;
	}
}
