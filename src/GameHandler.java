import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * Handles interfacing with the Game, and excecuting client side logic
 * for efficient handling of transactions automatically.
 *
 * @author holmanah.
 *         Created Apr 14, 2017.
 */
public class GameHandler implements Runnable {

	private String MerchantID;
	private Game game;
	private boolean shutDown;
	
	public GameHandler(String MerchantID) {
		this.MerchantID = MerchantID;
		this.game = new Game();
		this.shutDown = false;
	}
	
	@Override
	public void run() {
		while(!this.shutDown) {
			pollforUpdates();
			updateShops();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException exception) {
				// TODO Auto-generated catch-block stub.
				exception.printStackTrace();
			}
		}
	}
	/**
	 * Would be code for polling for changes in the game state if there were a game
	 * but because we are only simulating a game, this is actually just causing the game
	 * to update.
	 *
	 */
	private void pollforUpdates() {
		this.game.update();
	}

	/**
	 * runs doShopRestock for each shop belonging to the Merchant
	 *
	 */
	private void updateShops() {
		List<String> shopsList = Main.getShopList();
		for (int i = 0; i < shopsList.size(); i++) {
			doShopRestock(shopsList.get(i));
		}
	}
	/**
	 * gets the list of players currently online on the server from the (simulated) Game
	 *
	 * @return
	 */
	private List<String> getPlayerList() {
		return this.game.getPlayerList();
	}
	
	/**
	 * TODO gets list of all Servers (uses query)
	 *
	 * @return
	 */
	private List<String> getServerList() {
		//DEBUG CODE START
		List<String> dummyList = new ArrayList<>();
		dummyList.add("Lesenburg");
		dummyList.add("Icodracy");
		dummyList.add("Mascronias");
		dummyList.add("Delcromina");
		return dummyList;
		//DEBUG CODE END
	}
	
	/**
	 * TODO randomly decides whether or not the Storefront should restock with higher bias
	 * when Inventory is lower. If so, determines which items to stock by choosing with bias
	 * for Items bought recently that have a high AVG(Profit/TimeSpan). Also Biased toward Suppliers
	 *  that are closer to the Storefront and with higher discounts.
	 *   This is calculated using queries to the: 
	 *   	-StoreStocksItems Table for counting size of inventory
	 *   	-BuyOrder Table for items within a recent Datetime for the particular ShopID 
	 *   		counting the total UnitPrice*Quantity and finding the oldest order of said item to calculate the TimeSpan
	 *   	-Storefront Table to get the current Storefront's location
	 *   	-Supplier Table to get all the Suppliers within a certain distance to this Storefront sorted in order of 
	 *   		highest discount to lowest discount and then from closest to farthest.
	 *   	-Update the StockOrder Table with the new StockOrder
	 *   	-Update StoreStocksItems Table to fit with the data from the StockOrder.
	 *   	-Update Storefront Table to reflect the new funds
	 *
	 * @param ShopID
	 */
	private void doShopRestock(String ShopID) {
		//DEBUG CODE START
		String timeStamp = new SimpleDateFormat("<yyyy/MM/dd>[HH:mm:ss]:").format(new Date());
		if (Math.random() < 0.1) Main.updateQueue.add(timeStamp + " Restocked the Shop: " + ShopID);
		//DEBUG CODE END
	}

	public void shutDown() {
		this.shutDown = true;
	}
}
