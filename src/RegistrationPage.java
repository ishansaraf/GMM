import java.sql.CallableStatement;
import java.sql.Types;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jasypt.util.password.StrongPasswordEncryptor;

public class RegistrationPage implements GMMPage {

	// Fields for GUI
	JPanel centerPanel;
	JTextField username;
	JTextField password;
	JTextField email;

	@Override
	public void changeToPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void unShow() {
		// TODO Auto-generated method stub

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
				JOptionPane.showMessageDialog(null, "The user already exists in the database. Please input new parameters and try again.");
			} else if (returnVal == 0) {
				JOptionPane.showMessageDialog(null, "User" + user + " was successfully added!");
				
				// Blanking out fields
				username.setText("");
				password.setText("");
				email.setText("");
				
				// TODO: Decide whether successful insertion should just return to the login page
			} else {
				JOptionPane.showMessageDialog(null, "The insertion of the user failed. The error code is: " + returnVal);
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
