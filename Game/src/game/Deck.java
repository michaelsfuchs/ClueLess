package game;
import java.util.*;

public class Deck
{
	ArrayList<Card> deck = new ArrayList<Card>();
	Card[] caseFile = new Card[3];
	
	// make 3 decks of each type, shuffle them, take top card as case file
	// add them together and re-shuffle
	Deck(int aNumPlayers, int aNumRooms, int aNumWeapons)
	{		
		ArrayList<Card> playerDeck = new ArrayList<Card>(aNumPlayers);
		for(int p = 0; p < aNumPlayers; p++)
		{
			playerDeck.add(new Card(0, p));
		}
		Collections.shuffle(playerDeck);
		caseFile[0] = playerDeck.get(0);
		playerDeck.remove(0);
		
		ArrayList<Card> roomDeck = new ArrayList<Card>(aNumRooms);
		for(int r = 0; r < aNumRooms; r++)
		{
			roomDeck.add(new Card(1, r));
		}
		Collections.shuffle(roomDeck);
		caseFile[1] = roomDeck.get(0);
		roomDeck.remove(0);
		
		ArrayList<Card> weaponDeck = new ArrayList<Card>(aNumWeapons);
		for(int w = 0; w < aNumWeapons; w++)
		{
			weaponDeck.add(new Card(2, w));
		}
		Collections.shuffle(weaponDeck);
		caseFile[2] = weaponDeck.get(0);
		weaponDeck.remove(0);
		
		deck.ensureCapacity(aNumPlayers + aNumRooms + aNumWeapons);
		deck.addAll(playerDeck);
		deck.addAll(roomDeck);
		deck.addAll(weaponDeck);
		Collections.shuffle(deck);
	}

	
	
	
}

