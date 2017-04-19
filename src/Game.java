import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * Simulated Game that simulates actions done in the game in a meaningful way
 * based on certain information gathered from the database.
 *
 * @author holmanah.
 *         Created Apr 14, 2017.
 */
public class Game {

	
	private void updatePlayers() {
		//TODO randomly determines whether a player is online or offline.
		//	a player that is online has a very low chance to go offline, and a player that is
		//	offline as a relatively low chance to go online.
		//	the chance for offline players to go online increases as the total number of players
		//	on the server is lower and decreases when it higher. the opposite applies to players going offline
		//	also runs doPlayerInteraction for each player that is online
		List<String> playersList = this.getPlayerList();
		String PlayerID;
		for (int i = 0; i < playersList.size(); i++) {
			PlayerID = playersList.get(i);
			doPlayerInteraction(PlayerID);
		}
	}

	private void doPlayerInteraction(String PlayerID) {
		//TODO randomly decides whether or not the player decided to buy from a shop
		// and if so, checks the shops he has previously bought at and rolls randomly with bias
		// toward the shop that he has bought from the most, and then decides which item to buy by 
		// rolling randomly with bias towards items previously bought at said shop.
		
		String timeStamp = new SimpleDateFormat("<yyyy/MM/dd>[HH:mm:ss]:").format(new Date());
		if (Math.random() < 0.5) Main.updateQueue.add(timeStamp + " did a Player Interaction for PlayerID: " + PlayerID);
		//if (Math.random() < 0.5) System.out.println("did a Player Interaction for PlayerID: " + PlayerID);
	}

	public List<String> getPlayerList() {
		//TODO gets the list of players currently online on the server
		//	does so by queriying the database for players on the current Server with Online = true
		List<String> dummyList = new ArrayList<>();
		dummyList.add("Scott");
		dummyList.add("Leroy");
		dummyList.add("Kappa");
		dummyList.add("XxxH@X0R1337xxX");
		return dummyList; //debug dummy return value
	}
	
	public void update() {
		//TODO updates the game (duh...)
		updatePlayers();
	}
	
}
