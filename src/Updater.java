import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;

/**
 * Updates the updateFeed with queued messages every INTERVAL_MS milliseconds
 *
 * @author holmanah.
 *         Created Apr 14, 2017.
 */
public class Updater implements Runnable {
	
	static final int INTERVAL_MS = 200;
	private JFrame frame;
	private DefaultListModel<String> updateModel;
	private JList<String> updateFeed;
	
	public Updater(JFrame frame, DefaultListModel<String> updateModel, JList<String> updateFeed) {
		this.frame = frame;
		this.updateModel = updateModel;
		this.updateFeed = updateFeed;
	}
	
	@Override
	public void run() {
		while(true) {
			if (Main.updateQueue.peek() != null) { 
				this.updateModel.addElement(Main.updateQueue.poll());
				int curIndex = (this.updateModel.size()-1);
				if (curIndex <= this.updateFeed.getLastVisibleIndex() + 1 && curIndex >= this.updateFeed.getFirstVisibleIndex()){
					this.updateFeed.ensureIndexIsVisible(curIndex);
				}
				this.frame.repaint();
			}
			try {
				Thread.sleep(INTERVAL_MS);
			} catch (InterruptedException exception) {
				exception.printStackTrace();
			}
		}
	}

}
