package game;
import java.util.HashMap;

public class Map
{
	Location[][] board;
	HashMap<Integer,Location> locId2Point;
	
	Map(int aSize)
	{
		board = new Location[aSize][aSize];
		locId2Point = new HashMap<Integer,Location>(21);
		
		InitBoard();
	}
	
	public void InitBoard()
	{
		int roomID = 0, hallID = 9;
		for(int y = 0; y < 5; y++)
		{
			for(int x = 0; x < 5; x++)
			{	
				board[x][y] = new Location();
			}
		}
		for(int y = 0; y < 5; y++)
		{
			for(int x = 0; x < 5; x++)
			{				
				// These are the rooms
				if(x%2==0 && y%2==0)
				{
					board[x][y].locId = roomID;
					board[x][y].type = Location.Type.ROOM;
					locId2Point.put(roomID, board[x][y]);
					roomID++;
				}
				else if(x%2==0 || y%2==0)
				{
					board[x][y].locId = hallID;
					board[x][y].type = Location.Type.HALLWAY;
					locId2Point.put(hallID, board[x][y]);
					hallID++;
				}
				else
				{
					// Not a real location
					continue;
				}
				
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
			}
		}
		
		// Add the secret passages
		board[0][0].connections.add(board[4][4]);
		board[4][4].connections.add(board[0][0]);
		board[4][0].connections.add(board[0][4]);
		board[0][4].connections.add(board[4][0]);
	}
	
}