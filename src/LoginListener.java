import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
<<<<<<< HEAD
		try {
			Statement login = Main.conn.createStatement();
			String loginQuery = "";
			ResultSet rs = login.executeQuery(loginQuery);
			Main.MerchantID = rs.getString("MerchantID");
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
=======
		Main.MerchantID = "";
>>>>>>> b1c6ecaa8d656b777e25979e4721ed7c24358ff0
	}
}
