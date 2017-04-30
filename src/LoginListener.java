import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.jasypt.util.password.StrongPasswordEncryptor;

public class LoginListener implements ActionListener, KeyListener{

	private JTextField usernameField;
	private JPasswordField passwordField;

	public LoginListener(JTextField usernameField, JPasswordField passwordField) {
		this.usernameField = usernameField;
		this.passwordField = passwordField;
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		login();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_ENTER) {
			login();
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		// nothing
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// nothing
	}
	
	private void login(){
		// Queries the Merchant Table for the Username and Password
		// if it finds an entry, sets the MerchantID
		try {
			CallableStatement proc = Main.conn.prepareCall("{? = call checkLogin(?, ?)}");
			proc.registerOutParameter(1, Types.INTEGER);
			proc.setString(2, usernameField.getText());
			proc.registerOutParameter(3, Types.NVARCHAR);
			proc.execute();
			int status = proc.getInt(1);
			boolean match = false;
			if (status == 0) {
				String hash = proc.getNString(3);
				StrongPasswordEncryptor spe = new StrongPasswordEncryptor();
				char[] password = passwordField.getPassword();
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < password.length; i++) {
					sb.append(password[i]);
					password[i] = 0;
				}
				match = spe.checkPassword(sb.toString(), hash);
			}
			if (!match) {
				JOptionPane.showMessageDialog(null, "The username or password is invalid.");
			} else {
				proc = Main.conn.prepareCall("{call get_merchant_UID(?, ?)}");
				proc.setString(1, usernameField.getText());
				proc.registerOutParameter(2, Types.NVARCHAR);
				proc.execute();
				Main.MerchantID = proc.getNString(2);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
