package wwwordz.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import wwwordz.shared.WWWordzException;

/**
 * <b>Persistent collection of players indexed by nick.
 * Each player has nick, password, points and accumulated points. 
 * Data is persisted using serialization and backup each time a new user is created or points are changed.</b><br>
 * @author Rúben Lôpo, Rui Ramos
 */
public class Players
extends java.lang.Object
implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Players instance = null;
	private static File homeDir = new File(System.getProperty("user.dir"));
	private static File playersInfoFile = new File(homeDir, "players.ser");
	private HashMap<String, Player> playersHashMap = new HashMap<>();

	private static ObjectOutputStream stream1;

	private static ObjectInputStream stream2;
	
	public Players() {}
	
	/**
	 * Current home directory, where the data file is stored
	 * @return home directory
	 */
	public static java.io.File getHome() {
		return Players.homeDir;
	}
	
	/**
	 * Update home directory, where the data file is stored
	 * @param home - directory
	 */
	public static void setHome(java.io.File home) {
		Players.homeDir = home;
		Players.playersInfoFile = new File(homeDir, "players.ser");
	}
	 
	/**
	 * Get single instance of this class
	 * @return singleton
	 */
	public static Players getInstance()  {
		if(instance==null) {
			try {
				if(restore()!=null) instance = restore();
				else instance = new Players();
			}
			catch (WWWordzException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
	
	/**
	 * Verify player's password. If player doesn't exist yet then it is created with given password.
	 * @param nick - of player
	 * @param password - of player
	 * @return true if passwords match; false otherwise
	 */
	public boolean verify(java.lang.String nick,
            			  java.lang.String password) {
		if(playersHashMap.containsKey(nick)) {
			String playerPassword = playersHashMap.get(nick).password;
			if(playerPassword.equals(password)) return true;
		}
		else {
			Player newPlayer = new Player(nick, password);
			playersHashMap.put(nick, newPlayer);
			return true;
		}
		return false;
	}
	
	/**
	 * Reset points of current round while keeping accumulated points
	 * @param nick - of player
	 * @throws WWWordzException - if player is unknown
	 */
	public void resetPoints(java.lang.String nick)
            throws WWWordzException {
		addPoints(nick, 0);
	}
	
	/**
	 * Add points to player
	 * @param nick - of player
	 * @param points - to add
	 * @throws WWWordzException - if player is unknown
	 */
	public void addPoints(java.lang.String nick,
            			  int points)
    throws WWWordzException {
		if(!playersHashMap.containsKey(nick)) 
			throw new WWWordzException("This is not a valid player.");
		else {
			Player player = playersHashMap.get(nick);
			player.setPoints(points);
			backup();
		}
	}
	
	public Player getPlayer(java.lang.String nick) {
		return playersHashMap.get(nick);
	}
	
	/**
	 * Clears all players stored in this instances. 
	 * This method is used for testing purposes only.
	 */
	public void cleanup() {
		String[] nicks = new String[playersHashMap.size()];
		int i=0;
		for(String nick : playersHashMap.keySet()) {
			nicks[i++] = nick;
		}
		for(int j=0; j<nicks.length; j++) {
			playersHashMap.remove(nicks[j]);
		}		
	}
	
	/**
	 * Backup players data into a file
	 * @throws WWWordzException - if it can't write the file
	 */
	public static void backup() throws WWWordzException {

		synchronized (playersInfoFile) {
			try {
				stream1 = new ObjectOutputStream(new FileOutputStream(playersInfoFile));
				stream1.writeObject(getInstance());
			} 
			catch (Exception e) {
				throw new WWWordzException("Backup failed.");
			}
		}
	}
	
	/**
	 * Restore players data from a file
	 * @return playersData
	 * @throws WWWordzException - if it can't read the file
	 */
	public synchronized static Players restore()
			throws WWWordzException {
		
		Players playersData = null;
		if (playersInfoFile.canRead()) {
			synchronized (playersInfoFile) {
				try {
					stream2 = new ObjectInputStream(new FileInputStream(playersInfoFile));
					playersData = (Players)stream2.readObject();
				} 
				catch (Exception e) {
					throw new WWWordzException("Restore failed");
				}
			}
		}
		return playersData;
	}
}