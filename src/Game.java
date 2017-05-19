import java.io.File;
import java.io.FileNotFoundException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

import javax.swing.JOptionPane;

/**
 * 
 * Simulated Game that simulates player actions done in the game
 *
 * @author holmanah.
 *         Created Apr 14, 2017.
 */
public class Game {

	private static final String DICTIONARY_LOCATION = "english.cleaned.all.10.txt";
	private Queue<String> chatlog;
	private ArrayList<Integer> playerList;
	private Map<Boolean, ArrayList<Integer>> onOffMap;
	private ArrayList<String> wordDict;
	
	public Game() {
		this.chatlog = new LinkedList<>();
		this.playerList = new ArrayList<>();
		this.onOffMap = new HashMap<>();
		this.onOffMap.put(true, new ArrayList<>());
		this.onOffMap.put(false, new ArrayList<>());
		this.wordDict = new ArrayList<>();
		try {
			Scanner scanner = new Scanner(new File(DICTIONARY_LOCATION));
			while(scanner.hasNextLine()) {
				String add = scanner.nextLine().trim();
				if (add.contains("/")) continue;
				this.wordDict.add(add);
			}
		} catch (FileNotFoundException exception) {
			exception.printStackTrace();
		}
	}
	
	private void updatePlayers() {
		if (Main.getShopList().isEmpty()) {
			return;
		}
		updatePlayerList();
		for (Integer playerID : this.onOffMap.get(true)) {
			doPlayerInteraction(playerID);
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
		if (Math.random() < 0.001) {
			try {
				//call statement
				CallableStatement proc = Main.conn.prepareCall("{ ? = call dbo.PlayerOrdersItem(?, ?, ?, ?, ?, ?) }");
				proc.registerOutParameter(1, Types.INTEGER);
				proc.setInt(2, PlayerID);
				proc.setString(3, Main.MerchantID);
				proc.registerOutParameter(4, Types.VARCHAR);
				proc.registerOutParameter(5, Types.INTEGER);
				proc.registerOutParameter(6, Types.VARCHAR);
				proc.registerOutParameter(7, Types.VARCHAR);
				proc.execute();
				int returnVal = proc.getInt(1);

				String timeStamp = new SimpleDateFormat("<yyyy/MM/dd>[HH:mm:ss]:").format(new Date());
				if (returnVal == 0) {
					this.chatlog.add(timeStamp + " " + 
								proc.getString(4) + " just bought " + 
								proc.getInt(5) + " " + 
								proc.getString(6) + " from " + 
								proc.getString(7) + "!");
				}
				else if (returnVal == 1) {
					this.chatlog.add(timeStamp + " " + 
								proc.getString(4) + " wanted " + 
								proc.getInt(5) + " " + 
								proc.getString(6) + " from " + 
								proc.getString(7) + ", but there was not enough in stock!");
				}
				else if (returnVal == 2) {
					this.chatlog.add(timeStamp + " " + 
								proc.getString(4) + " stopped by " + 
								proc.getString(7) + " but there was nothing to buy.");
				}
				else {
					JOptionPane.showMessageDialog(null, "Player PlayerOrdersItem failed. Error code is: " + returnVal);
				}
//					Main.updateQueue.add(timeStamp + " did a Player Interaction for PlayerID: " + PlayerID);
			} 
			catch (SQLException exception) {exception.printStackTrace();}
			
		}
	}

	public void updatePlayerList() {
		if (this.playerList.isEmpty()) {
			try {
				CallableStatement proc = Main.conn.prepareCall("{call dbo.getPlayerList()}");
				ResultSet rs = proc.executeQuery();
				while (rs.next()) {
					int toAdd = rs.getInt("PlayerID");
					this.playerList.add(toAdd);
					if (Math.random() > 0.5) {
						this.onOffMap.get(true).add(toAdd);
						System.out.println(toAdd);
					}
					else {
						this.onOffMap.get(false).add(toAdd);
					}
				}
			} catch (SQLException exception) {
				exception.printStackTrace();
			}
		}
		int player;
		for (int i = 0; i < this.onOffMap.get(false).size()*0.01; i++) {
			int r1 = (int) (Math.random()*this.onOffMap.get(true).size());
			int r2 = (int) (Math.random()*this.onOffMap.get(false).size());
			player = onOffMap.get(true).get(r1);
			onOffMap.get(true).set(r1, onOffMap.get(false).get(r2));
			onOffMap.get(false).set(r2, player);
		}
		
//		for (int player : this.playerList) {
//			double rand = Math.random();
//			if (rand < 0.5) {
//				onOffMap.get(true).add(player);
//			}
//			else {
//				onOffMap.get(false).add(player);
//			}
//		}
	}
	
	public String nextChatLine() {
		return this.chatlog.poll();
	}
	
	public void update() {
		updatePlayers();
		for (int i = 0; i < this.onOffMap.get(true).size()*0.0003; i++) {			
			junkifyChat();
		}
	}

	private void junkifyChat() {
		//make/get a bunch of junky MMO chat messages, and add some edge cases
		// for testing the parser in GameHandler
		String timeStamp = new SimpleDateFormat("<yyyy/MM/dd>[HH:mm:ss]:").format(new Date());
		int randNumOfWords = (int) (Math.random()*10);
		int randomPlayer = this.onOffMap.get(true).get( (int) (Math.random() * this.onOffMap.get(true).size()) );
		if (randNumOfWords == 0) return;
		StringBuilder chatLine = new StringBuilder();
		chatLine.append(timeStamp);
		chatLine.append(' ');
		try {
			CallableStatement proc = Main.conn.prepareCall("{call dbo.getPlayerNameByID(?)}");
			proc.setInt(1, randomPlayer);
			ResultSet rs = proc.executeQuery();
			if (rs.next()) {
				chatLine.append(rs.getString("Username"));
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		chatLine.append(':');
		for (int i = 0; i < randNumOfWords; i++) {
			chatLine.append(' ');
			chatLine.append(this.wordDict.get( (int)(Math.random()*this.wordDict.size()) ));
		}
		this.chatlog.add(chatLine.toString());
	}
	
}
