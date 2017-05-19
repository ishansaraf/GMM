import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 * Updates the updateFeed with queued messages every INTERVAL_MS milliseconds
 *
 * @author holmanah.
 *         Created Apr 14, 2017.
 */
public class Updater implements Runnable {
	
	static final int INTERVAL_MS = 100;
	private DefaultListModel<String> updateModel;
	private JList<String> updateFeed;
	private boolean shutDown;
	
	public Updater(DefaultListModel<String> updateModel, JList<String> updateFeed) {
		this.updateModel = updateModel;
		this.updateFeed = updateFeed;
		this.shutDown = false;
	}
	
	@Override
	public void run() {
		while(!this.shutDown) {
			if (Main.updateQueue.peek() != null) { 
				this.updateModel.addElement(Main.updateQueue.poll());
				int curIndex = (this.updateModel.size()-1);
				if (curIndex <= this.updateFeed.getLastVisibleIndex() + 1 && curIndex >= this.updateFeed.getFirstVisibleIndex()){
					this.updateFeed.ensureIndexIsVisible(curIndex);
				}
				Main.mainframe.repaint();
			}
			try {
				Thread.sleep(INTERVAL_MS);
			} catch (InterruptedException exception) {
				exception.printStackTrace();
			}
		}
	}

	public void shutDown() {
		this.shutDown = true;
	}

}
