import static org.junit.Assert.*;

import org.junit.Test;

public class MapTest {
	
	//conformance test
		@Test
		public void constructor_test() {
			// new Map object of size different NxN sizes
			
			//Map test_map0 = new Map(0);
			//Map test_map1 = new Map(1);
			//Map test_map2 = new Map(2);
			//Map test_map3 = new Map(3);
			//Map test_map4 = new Map(4);
			
			Map test_map5 = new Map(5);
			
			Map test_map6 = new Map(6);
			Map test_map7 = new Map(7);
			
			Map test_map100 = new Map(100);
		}

		// fault test
		@Test
		public void constructor_test_fault() {
			
			// instantiate map objects that will throw exceptions
			try {
				Map test_map0 = new Map(0);
				Map test_map1 = new Map(1);
				Map test_map2 = new Map(2);
				Map test_map3 = new Map(3);
			} catch (java.lang.ArrayIndexOutOfBoundsException e) {
				assert(false);
			}
			assert(true);
		}
		
		// Test the output of initBoard. initBoard is called by the constructor\
		// Test if there are the correct number of rooms and hallways
		@Test
		public void initBoard_num_rooms_test() {
			// new map object
			Map test_map = new Map(5);
			
			// check if each room and hallway is initialized properly
			// assert there are 9 rooms and 12 hallways
			int actual_num_rooms = 9, num_rooms = 0, actual_num_halls = 12, num_halls = 0;
			for(Location location : test_map.locId2Point.values()) {
				if (location.type == Location.Type.HALLWAY) num_halls++;
				else if (location.type == Location.Type.ROOM) num_rooms++;
			}
			
			assertEquals(num_halls, actual_num_halls);
			assertEquals(num_rooms, actual_num_rooms);
		}
		
		// Test if all connections are valid
		@Test
		public void initBoard_valid_connections() {
			// new map object
			Map test_map = new Map(5);
			
			// Some print statements to show I'm not bugging out
			for(Location location : test_map.locId2Point.values()) {
				if (location.type == Location.Type.HALLWAY) System.out.println("Hallway: " + location.connections.toString());
				else if (location.type == Location.Type.ROOM) System.out.println("Room: " + location.connections.toString());;
			}
			
			/*
			for (int y = 0; y < 5; y++) {
				for (int x = 0; x < 5; x++) {
					System.out.println("board[" + x + "][" + y + "]:" + test_map.board[x][y].connections.size());
				}
			}
			*/
			
			// check if each room and hallway is initialized properly
			// assert all location objects have valid connections
			for(Location location : test_map.locId2Point.values()) {
				if (location.type == Location.Type.HALLWAY) {
					for(Location connection : location.connections) {
						assertNotNull(connection);
					}
				}
				else if (location.type == Location.Type.ROOM) {
					for(Location connection : location.connections) {
						assertNotNull(connection);
					}
				}
			}
		}
}
