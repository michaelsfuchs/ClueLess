import java.util.HashMap;

public class Map
{
	Location[][] board;
	HashMap<Integer,Location> cardId2Point;
	
	Map(int aSize)
	{
		board = new Location[aSize][aSize];
		cardId2Point = new HashMap<Integer,Location>();
	}
	
	public void InitBoard()
	{
		int roomID = 0, hallwayID = 0;
		
		for(int y = 0; y < 5; y++)
		{
			for(int x = 0; x < 5; x++)
			{				
				// Clear occupancy
				board[x][y].occupancy = 0;
				
				// Make valid connections to the other locations
				// Valid left
				if(x>0 && y%2==0)
				{
					board[x][y].connections.add(board[x-1][y]);
				}
				// Valid right
				if(x<4 && y%2==0)
				{
					board[x][y].connections.add(board[x+1][y]);
				}
				// Valid Up
				if(y>0 && x%2==0)
				{
					board[x][y].connections.add(board[x][y-1]);
				}
				// Valid Down
				if(y<4 && x%2==0)
				{
					board[x][y].connections.add(board[x][y+1]);
				}
				
				// Set Room type and IDs
				if(x%2==0 && y%2==0)
				{
					board[x][y].type = Location.Type.ROOM;
					board[x][y].locId = roomID++;
					cardId2Point.put(board[x][y].locId, board[x][y]);
				}
				else if(x%2==0 || y%2==0)
				{
					board[x][y].type = Location.Type.HALLWAY;
					board[x][y].locId = hallwayID++;
				}
				else
				{
					// Not a real location
				}
			}
		}
		
		// Add the secret passages
		board[0][0].connections.add(board[4][4]);
		board[4][4].connections.add(board[0][0]);
		board[4][0].connections.add(board[0][4]);
		board[0][4].connections.add(board[4][0]);
	}
	
}