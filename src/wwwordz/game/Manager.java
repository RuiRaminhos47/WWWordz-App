package wwwordz.game;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

/**
 * <b>This class is a singleton and acts as a facade for other classes in this package. 
 * Methods in this class are delegated in instances of these classes.</b><br>
 * @author Rúben Lôpo, Rui Ramos
 */
public class Manager
extends java.lang.Object {
	
	static final java.util.concurrent.ScheduledExecutorService worker = Executors.newScheduledThreadPool(1);	
	static final long INITIAL_TIME = 0;
	private static Manager instance = null;
	Round round = null;
	
	private Manager() {
		round = new Round();
		Runnable run = () -> round = new Round();
		worker.scheduleAtFixedRate(run, Round.getRoundDuration(), Round.getRoundDuration(), TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Single instance of Manager;
	 * @return instance
	 */
	public static Manager getInstance() {
		if(instance==null) instance = new Manager();
		return instance;
	}
	
	public long timeToNextPlay() {
		return round.getTimetoNextPlay();
	}
	
	/**
	 * Register user with nick and password for current round
	 * @param nick - of user to register
	 * @param password - of user to register
	 * @return time in seconds for next found
	 * @throws WWWordzException - if user is invalid
	 */
	public long register(java.lang.String nick,
            			 java.lang.String password)
    throws WWWordzException {
		return round.register(nick,password);
	}
	
	/**
	 * Get table of current round
	 * @return table
	 * @throws WWWordzException - if game has not started
	 */
	public Puzzle getPuzzle()
    throws WWWordzException {
		return round.getPuzzle();
	}
	
	/**
	 * Set number of points obtained by user in current round
	 * @param nick - of user
	 * @param points - to set
	 * @throws WWWordzException - if game is not over or reporting has ended
	 */
	public void setPoints(java.lang.String nick,
            			  int points)
    throws WWWordzException {
		round.setPoints(nick, points);
	}
	
	/**
	 * List of players in current round sorted by points
	 * @return list of ranks
	 * @throws WWWordzException - if players can still report values
	 */
	public java.util.List<Rank> getRanking()
    throws WWWordzException {
		return round.getRanking();
	}
	
	
}
