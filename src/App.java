
public class App
{
	public static void main(String[] args)
	{
		Game game = new Game();
		
		game.addPlayer(0);
		game.addPlayer(1);
		game.addPlayer(2);

		game.runFirstTurn();
		
		//game.runGame();
	
	}

}