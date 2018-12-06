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
    public static ClientCon client;
    public static int playerID;
    public static GameBoard gb;
    
    public clueLess() {
        playerID = -1;
    }
    /**
     * ClueLess is the main game application. It's responsible for managing hand 
     * off between the game board or UI, and the sockets communicating with the 
     * game play logic on the server
     * @param args 
     */
    public static void main(String args[]){
    
        // Initialize the UI
        gb = new GameBoard();
        gb.setVisible(true);
       
        // May switch between screens by sending a string as appropriate.
        // strings are "Welcome", "Lobby", "Gameboard", "Winning", and "Losing"
        // Note that a false accusation is a message, not a losing screen
        gb.switchScreens("Welcome");
        
        // This initializes the gameboard, please send the names of all 6 suspects
        String suspects[] = {"Col. Mustard","Prof. Plum","Mr. Green",
            "Mrs. Peacock","Miss Scarlet","Mrs. White"};
        String cards[] = {"Conservatory","Rope","Prof. Plum","Mrs. Peacock"};
        gb.setupGameBoard(suspects,cards);
        
        // add players
        gb.addPlayer("T-Rex");
        gb.addPlayer("Goldie");
        
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
        client=new ClientCon("localhost",port,gb);
        client.start();
        //Display message that gameID is port
        return port;
    }
    
    /**
     * This initializes the sockets for joining a pre-existing game.
     * @param gameID 
     */
    public static void joinGame(int gameID){
        System.out.println("Connecting to existing server");
        client=new ClientCon("localhost",gameID,gb);
        client.start();
    }
    
    /**
     * This function will add new player names to the lobby as each new player
     * joins that game instance.
     * @param userID 
     */
    public static void sendUserName(String userID){
        // add handling code here for networking end.
        client.writeToServer(userID);
    }
    
    /**
     * This function will cue the UI that it's the user's turn, giving it the
     * available move options the player has and letting the UI know if they may
     * make a suggestion in their current room (if moved by another player)
     * @param stayNSuggest
     * @param availableMoves 
     */
    public static void cueUsersTurn(boolean stayNSuggest, int availableMoves[]){
        
    }
    
    /**
     * This function will update a player's location on the map and push game 
     * updates to the text field that the user has moved.
     * @param playerID
     * @param roomID 
     */
    public static void updatePlayerLocation(int playerID, int roomID){
        
    }
    
    /**
     * Send update to the network with a player's move.
     * @param roomID 
     */
    public static void playerMove(int roomID){
        try{
           String move = playerID+":3:"+roomID;
           client.writeToServer(move);
        }
        catch(Exception e){
            
        }
    }
    
    /**
     * Called when a player makes a suggestion. 
     */
    public static void playerMakesSuggestion(String weapon,String room,String suspect){
        try{
           String suggestion = playerID+":4:0:"+suspect+":1:"+room+":2:"+weapon;
           client.writeToServer(suggestion);
        }
        catch(Exception e){
            
        }
    }
    
    /**
     * Called when a player makes an accusation. Needs to return a boolean value
     * to the player UI to know if the accusation was true or not. 
     */
    public static void playerMakesAccusation(String weapon,String room,String suspect){
        try{
           String accusation = playerID+":5:0:"+suspect+":1:"+room+":2:"+weapon;
           client.writeToServer(accusation);
        }
        catch(Exception e){
            
        }
    }
    
    /**
     * Called when a player is forced to reveal a card to disprove a suggestion.
     * @param cardTypeToReveal
     * @param cardNumber
     */
    public static void playerCardReveal(String cardTypeToReveal, String cardNumber){
        try{
            String cardReveal = playerID + ":6:"+cardTypeToReveal+":"+cardNumber;
            client.writeToServer(cardReveal);
        }
        catch(Exception e){
            
        }
    }
}
