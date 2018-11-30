import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

/**
 * This .java file is for testing the capabilities of the Card class
 * 
 * These test cases validate the following
 * 
 * 1.	Each Card object has an ID and type
 * 2.	Card class has a function that checks if cards are equal
 * 
 * @author gideon.hou
 *
 */
class CardTest_v1 {


	/*
	 * This test checks if Card objects can be assigned ID's and type's
	 */
	@Test
	void test_assign_id_and_types() {
		///*
		int num_cards = 100;
		// Create an array of num_cards integers that will be used as card IDs
		int[] card_IDs = new int[num_cards];
		for(int i = 0; i < card_IDs.length; i++) {
			card_IDs[i] = i;
		}
		
		// Create an array of CardTypes
		CardType[] card_types = new CardType[3];
		card_types[0] = CardType.ROOM;
		card_types[1] = CardType.SUSPECT;
		card_types[2] = CardType.WEAPON;
		
		Card[] card_array = new Card[card_IDs.length];
		// Create an array of Card objects using card_IDs and card_types
		for(int i = 0; i < card_IDs.length; i++) {
			card_array[i] = new Card(card_types[i % card_types.length], card_IDs[i]);
		}
		//*/
		
		// assert that there are num_cards cards and each card has a valid CardType
		for (int i = 0; i < card_array.length; i++) {
			assert(card_array[i].cardID == i);
			assert(card_array[i].type == CardType.ROOM || card_array[i].type == CardType.SUSPECT || card_array[i].type == CardType.WEAPON);
		}	
	}
	
	/*
	 * This test checks if cards can compare themselves to each other
	 */
	@Test
	void test_compare() {
		int num_cards = 10;
		// Create an array of num_cards integers that will be used as card IDs
		int[] card_IDs = new int[num_cards];
		for(int i = 0; i < card_IDs.length; i++) {
			card_IDs[i] = i;
		}
		
		// Create an array of CardTypes
		CardType[] card_types = new CardType[3];
		card_types[0] = CardType.ROOM;
		card_types[1] = CardType.SUSPECT;
		card_types[2] = CardType.WEAPON;
		
		// Create 2 card arrays that will be compared against each other
		Card[] card_array1 = new Card[card_IDs.length];
		// Create an array of Card objects using card_IDs and card_types
		for(int i = 0; i < card_IDs.length; i++) {
			card_array1[i] = new Card(card_types[i % card_types.length], card_IDs[i]);
		}
		
		Card[] card_array2 = new Card[card_IDs.length];
		// Create an array of Card objects using card_IDs and card_types
		for(int i = 0; i < card_IDs.length; i++) {
			card_array2[i] = new Card(card_types[i % card_types.length], card_IDs[i]);
		}
		
		// compare the two card arrays together
		// in this check, all cards should be the same
		for (int i = 0; i < num_cards; i++) {
			assert(card_array1[i].equals(card_array2[i]));
		}
		
		// create a card array that is different from card_array1 and card_array2
		int[] card_IDs_different = new int[num_cards];
		int starting_card_id = 10;
		for(int i = starting_card_id; i < starting_card_id + num_cards; i++) {
			card_IDs[i - num_cards] = i;
		}
		
		Card[] card_array3 = new Card[card_IDs.length];
		// Create an array of Card objects using card_IDs and card_types
		for(int i = 0; i < card_IDs_different.length; i++) {
			card_array2[i] = new Card(card_types[i % card_types.length], card_IDs_different[i]);
		}
		// in this check, all cards should not be the same
		for (int i = 0; i < num_cards; i++) {
			assertFalse(card_array1[i].equals(card_array3[i]));
			
		}
		
	}
}
