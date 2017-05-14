import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

import org.apache.commons.validator.EmailValidator;
import org.jasypt.util.password.StrongPasswordEncryptor;

public class RegistrationPage implements GMMPage {

	// Fields for GUI
	GMMPage curPage;
	JFrame frame;
	JPanel centerPanel;
	JTextField username;
	JPasswordField password;
	JPasswordField confirm;
	JTextField email;

	public class SubmitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// Only trying to encrypt password if input fields correctly entered
			if (validateInputs()) {
				String pwd = encryptPassword();

				// Check that password encrypted correctly
				if (pwd.trim().length() != 0) {
					String user = username.getText();
					String emailID = email.getText();
					addMerchant(user, emailID, pwd);

					// Blanking out password string
					pwd = "";

					// Closing window, reopening login
					Main.register = false;
					Main.relaunch = true;
					frame.dispose();
				}
			}
		}

	}

	public RegistrationPage(JFrame frame) {
		this.frame = frame;

		// Reopening login page if closed
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				Main.register = false;
				Main.relaunch = true;
				frame.dispose();
			}
		});

		// Create panels
		centerPanel = new JPanel();
		JPanel userPanel = new JPanel();
		JPanel passPanel = new JPanel();
		JPanel confirmPanel = new JPanel();
		JPanel emailPanel = new JPanel();
		JPanel registerPanel = new JPanel();

		// Create labels, extra spaces is for alignment of fields
		JLabel userLabel = new JLabel("        *Username:");
		JLabel passLabel = new JLabel("        *Password:");
		JLabel confirmLabel = new JLabel("*Confirm Password:");
		JLabel emailLabel = new JLabel("        *Email ID:");

		// Create fields
		username = new JTextField(30);
		password = new JPasswordField(30);
		confirm = new JPasswordField(30);
		email = new JTextField(30);

		// Create button
		JButton registerButton = new MenuButton("Register", new SubmitListener());

		// Create header
		JLabel header = new JLabel("Add User Details", SwingConstants.CENTER);

		// Set foregrounds
		userLabel.setForeground(Main.TEXT_COLOR);
		passLabel.setForeground(Main.TEXT_COLOR);
		confirmLabel.setForeground(Main.TEXT_COLOR);
		emailLabel.setForeground(Main.TEXT_COLOR);
		username.setForeground(Main.TEXT_COLOR);
		password.setForeground(Main.TEXT_COLOR);
		confirm.setForeground(Main.TEXT_COLOR);
		email.setForeground(Main.TEXT_COLOR);
		header.setForeground(Main.TEXT_COLOR);

		// Limiting fields
		((AbstractDocument) username.getDocument()).setDocumentFilter(new LimitDocumentFilter(30));
		((AbstractDocument) password.getDocument()).setDocumentFilter(new LimitDocumentFilter(30));
		((AbstractDocument) confirm.getDocument()).setDocumentFilter(new LimitDocumentFilter(30));
		((AbstractDocument) email.getDocument()).setDocumentFilter(new LimitDocumentFilter(30));

		// Set fonts
		userLabel.setFont(Main.FIELD_FONT);
		passLabel.setFont(Main.FIELD_FONT);
		confirmLabel.setFont(Main.FIELD_FONT);
		emailLabel.setFont(Main.FIELD_FONT);
		registerButton.setFont(Main.FIELD_FONT);
		username.setFont(Main.FIELD_FONT);
		password.setFont(Main.FIELD_FONT);
		confirm.setFont(Main.FIELD_FONT);
		email.setFont(Main.FIELD_FONT);
		header.setFont(Main.FIELD_FONT);

		// Add fields to panels
		userPanel.add(userLabel);
		userPanel.add(username);
		emailPanel.add(emailLabel);
		emailPanel.add(email);
		passPanel.add(passLabel);
		passPanel.add(password);
		confirmPanel.add(confirmLabel);
		confirmPanel.add(confirm);
		registerPanel.add(registerButton);
		centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		centerPanel.add(header, BorderLayout.NORTH);
		centerPanel.add(userPanel);
		centerPanel.add(emailPanel);
		centerPanel.add(passPanel);
		centerPanel.add(confirmPanel);
		centerPanel.add(registerPanel);

		// Set background colors
		centerPanel.setBackground(Main.BG_COLOR);
		userPanel.setBackground(Main.BG_COLOR);
		passPanel.setBackground(Main.BG_COLOR);
		confirmPanel.setBackground(Main.BG_COLOR);
		emailPanel.setBackground(Main.BG_COLOR);
		registerPanel.setBackground(Main.BG_COLOR);
		username.setBackground(Main.FIELD_COLOR);
		password.setBackground(Main.FIELD_COLOR);
		confirm.setBackground(Main.FIELD_COLOR);
		email.setBackground(Main.FIELD_COLOR);
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
		// nothing
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

		// Blanking out password fields
		password.setText("");
		confirm.setText("");
		
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
				JOptionPane.showMessageDialog(null, "User " + user + " was successfully added!");

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
		String passConfirm = confirm.getText();

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
		// Checking that email is valid
		if (!isValidEmail(emailID)) {
			JOptionPane.showMessageDialog(centerPanel.getComponent(0), "Please enter a valid e-mail id.");
			return false;
		}
		// Checking that password match
		if (!pass.equals(passConfirm)) {
			JOptionPane.showMessageDialog(centerPanel.getComponent(0), "Please make sure that your passwords match.");
			return false;
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	private boolean isValidEmail(String emailID) {
		EmailValidator validator = EmailValidator.getInstance();
		if (validator.isValid(emailID))
			return true;
		return false;
	}
}