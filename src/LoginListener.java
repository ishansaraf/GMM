import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		//TODO queries the Merchant Table for the Username and Password
		//	if it finds an entry, sets the MerchantID
		Main.MerchantID = "";
	}
	
	public void keyVerify() {
		
	}
}
