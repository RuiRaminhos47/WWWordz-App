package wwwordz.game;

/**
 * <b>A player of WWWordz, including authentication data (name and password), 
 * current round and accumulated points.</b><br>
 * @author Rúben Lôpo, Rui Ramos
 */
public class Player
extends java.lang.Object
implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	java.lang.String nick;
	java.lang.String password;
	int points;
	int accumulated;
	
	/**
	 * Creates a player from nick and password
	 * @param nick - of player
	 * @param password - of player
	 */
	public Player(java.lang.String nick,
            	  java.lang.String password) {
		this.nick = nick;
		this.password = password;
		this.points = 0;
		this.accumulated = 0;
	}
	
	/**
	 * Current nick of this player
	 * @return nick of this player
	 */
	public java.lang.String getNick() {
		return this.nick;
	}
	
	/**
	 * Update nick of this player
	 * @param nick - of player
	 */
	public void setNick(java.lang.String nick) {
		this.nick = nick;
	}
	
	/**
	 * Current password of this player
	 * @return password
	 */
	public java.lang.String getPassword() {
		return this.password;
	}
	
    /**
     * Update password of this player
     * @param password - of player
     */
	public void setPassword(java.lang.String password) {
		this.password = password;
	}
	
	/**
	 * Current points of this player
	 * @return points of player
	 */
	public int getPoints() {
		return this.points;
	}
	
	/**
	 * Update points of current round for this player These points are added to accumulated points
	 * @param points - of player
	 */
	public void setPoints(int points) {
		this.points = points;
		this.accumulated += points;
	}
	
	/**
	 * Current accumulated points of this player
	 * @return accumulated points of this player
	 */
	public int getAccumulated() {
		return this.accumulated;
	}
	
	/**
	 * Update accumulated points of this player
	 * @param accumulated - points
	 */
	public void setAccumulated(int accumulated) {
		this.accumulated = accumulated;
	}
}
