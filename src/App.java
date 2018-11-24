
public class App
{
	public static void main(String[] args)
	{
		Game game = new Game();
				
		game.addPlayer(0);
		game.addPlayer(1);
		game.addPlayer(2);
				
		game.runFirstTurn();
		
//		System.out.println("Ran First Turn");
//
//		for(Player p : game.players)
//		{
//			for(Deck.Card c : p.hand)
//			{
//				System.out.print(c.cardID + "," + c.type + " : ");
//			}
//			System.out.println();
//		}
		
		game.runGame();
	
	}

}