import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Types;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;

import org.jasypt.util.password.StrongPasswordEncryptor;

public class RegistrationPage implements GMMPage {

	// Fields for GUI
	GMMPage curPage;
	JFrame frame;
	JPanel centerPanel;
	JTextField username;
	JPasswordField password;
	JTextField email;

	public class SubmitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub

		}

	}

	public RegistrationPage(JFrame frame) {
		this.frame = frame;
		
		// Create panels
		centerPanel = new JPanel();
		JPanel userPanel = new JPanel();
		JPanel passPanel = new JPanel();
		JPanel emailPanel = new JPanel();
		JPanel registerPanel = new JPanel();

		// Create labels
		JLabel userLabel = new JLabel("*Username:");
		JLabel passLabel = new JLabel("*Password:");
		JLabel emailLabel = new JLabel("*Email ID:");

		// Create fields
		username = new JTextField(30);
		password = new JPasswordField(15);
		email = new JTextField(50);

		// Create button
		JButton registerButton = new MenuButton("Register", new SubmitListener());

		// Create header
		JLabel header = new JLabel("Add User Details", SwingConstants.CENTER);

		// Set foregrounds
		userLabel.setForeground(Main.TEXT_COLOR);
		passLabel.setForeground(Main.TEXT_COLOR);
		emailLabel.setForeground(Main.TEXT_COLOR);
		username.setForeground(Main.TEXT_COLOR);
		password.setForeground(Main.TEXT_COLOR);
		email.setForeground(Main.TEXT_COLOR);
		header.setForeground(Main.TEXT_COLOR);

		// Limiting fields
		((AbstractDocument) username.getDocument()).setDocumentFilter(new LimitDocumentFilter(30));
		((AbstractDocument) password.getDocument()).setDocumentFilter(new LimitDocumentFilter(15));
		((AbstractDocument) email.getDocument()).setDocumentFilter(new LimitDocumentFilter(50));

		// Set fonts
		userLabel.setFont(Main.FIELD_FONT);
		passLabel.setFont(Main.FIELD_FONT);
		emailLabel.setFont(Main.FIELD_FONT);
		registerButton.setFont(Main.FIELD_FONT);
		username.setFont(Main.FIELD_FONT);
		password.setFont(Main.FIELD_FONT);
		email.setFont(Main.FIELD_FONT);
		header.setFont(Main.FIELD_FONT);

		// Add fields to panels
		userPanel.add(userLabel);
		passPanel.add(passLabel);
		emailPanel.add(emailLabel);
		registerPanel.add(registerButton);
		centerPanel.setLayout(new GridLayout(2, 2));
		centerPanel.add(header);
		centerPanel.add(userPanel);
		centerPanel.add(passPanel);
		centerPanel.add(emailPanel);
		centerPanel.add(registerPanel);

		// Set background colors
		centerPanel.setBackground(Main.BG_COLOR);
		userPanel.setBackground(Main.BG_COLOR);
		passPanel.setBackground(Main.BG_COLOR);
		emailPanel.setBackground(Main.BG_COLOR);
		registerPanel.setBackground(Main.BG_COLOR);
		username.setBackground(Main.BG_COLOR);
		password.setBackground(Main.BG_COLOR);
		email.setBackground(Main.BG_COLOR);
	}

	@Override
	public void changeToPage() {
		if (this.curPage != this) {
			this.frame.add(centerPanel, BorderLayout.CENTER);
			this.curPage = this;
			this.frame.revalidate();
			this.frame.repaint();
			this.frame.setLocationRelativeTo(null);
			this.frame.setVisible(true);
			System.out.println("RegistrationPage Loaded");
		}
	}

	@Override
	public void shutDown() {
		//nothing
	}

	@Override
	public void unShow() {
		if (centerPanel != null)
			this.frame.remove(centerPanel);
		this.curPage = null;
		System.out.println("RegistrationPage unloaded");
	}

	public String encryptPassword() {
		String out = "";

		// Encrypting inputed password
		StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
		out = encryptor.encryptPassword(password.getText());

		// Blanking out password field
		password.setText("");

		return out;
	}

	public void addMerchant(String user, String emailID, String pass) {
		try {
			CallableStatement proc = Main.conn.prepareCall("{ ? = call dbo.addUser(?, ?, ?) }");

			// Registering parameters for stored procedure
			proc.setString(2, user);
			proc.setString(3, emailID);
			proc.setString(4, pass);

			// Registering return value, executing procedure
			proc.registerOutParameter(1, Types.INTEGER);
			proc.execute();

			int returnVal = proc.getInt(1);

			if (returnVal == 1) {
				JOptionPane.showMessageDialog(null,
						"The user already exists in the database. Please input new parameters and try again.");
			} else if (returnVal == 0) {
				JOptionPane.showMessageDialog(null, "User" + user + " was successfully added!");

				// Blanking out fields
				username.setText("");
				password.setText("");
				email.setText("");

				// TODO: Decide whether successful insertion should just return
				// to the login page
			} else {
				JOptionPane.showMessageDialog(null,
						"The insertion of the user failed. The error code is: " + returnVal);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean validateInputs() {
		// Temporary fields
		String user = username.getText();
		String pass = password.getText();
		String emailID = email.getText();

		// Checking that user name is not empty
		if (user.trim().length() == 0) {
			JOptionPane.showMessageDialog(centerPanel.getComponent(0), "The username cannot be empty.");
			return false;
		}
		// Checking password is not empty
		if (pass.trim().length() == 0) {
			JOptionPane.showMessageDialog(centerPanel.getComponent(0), "The password cannot be empty.");
			return false;
		}
		// Checking email is not empty
		if (emailID.trim().length() == 0) {
			JOptionPane.showMessageDialog(centerPanel.getComponent(0), "The email cannot be empty.");
			return false;
		}
		return true;
	}
}