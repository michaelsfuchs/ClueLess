
public class App
{
	public static void main(String[] args)
	{
		Game game = new Game();
				
		game.addPlayer(0);
		game.addPlayer(1);
		game.addPlayer(2);
//		game.addPlayer(3);
//		game.addPlayer(4);
//		game.addPlayer(5);

		game.runFirstTurn();
		
		System.out.println("Ran First Turn");
//
//		for(Player p : game.players)
//		{
//			for(Deck.Card c : p.hand)
//			{
//				System.out.print(c.cardID + "," + c.type + " : ");
//			}
//			System.out.println();
//		}
		
		try
		{
			game.runGame();
		}
		catch(Exception e)
		{
			System.out.println("Error");
		}
	}

}