package wwwordz.shared;

/**
 * <b>A row in the ranking table. 
 * Basically all the data of a player except the password.</b><br>
 * @author Rúben Lôpo, Rui Ramos
 */
public class Rank
extends java.lang.Object
implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String nick;
	int points;
	int accumulated;
	
	/**
	 * Empty constructor. Creates an empty line.
	 */
	public Rank() {}
	
	/**
	 * An instance with all fields initialized
	 * @param nick - of player
	 * @param points - of player
	 * @param accumulated - points of player
	 */
	public Rank(java.lang.String nick,
            	int points,
            	int accumulated) {
		this.nick = nick;
		this.points = points;
		this.accumulated = accumulated;
	}
	
	/**
	 * Current nick in this rank
	 * @return nick of this rank's play
	 */
	public java.lang.String getNick() {
		return this.nick;
	}
	
	/**
	 * Change the nick in this rank
	 * @param nick - to change
	 */
	public void setNick(java.lang.String nick) {
		this.nick = nick;
	}
	
	/**
	 * Current points in this rank
	 * @return points
	 */
	public int getPoints() {
		return this.points;
	}
	
	/**
	 * Change points in this rank
	 * @param points - to change
	 */
	public void setPoints(int points) {
		this.points = points;
	}
	
	/**
	 * Current accumulated points in this rank
	 * @return accumulated points
	 */
	public int getAccumulated() {
		return this.accumulated;
	}
	
	/**
	 * Change accumulated points in this rank
	 * @param accumulated - points to set
	 */
	public void setAccumulated(int accumulated) {
		this.accumulated = accumulated;
	}
}
