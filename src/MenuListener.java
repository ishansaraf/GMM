import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuListener implements ActionListener {
	
	private GMMPage page;
	
	public MenuListener(GMMPage page) {
		this.page = page;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
//		if (Main.curPage == null) System.out.println("null");
		this.page.changeToPage();
	}

}
