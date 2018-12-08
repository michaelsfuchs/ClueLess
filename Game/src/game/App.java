package game;

import cgserver.*;

public class App
{
	public static void main(String[] args)
	{
		CGServer server = new CGServer(12345);
		server.run();
		
		
	}

}