import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
			Main.register = true;
			Main.relaunch = false;
	}
}