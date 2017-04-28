import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ShopAddPage implements GMMPage {

	public class SubmitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub.

		}

	}

	JPanel centerPanel;
	JTextField name;
	JTextField server;
	JTextField locationX;
	JTextField locationY;
	JTextField funds;
	
	public ShopAddPage() {
		//create panels
		this.centerPanel = new JPanel();
		JPanel namePanel = new JPanel();
		JPanel serverPanel = new JPanel();
		JPanel locationPanel = new JPanel();
		JPanel fundsPanel = new JPanel();
		JPanel submitPanel = new JPanel();
		
		//create Labels
		JLabel nameLabel = new JLabel("*Name: ");
		JLabel serverLabel = new JLabel("*Server: ");
		JLabel locationLabelX = new JLabel("*Location:       x=");
		JLabel locationLabelY = new JLabel(" y=");
		JLabel fundsLabel = new JLabel(" Current Funds: ");
		
		//create TextFields
		this.name = new JTextField(26);
		this.server = new JTextField(24);
		this.locationX = new JTextField(5);
		this.locationY = new JTextField(5);
		this.funds = new JTextField(17);
		
		//create button
		JButton submitButton = new MenuButton("Submit", new SubmitListener());
		
		//set fonts
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
		
		//add fields to panels
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
		this.centerPanel.add(header);
		this.centerPanel.add(namePanel);
		this.centerPanel.add(serverPanel);
		this.centerPanel.add(locationPanel);
		this.centerPanel.add(fundsPanel);
		this.centerPanel.add(submitPanel);
		
		//set BG colors
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
		// TODO Auto-generated method stub.

	}

	@Override
	public void unShow() {
		if (this.centerPanel != null) Main.mainframe.remove(this.centerPanel);
		System.out.println("ShopAddPage Unloaded");
	}
}
