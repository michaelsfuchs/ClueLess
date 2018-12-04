/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clueLess;

/**
 *
 * @author Alisha R. Hunt
 */
public class clueLess {
    
    public static void clueLess() {
    
}
    /**
     * ClueLess is the main game application. It's responsible for managing hand 
     * off between the game board or UI, and the sockets communicating with the 
     * game play logic on the server
     * @param args 
     */
    public static void main(String args[]){
    

        GameBoard gb = new GameBoard();
        gb.setVisible(true);

        // The client side networking will be established here.

       /* if(jTextField1.getText().equals("")){
            System.out.println("Creating new server");
            int port = ClientCon.connectMaster();
            System.out.println("SERVER PORT IS :" + port);
            ClientCon c=new ClientCon("localhost",port);
            c.start();
        }
        else{
            System.out.println("Connecting to existing server");
            int port = Integer.parseInt(jTextField1.getText());
            ClientCon c=new ClientCon("localhost",port);
            c.start();
        }*/
       

        // This initializes the UI, please send the names of all 6 suspects
        String suspects[] = {"Col. Mustard","Prof. Plum","Mr. Green",
            "Mrs. Peacock","Miss Scarlet","Mrs. White"};
        String cards[] = {"Conservatory","Rope","Prof. Plum","Mrs. Peacock"};
        gb.setupGameBoard(suspects,cards);

        // May switch between screens by sending a string as appropriate.
        // strings are "Welcome", "Lobby", "Gameboard", "Winning", and "Losing"
        // Note that a false accusation is a message, not a losing screen
        gb.switchScreens("Welcome");
        
        // During a user's turn they will have multiple move options
        // Those options must be opened by the room ID
        // Below is a sample. This are currently from left to right, but will
        // be aligned with Michael's numbering system soon.
        int openMoves[]= {2,6,21};
        gb.openRoomSelection(openMoves[0]);
        gb.openRoomSelection(openMoves[1]);
        gb.openRoomSelection(openMoves[2]);

        

    
    
    }
    
    /**
     * Initializes the sockets for a new game connection.
     * @return gameID
     */
    public static int startNewGame(){
        System.out.println("Creating new server");
        int port = ClientCon.connectMaster();
        System.out.println("SERVER PORT IS :" + port);
        ClientCon c=new ClientCon("localhost",port);
        c.start();
        
        return port;
    }
    
    /**
     * This initializes the sockets for joining a pre-existing game.
     * @param gameID 
     */
    public static void joinGame(int gameID){
        System.out.println("Connecting to existing server");
        ClientCon c=new ClientCon("localhost",gameID);
        c.start();
    }
    
    public static void sendUserName(String userID){
        // add handling code here for networking end.
    }
}
