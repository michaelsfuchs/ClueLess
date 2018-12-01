import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

/**
 * This java file tests Player.java
 * 
 * 1.	Test class constructor
 * 
 * @author gideon.hou
 *
 */
class PlayerTest_v1 {


	@Test
	/*
	 * This test checks the class constructor
	 */
	void constructor_test() {
		
		//Create new Player object
		Player test_player = new Player();
		
		// test the object variables in test_player
		assert(isInteger(test_player.playerID));
		
		assert(test_player.currentLoc instanceof Location);
		
		assert(test_player.isAlive);
		
		assert(test_player.isConnected);
		
		assertFalse(test_player.wasMoved);
		
		assert(test_player.hand instanceof ArrayList);
		
		
		
		System.out.println("player ID: " + test_player.playerID);
		System.out.println("Location: " + test_player.currentLoc);
		System.out.println("is Alive? " + test_player.isAlive);
		System.out.println("is Connected? " + test_player.isConnected);
		System.out.println("was moved? " + test_player.wasMoved);
		System.out.println("hand: " + Arrays.toString(test_player.hand.toArray()));
		
	}

	// This is a function to check if an object is an integer
		public boolean isInteger(Object object) {
			if(object instanceof Integer) {
				return true;
			} else {
				String string = object.toString();
				
				try {
					Integer.parseInt(string);
				} catch(Exception e) {
					return false;
				}	
			}
		  
		    return true;
		}
}
