import java.util.ArrayList;


public class Player
{
	int playerID;
	Location currentLoc;
	boolean isAlive;
	boolean isConnected;
	boolean wasMoved;
	ArrayList<Card> hand;
	
	Player(int aPlayerId)
	{
		isAlive = false;
		isConnected = false;
		wasMoved = false;
		hand = new ArrayList<Card>();
	}
};
