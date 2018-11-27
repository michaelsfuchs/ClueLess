import java.util.ArrayList;


public class Player
{
	int playerID;
	Location currentLoc;
	boolean isAlive;
	boolean isConnected;
	boolean wasMoved;
	ArrayList<Card> hand;
	
	Player()
	{
		hand = new ArrayList<Card>();
	}
};
