import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class RestockPage implements GMMPage {

	private JPanel centerPanel;
	
	public RestockPage() {
		//create panels
		this.centerPanel = new JPanel();

		//set BG colors
		this.centerPanel.setBackground(Main.BG_COLOR);
		this.centerPanel.setBorder(BorderFactory.createLineBorder(Color.black));
	}

	@Override
	public void changeToPage() {
		if (Main.curPage != this) {
			Main.curPage.unShow();
			Main.mainframe.add(this.centerPanel, BorderLayout.CENTER);
			Main.curPage = this;
			Main.mainframe.revalidate();
			Main.mainframe.repaint();
			System.out.println("RestockPage Loaded");
		}
	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub.

	}

	@Override
	public void unShow() {
		if (this.centerPanel != null) Main.mainframe.remove(this.centerPanel);
		System.out.println("RestockPage Unloaded");
	}

}
