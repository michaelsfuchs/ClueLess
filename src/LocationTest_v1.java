import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import junit.framework.Assert;

/**
 * This .java file tests the capabilities of Location.java
 * 
 * 1.	Test the class constructor
 * 
 * @author gideon.hou
 *
 */
class LocationTest_v1 {

	/*
	 * This tests the Location.java class constructor
	 */
	@Test
	void constructor_test() {
		
		// Instantiate new Location object
		Location test_location = new Location();
		
		assertEquals(test_location.getClass(), Location.class);
		
		// check if test_location is of Class type Location
		assert(test_location instanceof Location);
		
		// check if object variables are of the correct type
		assert(isInteger(test_location.locId));
		
		//assert(test_location.type == Location.Type.HALLWAY || test_location.type == Location.Type.ROOM);
		
		assert(isInteger(test_location.occupancy));
	
		assert(test_location.connections instanceof ArrayList);
		
		System.out.println(test_location.locId);
		System.out.println(test_location.type);
		System.out.println(test_location.occupancy);
		System.out.println(Arrays.toString(test_location.connections.toArray()));
		
		
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
