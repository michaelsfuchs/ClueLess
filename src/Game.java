import java.util.*;
import java.io.*;


enum LocType
{
	HALLWAY,
	ROOM,
}

class Location
{
	int locID;
	int x;
	int y;
	LocType type;
	ArrayList<Player> occupancy;
	ArrayList<Location> connections;
};

class Weapon
{
	int weaponID;
	Location currentLoc;
};

class Player
{
	int playerID;
	Location currentLoc;
	boolean isAlive;
	boolean wasMoved;
	ArrayList<Deck.Card> hand;
};

class TurnOptions
{
	ArrayList<Deck.Card> availableMoves;
}

public class Game
{
	int numPlayers = 6;
	int numRooms = 9;
	int numWeapons = 6;
	
	int currentPlayer = 0;
	int numLivePlayers = 0;
	
	Player[] players = new Player[numPlayers];
	Weapon[] weapons = new Weapon[numWeapons];
	Location[][] board = new Location[5][5];
	
	Deck.Card[] caseFile = new Deck.Card[3];
	
	boolean isGameRunning = true;
	
	public Game()
	{
		
	}
	
	public void InitBoard()
	{
		int roomID = 0, hallwayID = 0;
		for(int y = 0; y < 5; y++)
		{
			for(int x = 0; x < 5; x++)
			{
				// Clear occupancy
				board[x][y].occupancy.clear();
				
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
				
				// Add the secret passages
				board[0][0].connections.add(board[4][4]);
				board[4][4].connections.add(board[0][0]);
				board[4][0].connections.add(board[0][4]);
				board[0][4].connections.add(board[4][0]);
				
				// Set Room type and IDs
				if(x%2==0 && y%2==0)
				{
					board[x][y].type = LocType.ROOM;
					board[x][y].locID = roomID++;
				}
				else if(x%2==0 || y%2==0)
				{
					board[x][y].type = LocType.HALLWAY;
					board[x][y].locID = hallwayID++;
				}
				else
				{
					// Not a real location
				}
			}
		}
		
		// Init weapon IDs, can start them randomly if you want?
		for(int i = 0; i < numWeapons; i++)
		{
			weapons[i].weaponID = i;
		}
		
		// Set all players as "dead" before they get added
		for(Player p : players)
		{
			p.isAlive = false;
		}
	}

	public void addPlayer()
	{
	
	}
	
	public void runFirstTurn()
	{
		// Live players must move automatically to the hallway next to their home square
		Location[] homeLocations = new Location[6];
		homeLocations[0] = board[0][3];
		homeLocations[1] = board[1][1];
		homeLocations[2] = board[1][4];
		homeLocations[3] = board[3][0];
		homeLocations[4] = board[4][1];
		homeLocations[5] = board[4][3];
		
		for(int currPlayer = 0; currPlayer < numLivePlayers; currPlayer++)
		{
			//sendMsg(currentPlayer, "Your first turn must be to your home square");
			players[currPlayer].currentLoc = homeLocations[currPlayer];
			
			// Its a real player
			players[currPlayer].isAlive = true;
		}
		
		// Deal hands to live players
		Deck deck = new Deck(numPlayers, numRooms, numWeapons);
		caseFile = deck.caseFile;
		
		for(int currCard = 0, currPlayer = 0; 
				currCard < deck.deck.size(); 
				currCard++, currPlayer = (currPlayer + 1) % numLivePlayers)
		{
			players[currPlayer].hand.add(deck.deck.get(currCard));
		}
		
		//SendTurnUpdates;
	}
	
	public void onReceiveAccusation(int aPlayerID, Deck.Card[] aAccusation)
	{
		if( aAccusation[0].equals(caseFile[0]) &&
			aAccusation[1].equals(caseFile[1]) &&
			aAccusation[2].equals(caseFile[2]))
		{
			// Announce player winner
		}
		else
		{
			//Announce loser
		}
	}
	
	public void onReceiveSuggestion(int aPlayerID, Deck.Card[] aSuggestion)
	{
		// Move player and weapon to that room
		Location loc = board[aSuggestion[1].cardID % board.length][aSuggestion[1].cardID / board.length];
		
		weapons[aSuggestion[2].cardID].currentLoc = loc;
		
		Player p = players[aSuggestion[0].cardID];
		p.currentLoc = loc;
		if(!loc.occupancy.contains(p))
		{
			loc.occupancy.add(p);
		}
		
		for(int i = aPlayerID, count = 0; count < numPlayers; i=(i+1)%numPlayers, count++)
		{
			// Skip plyer who made suggestion
			if(players[i].playerID != aPlayerID)
			{
				// Find matches in that players hand if they exist
				ArrayList<Deck.Card> matches = new ArrayList<Deck.Card>(3);
				for(int j = 0; j < 3; j++)
				{
					if(players[i].hand.contains(aSuggestion[j]))
					{
						matches.add(aSuggestion[i]);
					}		
				}
				
				if(!matches.isEmpty())
				{
					// Enable player to choose one, then send it, and end loop
					break;
				}
			}
		}
	}
	
	public void runGame()
	{
		currentPlayer = 0;
		
		while(isGameRunning)
		{
			// current player
			Player p = players[currentPlayer];
			if(!p.isAlive)
			{
				continue;
			}
			
			// Get available moves
			Location l = p.currentLoc;
			ArrayList<Location> availableMoves = new ArrayList<Location>(4);
			for(Location nextMove : l.connections)
			{
				if(nextMove.occupancy.isEmpty() ||  nextMove.type == LocType.ROOM)
				{
					// Add location to message
					availableMoves.add(nextMove);
				}
			}
			// Show available moves (graphics, text list, etc)
			
			if(availableMoves.isEmpty())
			{
				if(p.wasMoved)
				{
					// Enable option to make suggestion for that room
				}
				else
				{
					// Sorry, no moves for you
				}
			}
			
			// Maybe add timeout if they dont do anything?		
			
		}
	}
};