import java.util.*;
import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;


public class Game
{
	static BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));	
	static Scanner stdin = new Scanner(System.in);
	
	int numPlayers = 6;
	int numRooms = 9;
	int numWeapons = 6;
	
	int currentPlayer = 0;
	int numLivePlayers = 0;
	
	Player[] players = new Player[numPlayers];
	Weapon[] weapons = new Weapon[numWeapons];
	Map map = new Map(5);
	
	Card[] caseFile = new Card[3];
	
	boolean isGameRunning = true;
	
	public Game()
	{
		for(int p = 0; p < numPlayers; p++)
		{
			players[p] = new Player();

		}
		for(int w = 0; w < numWeapons; w++)
		{
			weapons[w] = new Weapon();

		}
		for(int y = 0; y < 5; y++)
		{
			for(int x = 0; x < 5; x++)
			{	
				map.board[x][y] = new Location();
			}
		}
		
		map.InitBoard();
		
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
	
	public void addPlayer(int aPlayerId)
	{
		if(numLivePlayers == 6)
		{
			//System.out.println("Game Full");
		}
		else if(players[aPlayerId].isAlive)
		{
			//System.out.println("Player Taken");
		}
		else
		{
			players[aPlayerId] = new Player();
			players[aPlayerId].playerID = aPlayerId;
			players[aPlayerId].isAlive = true;
			players[aPlayerId].isConnected = true;
			players[aPlayerId].wasMoved = false;
			numLivePlayers++;
			
			//System.out.println("Player " + aPlayerId + " Added");
		}
		
		// Announce that a new player joined
	}
	
	public void runFirstTurn()
	{
		// Live players must move automatically to the hallway next to their home square
		Location[] homeLocations = new Location[6];
		homeLocations[0] = map.board[0][3];
		homeLocations[1] = map.board[1][0];
		homeLocations[2] = map.board[1][4];
		homeLocations[3] = map.board[3][0];
		homeLocations[4] = map.board[4][1];
		homeLocations[5] = map.board[4][3];
		
		for(int currPlayer = 0; currPlayer < numPlayers; currPlayer++)
		{
			if(players[currPlayer].isAlive)
			{
				//sendMsg(currentPlayer, "Your first turn must be to your home square");
				players[currPlayer].currentLoc = homeLocations[currPlayer];
				players[currPlayer].currentLoc.occupancy++;
			}
		}
		
		// Deal hands to live players
		Deck deck = new Deck(numPlayers, numRooms, numWeapons);
		caseFile = deck.caseFile;
		
		int currCard = 0;
		for(int currPlayer = 0; 
				currCard < deck.deck.size(); 
				currPlayer = (currPlayer + 1) % numPlayers)
		{
			if(players[currPlayer].isAlive)
			{
				players[currPlayer].hand.add(deck.deck.get(currCard++));
			}
		}
		
		//SendTurnUpdates;
		//Send all players their hands
	}
	
	public void onReceiveMove(int aPlayerId, int locId)
	{
		players[aPlayerId].currentLoc.occupancy--;
		players[aPlayerId].currentLoc = map.locId2Point.get(locId);
		players[aPlayerId].currentLoc.occupancy++;
	}
			
	public void onReceiveAccusation(int aPlayerId, Card aSuspect, Card aLocation, Card aWeapon)
	{
		if( aSuspect.equals(caseFile[0]) &&
			aLocation.equals(caseFile[1]) &&
			aWeapon.equals(caseFile[2]))
		{
			// Announce player winner
		}
		else
		{
			players[aPlayerId].isAlive = false;
			//Announce loser
		}
	}
	
	// Card[] order is Player, Room, Weapon
	public void onReceiveSuggestion(int aPlayerID, Card aSuspect, Card aLocation, Card aWeapon)
	{
		// First send message to everyone what the suggestion was		
		
		// move from old room to new room
		Player p = players[aSuspect.cardID];
		Location l = map.locId2Point.get(aLocation.cardID);
		p.currentLoc.occupancy--;
		p.currentLoc = l;
		
		weapons[aWeapon.cardID].currentLoc = l;
		
		// Enable that player's "was moved" field
		p.wasMoved = true;
		
		for(int i = aPlayerID+1, count = 0; count < numPlayers-1; i=(i+1)%numPlayers, count++)
		{
			System.out.println("Player "+i+" next : ");

			if(players[i].isAlive)
			{
				System.out.println("Player "+i+" alive");
				
				// Find matches in that players hand if they exist
				ArrayList<Card> matches = new ArrayList<Card>(3);
				if(players[i].hand.contains(aSuspect))
				{
					matches.add(aSuspect);
				}		
				if(players[i].hand.contains(aLocation))
				{
					matches.add(aLocation);
				}
				if(players[i].hand.contains(aWeapon))
				{
					matches.add(aWeapon);
				}
				
				if(!matches.isEmpty())
				{
					if(players[i].isConnected)
					{
						// Send message to player choose one
						System.out.println("Player "+i+" Choose a card from your hand");
						int response = stdin.nextInt();
					}
					else
					{
						// Send this random one since the player is dead and gone
						// and is now a "Bot"
						int randomCard = (int)Math.floor(Math.random()*matches.size());
						matches.get(randomCard);
					}
					
					// Receive card, then show it to suggestor
					// announce to everyone this player showed a card					
					break;
				}
			}
		}
	}
	
	public void runGame() throws InterruptedException
	{
		currentPlayer = 0;
		
		while(isGameRunning)
		{
			// current player
			Player p = players[currentPlayer];
			if(p.isAlive)
			{
			
				// Get available moves
				Location l = p.currentLoc;
				ArrayList<Location> availableMoves = new ArrayList<Location>(4);
				for(Location nextMove : l.connections)
				{
					if(nextMove.occupancy == 0 ||  nextMove.type == Location.Type.ROOM)
					{
						// Add location to message
						availableMoves.add(nextMove);
					}
				}
				
				if(p.wasMoved)
				{
					// Enable option to make suggestion for that room
				}
				//Send moves
				
				System.out.print("Player "+(currentPlayer+1)+" CurrentLoc: "+p.currentLoc.locId+"  Move options: ");
				System.out.print("Enable Suggestion: "+p.wasMoved+" Locations: ");
				for(Location move : availableMoves)
				{
					System.out.print(move.locId + " ");
				}
				System.out.println();
				
				// Wait for responses for a certain timeout, then process message
				//TimedBlockingReceiveProcessMessage();
				//Thread.sleep(3000);
				

				System.out.println("Choose Option");
				int response = stdin.nextInt();
				if(response == 0)
				{
					onReceiveSuggestion(currentPlayer,
							new Card(CardType.SUSPECT, 1),
							new Card(CardType.ROOM, 1),
							new Card(CardType.WEAPON, 1));
				}
				else
				{
					onReceiveMove(currentPlayer, availableMoves.get(response-1).locId);
				}
			}
			
			currentPlayer = (currentPlayer+1) % numPlayers;
		}
	}

};