import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.JOptionPane;

/**
 * 
 * Simulated Game that simulates actions done in the game in a meaningful way
 * based on certain information gathered from the database.
 *
 * @author holmanah.
 *         Created Apr 14, 2017.
 */
public class Game {

	private Queue<String> chatlog;
	
	public Game() {
		this.chatlog = new LinkedList<>();
	}
	
	private void updatePlayers() {
		//TODO randomly determines whether a player is online or offline.
		//	a player that is online has a very low chance to go offline, and a player that is
		//	offline as a relatively low chance to go online.
		//	the chance for offline players to go online increases as the total number of players
		//	on the server is lower and decreases when it higher. the opposite applies to players going offline
		//	also runs doPlayerInteraction for each player that is online
		
		List<Integer> playersList = this.getPlayerList();
		int PlayerID;
		for (int i = 0; i < playersList.size(); i++) {
			PlayerID = playersList.get(i);
			doPlayerInteraction(PlayerID);
		}
	}

	/**
	 * 
	 * Randomly decides whether or not the player decided to buy from a shop
	 * if so, 
	 * 		-randomly chooses a shop that the merchant owns
	 * 		-then decides which item to buy randomly from the items at that shop
	 *
	 * @param PlayerID
	 */
	@SuppressWarnings("resource")
	private void doPlayerInteraction(int PlayerID) {
//		double popularity = 0;
//		try {
//			CallableStatement proc = Main.conn.prepareCall("{ ? = call dbo.getPopularity() }"); //TODO: implement
//			proc.registerOutParameter(1, Types.DOUBLE);
//			proc.execute();
//			popularity = proc.getDouble(1);
//		}
//		catch (SQLException exception) {exception.printStackTrace();}
		if (Math.random() < 0.3) {//popularity) {
			try {
				//call statement
				CallableStatement proc = Main.conn.prepareCall("{ ? = call dbo.PlayerOrdersItem(?, ?, ?, ?, ?, ?) }");
				proc.registerOutParameter(1, Types.INTEGER);
				proc.setInt(2, PlayerID);
				proc.setString(3, Main.MerchantID);
				proc.registerOutParameter(4, Types.VARCHAR);
				proc.registerOutParameter(5, Types.INTEGER);
				proc.registerOutParameter(6, Types.CHAR);
				proc.registerOutParameter(7, Types.CHAR);
				proc.execute();
				int returnVal = proc.getInt(1);

				if (returnVal == 0) {
					String timeStamp = new SimpleDateFormat("<yyyy/MM/dd>[HH:mm:ss]:").format(new Date());
					this.chatlog.add(timeStamp + " " + 
								proc.getString(4) + " just bought " + 
								proc.getInt(5) + " " + 
								proc.getString(6).trim() + " from " + 
								proc.getString(7).trim() + "!");
				}
				else {
					JOptionPane.showMessageDialog(null, "Player BuyOrder failed. Error code is: " + returnVal);
				}
//					Main.updateQueue.add(timeStamp + " did a Player Interaction for PlayerID: " + PlayerID);
			} 
			catch (SQLException exception) {exception.printStackTrace();}
			
		}
	}

	public List<Integer> getPlayerList() {
		//TODO gets the list of players currently online on the server
		//	does so by queriying the database for players on the current Server with Online = true
		List<Integer> dummyList = new ArrayList<>();
		dummyList.add(5);
		dummyList.add(6);
		dummyList.add(8);
		dummyList.add(9);
		return dummyList; //debug dummy return value
	}
	
	public String nextChatLine() {
		return this.chatlog.poll();
	}
	
	public void update() {
		//TODO updates the game (duh...)
		updatePlayers();
	}
	
}
