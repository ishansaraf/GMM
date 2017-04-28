import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

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
		//create panels
		this.centerPanel = new JPanel();
		JPanel namePanel = new JPanel();
		JPanel serverPanel = new JPanel();
		JPanel locationPanel = new JPanel();
		JPanel discountPanel = new JPanel();
		JPanel submitPanel = new JPanel();
		
		//create Labels
		JLabel nameLabel = new JLabel("*Name: ");
		JLabel serverLabel = new JLabel("*Server: ");
		JLabel locationLabelX = new JLabel("*Location:       x=");
		JLabel locationLabelY = new JLabel(" y=");
		JLabel discountLabel = new JLabel(" Product Discount: ");
		JLabel afterDiscountLabel = new JLabel("%          ");
		
		//create TextFields
		this.name = new JTextField(26);
		this.server = new JComboBox<>();
		this.locationX = new JTextField(5);
		this.locationY = new JTextField(5);
		this.discount = new JTextField(3);
		
		//create button
		JButton submitButton = new MenuButton("Submit", new SubmitListener());
		
		//populate ComboBoxes
		List<String> tempArrList = Main.getServerList();
		String[] modelArray = new String[tempArrList.size()+1];
		modelArray[0] = "                      ";
		for (int i = 0; i < modelArray.length-1; i++) {
			modelArray[i+1] = tempArrList.get(i);
		}
		ComboBoxModel<String> serverModel = new DefaultComboBoxModel<>(modelArray);
		this.server.setModel(serverModel);
		
		//set fonts
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
		
		//add fields to panels
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
		this.centerPanel.add(header);
		this.centerPanel.add(namePanel);
		this.centerPanel.add(serverPanel);
		this.centerPanel.add(locationPanel);
		this.centerPanel.add(discountPanel);
		this.centerPanel.add(submitPanel);
		
		//set BG colors
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
		if (this.centerPanel != null) Main.mainframe.remove(this.centerPanel);
		System.out.println("SupplierAddPage Unloaded");
	}

}
