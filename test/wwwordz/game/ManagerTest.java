package wwwordz.game;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Collection;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wwwordz.TestData;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Rank;
import wwwordz.shared.WWWordzException;

/**
* Template for a test class on Manager 
* 
*/
@DisplayName("Manager")
public class ManagerTest extends TestData {
	
	private static final int REPEAT = 10;
	
	static final long STAGE_DURATION = 500;
	static final long SLACK = 20;
	
	Puzzle[] puzzlesCreated = new Puzzle[10];
	
	/**
	 * A manager to test
	 */
	static wwwordz.game.Manager manager;
	
	/**
	 * Set stage durations in round before any tests and get Manager instance for testing
	 */
	@BeforeAll
	public static void prepare() {
		Round.setJoinStageDuration(STAGE_DURATION);
		Round.setPlayStageDuration(STAGE_DURATION);
		Round.setReportStageDuration(STAGE_DURATION);
		Round.setRankingStageSuration(STAGE_DURATION);
		manager = Manager.getInstance();
	}
	
	/**
	 * Check if Manager is singleton
	 */
	@Test
	@DisplayName("Is singleton")
	public void testSingleton() {
		assertNotNull(manager);
		Manager copy = Manager.getInstance();
		assertEquals(manager,copy,"Multiples instances of singleton");
	}
	
	/**
	 * Test values to start a next play stage
	 * @throws InterruptedException 
	 */
	@Test
	@DisplayName("Time to next play")
	public void testGetTimeToNextPlay() throws InterruptedException {
		long time = manager.timeToNextPlay();
		assertTrue(time<=STAGE_DURATION,"Less then stage duration");
  		Thread.sleep(time-SLACK);
		
		time = manager.timeToNextPlay();
		assertTrue( time <= SLACK , "Just slack time before play");
		
		Thread.sleep(SLACK);
		
		assertTrue(manager.timeToNextPlay() > Round.getRoundDuration() - SLACK,
				"Must wait till next round");
	}
	
}
