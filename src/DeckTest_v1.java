import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * This .java file tests the capabilities of Deck.java
 * 
 * Deck.java has 1 card for each player, 1 card for each weapon, and 1 card for each room.
 * 
 * num_players + num_weapons + num_rooms = total_num_cards
 * 
 * This .java file checks if Deck.java can do the following:
 * 
 * 1.	Instantiate Deck objects with the following parameters
 * 		a.	3 players, 6 weapons, 9 rooms
 * 		b.	4 players, 6 weapons, 9 rooms
 * 		c.	5 players, 6 weapons, 9 rooms
 * 		d.	6 players, 6 weapons, 9 rooms
 * 
 * 2.	Card objects in the caseFile Card array do not contain Card objects found in the deck
 * 
 * @author gideon.hou
 *
 */
class DeckTest_v1 {

	/*
	 *  This test checks condition 1.
	 *  
	 *  Check if Deck.java can instantiate decks with a group sizes between 3 and 6 inclusive
	 *  
	 *  Checks if the deck sizes are correct after object instantiation
	 */
	@Test
	void test_deck_constructor() {
		// number of weapons can only be 6 in Clueless
		int num_weapons = 6;
		
		// number of rooms can only be 9 in Clueless
		int num_rooms = 9;
		
		// Create decks with different numbers of players
		Deck three_players = new Deck(3, num_rooms, num_weapons);
		Deck four_players = new Deck(4, num_rooms, num_weapons);
		Deck five_players = new Deck(5, num_rooms, num_weapons);
		Deck six_players = new Deck(6, num_rooms, num_weapons);
		
		// The following variables are the number of cards that should be in the deck (without casefile cards)
		int num_cards_three_players = 3 + num_weapons + num_rooms - 3; // number of cards that should be in a 3 player deck
		int num_cards_four_players = 4 + num_weapons + num_rooms - 3; // number of cards that should be in a 4 player deck
		int num_cards_five_players = 5 + num_weapons + num_rooms - 3; // number of cards that should be in a 5 player deck
		int num_cards_six_players = 6 + num_weapons + num_rooms - 3; // number of cards that should be in a 6 player deck
		
		// Check if the deck sizes are correct
		assertEquals(three_players.deck.size(), num_cards_three_players);
		assertEquals(four_players.deck.size(), num_cards_four_players);
		assertEquals(five_players.deck.size(), num_cards_five_players);
		assertEquals(six_players.deck.size(), num_cards_six_players);
		
	}

	/*
	 * This test checks condition 2.
	 * 
	 * Checks if the cards in the deck are not found in the caseFile Card array
	 */
	@Test
	void test_caseFile_cards() {
		// create a deck object of 6 players, 9 rooms, and 6 weapons
		Deck six_players = new Deck(6, 9, 6);
		
		// Make sure that no card in the deck are the same as the ones in the caseFile Card array
		for (Card card: six_players.deck) {
			assertFalse(card.equals(six_players.caseFile[0]));
			assertFalse(card.equals(six_players.caseFile[1]));
			assertFalse(card.equals(six_players.caseFile[2]));
		}
	}
}
