package wwwordz.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wwwordz.puzzle.Generator;
import wwwordz.shared.Configs;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

/**
 * <b>A round has 4 sequential stages: <br>
 * 1. join - client join the round <br>
 * 2. play - client retrieves puzzle and player solve puzzle <br>
 * 3. report - clients report points back to server <br>
 * 4. ranking - clients ask for rankings <br>
 * Each stage has a specific duration and the round method can only be executed within a limited time frame. 
 * The duration of each stage can be checked or changed with static setters and getters. 
 * The following method should be executed in the associated stages. <br>
 * 1. register() - join <br>
 * 2. getPuzzle() - play <br>
 * 3. setPoints() - register <br>
 * 4. getRanking() - ranking <br>
 *When executed outside their stages these methods raise a WWWordzException.</b><br>
 * @author Rúben Lôpo, Rui Ramos
 */
public class Round
extends java.lang.Object {
	
	private static long joinStageDuration    	=  Configs.JOIN_STAGE_DURATION;
	private static long playStageDuration    	=  Configs.PLAY_STAGE_DURATION;
	private static long reportStageDuration 	=  Configs.REPORT_STAGE_DURATION;
	private static long rankingStageSuration 	=  Configs.RANKING_STAGE_DURATION;
	private static long roundDuration           =  0L;
	private static List<Rank> rankingGeral = null;
	
	public Round() {}
	
	/**
	 * Duration of join stage in milliseconds
	 * @return join duration in milliseconds
	 */
	public static long getJoinStageDuration() {
		return joinStageDuration;
	}
	
	/**
	 * Change join stage
	 * @param joinStageDuration -  in milliseconds
	 */
	public static void setJoinStageDuration(long joinStageDuration) {
		Round.joinStageDuration = joinStageDuration;
		setRoundDuration();
	}
	
	/**
	 * Duration of play stage in milliseconds
	 * @return play duration in milliseconds
	 */
	public static long getPlayStageDuration() {
		return playStageDuration;
	}
	
	/**
	 * Change play stage
	 * @param playStageDuration - in milliseconds
	 */
	public static void setPlayStageDuration(long playStageDuration) {
		Round.playStageDuration = playStageDuration;
		setRoundDuration();
	}
	
	/**
	 * Duration of report stage in milliseconds
	 * @return report stage duration in milliseconds
	 */
	public static long getReportStageDuration() {
		return reportStageDuration;
	}
	
	/**
	 * Change report stage
	 * @param reportStageDuration - in milliseconds
	 */
	public static void setReportStageDuration(long reportStageDuration) {
		Round.reportStageDuration = reportStageDuration;
		setRoundDuration();
	}
	
	/**
	 * Duration of ranking stage in milliseconds
	 * @return ranking duration in milliseconds
	 */
	public static long getRankingStageSuration() {
		return rankingStageSuration;
	}
	
	/**
	 * Change ranking stage
	 * @param rankingStageDuration - in milliseconds
	 */
	public static void setRankingStageSuration(long rankingStageDuration) {
		Round.rankingStageSuration = rankingStageDuration;
		setRoundDuration();
	}
	
	/**
	 * Complete duration of a round (all stages)
	 * @return duration of a round
	 */
	public static long getRoundDuration() {
		return roundDuration;
	}
	
	/**
	 * Set round duration
	 */
	public static void setRoundDuration() {
		Round.roundDuration = joinStageDuration + playStageDuration 
				+ reportStageDuration + rankingStageSuration;
	}
	
	enum Relative { before, after };
	enum Stage { join, play, report, ranking };
	private static final Generator generator  = new Generator();
	private static final Players players = Players.getInstance();
	
	Date join = new Date();
	Date play = new Date(join.getTime() 
			+ joinStageDuration);
	Date report = new Date(play.getTime() 
			+ playStageDuration);
	Date ranking = new Date(report.getTime() 
			+ reportStageDuration);
	Date end = new Date(ranking.getTime() 
			+ rankingStageSuration);
	
	Puzzle puzzle = generator.generate();
	Map<java.lang.String,Player> roundPlayers = new HashMap<>();
	
	/**
	 * Time in milliseconds to the next play stage. 
	 * If the play stage of this round has already started then the time returned refers to the next round.
	 * @return time in milliseconds
	 */
	public long getTimetoNextPlay() {
		Date currentTime = new Date();
		if(currentTime.before(play)) {
			return play.getTime() - currentTime.getTime();
		}
		else {
			return end.getTime() - currentTime.getTime() + joinStageDuration;
		}
	}
	
	/**
	 * Register user with nick and password for this round
	 * @param nick - of registered user
	 * @param password - of registered user
	 * @return time in milliseconds for next round
	 * @throws WWWordzException - if not it join stage or user is invalid
	 */
	public long register(java.lang.String nick,
            			 java.lang.String password)
    throws WWWordzException {
		Date currentTime = new Date();
		
		if(currentTime.after(play)) 
			throw new WWWordzException("The register is not valid in this stage.");
		if(!players.verify(nick,password)) 
			throw new WWWordzException("This is not a valid user.");
			
		Player playerToJoin = players.getPlayer(nick);
		roundPlayers.put(nick, playerToJoin);
		return play.getTime() - currentTime.getTime();
	}
	
	/**
	 * Get table of this round
	 * @return table
	 * @throws WWWordzException - if not in play stage
	 */
	public Puzzle getPuzzle() throws WWWordzException {
		Date currentTime = new Date();
		
		if(currentTime.before(play)) 
			throw new WWWordzException("There is not a puzzle yet.");
		if(currentTime.after(report)) 
			throw new WWWordzException("There is not a puzzle anymore.");
		
		return puzzle;
	}
	
	/**
	 * Set number of points obtained by user in this round
	 * @param nick - of user with changed points
	 * @param points - to set to user
	 * @throws WWWordzException - if not in report stage
	 */
	public void setPoints(java.lang.String nick,
            			  int points)
    throws WWWordzException {
		Date currentTime = new Date();
		
		if(roundPlayers.containsKey(nick)==false)
			throw new WWWordzException("This is not a valid player.");	
		if(currentTime.before(report))
			throw new WWWordzException("Can't add points in this stage");
		if(currentTime.after(ranking))
			throw new WWWordzException("Can't add points in this stage");
			
		roundPlayers.get(nick).setPoints(points);
	}
	
	/**
	 * List of players in this round sorted by points
	 * @return list or ranks
	 * @throws WWWordzException - if not in ranking stage
	 */
	public List<Rank> getRanking()
    throws WWWordzException {
		Date currentTime = new Date();		
		
		if (currentTime.before(ranking)) 
			throw new WWWordzException("Can't get Ranking in this stage.");
		
		if(roundPlayers.size()==0) 
			throw new WWWordzException("There are no players to rank.");
		
		if(rankingGeral==null) {
			rankingGeral = constructRanking();
		}
		
		return rankingGeral;
	}
	
	public List<Rank> constructRanking() {
		List<Rank> ranking = new ArrayList<Rank>();
		List<Player> playersList = new ArrayList<>(roundPlayers.values());
		Collections.sort(playersList, new Comparator<Player>() {
	        public int compare(Player one, Player two) {
	            return two.getPoints() - one.getPoints();
	        }
	    });
		for(Player currentPlayer : playersList) {
			Rank aux = new Rank(currentPlayer.getNick(), currentPlayer.getPoints(), currentPlayer.getAccumulated());
			ranking.add(aux);
		}
		return ranking;
	}
	
}