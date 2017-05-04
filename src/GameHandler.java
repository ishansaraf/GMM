import java.util.ArrayList;
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
		while(true) {
			String chatline = this.game.nextChatLine();
			if (chatline == null) break;
			this.parseChatLine(chatline);
		}
		
		
		
		this.game.update();
	}

	private void parseChatLine(String chatline) {
		//TODO additional constraints to note:
		//	nothing should have a : in its name
		//  also shouldn't contain spaces
		String noTimeLine = chatline.substring(24);
		String playerName = noTimeLine.split(":")[0];
		boolean valid = this.isValidPlayer(playerName);
		if (!valid && playerName.contains(" just bought ")) {
			String ajb = playerName.split(" just bought ")[1];
			if (ajb.contains(" from ") && ajb.charAt(ajb.length()-1) == '!'){
				//Its a buy order
				this.sendBuyOrder(chatline);
				Main.updateQueue.add(chatline);
			}
		}
		else if (!valid && playerName.contains(" wanted ")) {
			String[] ajbArr = playerName.split(" wanted ");
			ajbArr = ajbArr[ajbArr.length-1].split(", ");
			String ajb = ajbArr[ajbArr.length-1];
			if (ajb.equals("but there was not enough in stock!")){
				//Its a buy order restock reccomend message
				Main.updateQueue.add(chatline);
			}
		}
//		System.out.println(chatline);
	}

	private boolean isValidPlayer(String playerName) {
		// TODO create stored proc for checking if the playerName is valid
		// REMEMBER TO SANITIZE INPUT
		return false;
	}

	private void sendBuyOrder(String chatline) {
		// TODO greate stored proc for sending in buy orders
//		System.out.println(chatline + " buyOrder to be placed");
	}

//	/**
//	 * runs doShopRestock for each shop belonging to the Merchant
//	 *
//	 */
//	private void updateShops() {
//		List<String> shopsList = Main.getShopList();
//		for (int i = 0; i < shopsList.size(); i++) {
////			doShopRestock(shopsList.get(i));
//		}
//	}
	
	/**
	 * gets the list of players currently online on the server from the (simulated) Game
	 *
	 * @return
	 */
	private List<Integer> getPlayerList() {
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
	
//	/**
//	 * TODO randomly decides whether or not the Storefront should restock with higher bias
//	 * when Inventory is lower. If so, determines which items to stock by choosing with bias
//	 * for Items bought recently that have a high AVG(Profit/TimeSpan). Also Biased toward Suppliers
//	 *  that are closer to the Storefront and with higher discounts.
//	 *   This is calculated using queries to the: 
//	 *   	-StoreStocksItems Table for counting size of inventory
//	 *   	-BuyOrder Table for items within a recent Datetime for the particular ShopID 
//	 *   		counting the total UnitPrice*Quantity and finding the oldest order of said item to calculate the TimeSpan
//	 *   	-Storefront Table to get the current Storefront's location
//	 *   	-Supplier Table to get all the Suppliers within a certain distance to this Storefront sorted in order of 
//	 *   		highest discount to lowest discount and then from closest to farthest.
//	 *   	-Update the StockOrder Table with the new StockOrder
//	 *   	-Update StoreStocksItems Table to fit with the data from the StockOrder.
//	 *   	-Update Storefront Table to reflect the new funds
//	 *
//	 * @param ShopID
//	 */
//	private void doShopRestock(String ShopID) {
//		//DEBUG CODE START
//		String timeStamp = new SimpleDateFormat("<yyyy/MM/dd>[HH:mm:ss]:").format(new Date());
//		if (Math.random() < 0.05) Main.updateQueue.add(timeStamp + " Restocked the Shop: " + ShopID);
//		//DEBUG CODE END
//	}

	public void shutDown() {
		this.shutDown = true;
	}
}
