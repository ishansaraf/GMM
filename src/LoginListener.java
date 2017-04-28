import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginListener implements ActionListener {

	private JTextField usernameField;
	private JPasswordField passwordField;

	public LoginListener(JTextField usernameField, JPasswordField passwordField) {
		this.usernameField = usernameField;
		this.passwordField = passwordField;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		//Queries the Merchant Table for the Username and Password
		//	if it finds an entry, sets the MerchantID
		Main.MerchantID = "";
	}
}
