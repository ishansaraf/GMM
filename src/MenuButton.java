import java.awt.event.ActionListener;

import javax.swing.JButton;

public class MenuButton extends JButton {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 * Creates a menu button with text.
	 * 
	 * @param text - the text of the button
	 */
	public MenuButton(String text, ActionListener listener) {
		super(text);
		this.setBackground(Main.MENU_BUTTON_COLOR);
		this.setForeground(Main.TEXT_COLOR);
		this.setFocusPainted(false);
		this.addActionListener(listener);
	}
}
