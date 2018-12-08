package game;

import java.util.*;
import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

import cgserver.*;

import java.util.StringTokenizer;


public class Game
{	
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
	CGServer CGServer;
	public Game(CGServer obj)
	{	
                CGServer=obj;
		// Init players
		for(int p = 0; p < numPlayers; p++)
		{
			players[p] = new Player(p);
		}
		
		// Init weapons
		for(int w = 0; w < numWeapons; w++)
		{
			// start all weapons in the center room which ID = 4
			weapons[w] = new Weapon(w, map.locId2Point.get(4));
		}

	}
	
	public void addPlayer(int aPlayerId,String username) throws IOException
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
			players[aPlayerId].isAlive = true;
			players[aPlayerId].isConnected = true;
			
			numLivePlayers++;			
		}
		
		// Wait for new player's message with their name;
		ClientHandler newPlayerClient = CGServer.clients.get(aPlayerId);
		
                System.out.println("Adding username of newly joined player : "+aPlayerId+":"+username);
                
               /* while(newPlayerClient.newMessages.isEmpty()){
                    System.out.println("Inside addPlayer loop");
                    String msgIn = newPlayerClient.newMessages.get(0);
                    players[aPlayerId].customName = msgIn;
                    System.out.println("New player playerID : "+aPlayerId+" named : "+msgIn);
                    newPlayerClient.newMessages.remove(0);
                    break;
                } */
                
                players[aPlayerId].customName = username;
                
		String msgOut = "M:12";
		for(int p = 0;p<numLivePlayers;p++)
		{
			msgOut = msgOut+":"+aPlayerId+":"+players[aPlayerId].customName;
		}
		CGServer.sendToAllClients(msgOut);

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
		for(int playerIdx = 0; playerIdx < numPlayers; playerIdx++)
		{
			if(players[playerIdx].isAlive)
			{
				//sendToPlayer(msg.hand);
			}
		}
	}
	
	public void onReceiveMove(int aPlayerId, int locId) throws IOException
	{
		players[aPlayerId].currentLoc.occupancy--;
		players[aPlayerId].currentLoc = map.locId2Point.get(locId);
		players[aPlayerId].currentLoc.occupancy++;
		
		CGServer.sendToAllClients("0#1:" + aPlayerId + ":" + locId);
	}
			
	public void onReceiveAccusation(int aPlayerId, Card aSuspect, Card aLocation, Card aWeapon) throws IOException
	{
		if( aSuspect.equals(caseFile[0]) &&
			aLocation.equals(caseFile[1]) &&
			aWeapon.equals(caseFile[2]))
		{
			// Announce player winner, end game
			CGServer.sendToAllClients("0#8:Player " + aPlayerId + " Has Won!");
			isGameRunning = false;
		}
		else
		{
			players[aPlayerId].isAlive = false;
			//Announce loser
			CGServer.sendToAllClients("0#8:Player " + aPlayerId + " Has Lost!");

		}
	}
	
	// Card[] order is Player, Room, Weapon
	public void onReceiveSuggestion(int aPlayerID, Card aSuspect, Card aLocation, Card aWeapon) throws IOException
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
			if(players[i].isAlive)
			{				
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
					String msgOut = i + ":6:";
							
					if(players[i].isConnected)
					{						
						// Send message to player choose one
						ClientHandler disprovingPlayer = CGServer.clients.get(i);
						String message = "0:11";
						for(Card c : matches)
						{
							message = message + ":" + c.type + ":" + c.cardID;
						}
						disprovingPlayer.out.writeUTF(message);
						
						// Wait for response
						while(disprovingPlayer.newMessages.isEmpty());
						String msg = disprovingPlayer.newMessages.get(0);
						disprovingPlayer.newMessages.remove(0);
						
						int cardType = Integer.parseInt(msg.substring(msg.length()-3));
						int cardID = Integer.parseInt(msg.substring(msg.length()-1));
						msgOut = msgOut + cardType + ":" + cardID;
					}
					else
					{
						// Choose a random one since the player is dead and gone
						// and is now a "Bot"
						int randomCard = (int)Math.floor(Math.random()*matches.size());
						Card c = matches.get(randomCard);
						msgOut = msgOut + c.type + ":" + c.cardID;
					}
					
					// Receive card, then show it to suggestor
					ClientHandler suggestingPlayer = CGServer.clients.get(aPlayerID);
					suggestingPlayer.out.writeUTF(msgOut);
					
					// announce to everyone this player showed a card
					CGServer.sendToAllClients("0#8:Player " + i + " disproved the suggestion");
					break;
				}
			}
		}
	}
	
	public void runGame()
	{
		try{
			
		currentPlayer = 0;
		
		while(isGameRunning)
		{
			// current player
			Player p = players[currentPlayer];
			if(p.isAlive)
			{
				ClientHandler currentPlayerClient = CGServer.clients.get(currentPlayer);
				String availableMovesMessage = "0:2:";
				
				if(p.wasMoved)
				{
					// Enable option to make suggestion for that room
					availableMovesMessage = availableMovesMessage + "1:";
				}
				else
				{
					// Disable option to make suggestion for that room
					availableMovesMessage = availableMovesMessage + "0";
				}
				
				// Get available moves
				Location l = p.currentLoc;
				ArrayList<Location> availableMoves = new ArrayList<Location>(4);
				for(Location nextMove : l.connections)
				{
					if(nextMove.occupancy == 0 ||  nextMove.type == Location.Type.ROOM)
					{
						// Add location to message
						availableMoves.add(nextMove);
						
						// Add it to the message
						availableMovesMessage = availableMovesMessage + ":" + nextMove.locId;
					}
				}
				
				//Send moves
				currentPlayerClient.out.writeUTF(availableMovesMessage);
				
				// Wait for responses for a certain timeout, then process message
				boolean playerTurn = true;
				while(playerTurn)
				{
					if(!currentPlayerClient.newMessages.isEmpty())
					{
//						TimedBlockingReceiveProcessMessage();
//						if(timeout)
//						{
//							p.isAlive = false;
//							p.isConnected = false;
//							break;
//						}
						
						String msgIn = currentPlayerClient.newMessages.get(0);
						
						StringTokenizer st = new StringTokenizer(msgIn, ":"); 
		                int source = Integer.parseInt(st.nextToken()); 
		                int msgId = Integer.parseInt(st.nextToken()); 

						switch(msgId)
						{
						case 3: // Move
							int newLoc = Integer.parseInt(st.nextToken());
							onReceiveMove(currentPlayer, newLoc);
							if(map.locId2Point.get(newLoc).type == Location.Type.ROOM)
							{
								// Send enable suggestion again
								String newSuggestionMessage = "0:2:1";
								currentPlayerClient.out.writeUTF(newSuggestionMessage);
							}
							break;
						case 4: // Suggestion
							Card sugg[] = new Card[3];
							for(int i=0;i<3;i++)
							{
								sugg[i] = new Card(Integer.parseInt(st.nextToken()),Integer.parseInt(st.nextToken()));
							}
							onReceiveSuggestion(source, sugg[0], sugg[1], sugg[2]);
							break;
						case 5: // Accusation
							Card acc[] = new Card[3];
							for(int i=0;i<3;i++)
							{
								acc[i] = new Card(Integer.parseInt(st.nextToken()),Integer.parseInt(st.nextToken()));
							}
							onReceiveAccusation(source, acc[0], acc[1], acc[2]);
							playerTurn = false;
							break;
						case 9: // End Turn
							playerTurn = false;
							break;
						}
						
						currentPlayerClient.newMessages.remove(0);
					}
				}
				
			}
			
			currentPlayer = (currentPlayer+1) % numPlayers;
		}
		} catch(IOException e){
		
		}
	}
};