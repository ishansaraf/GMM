import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		//Queries the Merchant Table for the Username and Password
		//	if it finds an entry, sets the MerchantID
		try {
			Statement login = Main.conn.createStatement();
			String loginQuery = "";
			ResultSet rs = login.executeQuery(loginQuery);
			Main.MerchantID = rs.getString("MerchantID");
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}
	
	public void keyVerify() {
		
	}
}
