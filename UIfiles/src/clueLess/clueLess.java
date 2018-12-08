/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clueLess;

import java.io.IOException;

/**
 *
 * @author Alisha R. Hunt
 */
public class clueLess {
    public static ClientCon client;
    public static int playerID = -1;
    public static GameBoard gb = null;
    
    public clueLess() {
        client = null;
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
        gb.setupGameBoard();
        
        // add players
        //gb.addPlayer("T-Rex");
        //gb.addPlayer("Goldie");
        
        // During a user's turn they will have multiple move options
        // Those options must be opened by the room ID
        // Below is a sample. This are currently from left to right, but will
        // be aligned with Michael's numbering system soon.
        

    }
    
    /**
     * Initializes the sockets for a new game connection.
     * @param gb
     * @return gameID
     * @throws java.io.IOException
     */
    public static int startNewGame(GameBoard gb) throws IOException{        
        System.out.println("Creating new server");
        int port = ClientCon.connectMaster();
        System.out.println("SERVER PORT IS :" + port);
        client=new ClientCon("ec2-3-17-66-140.us-east-2.compute.amazonaws.com",port,gb);
        System.out.println("Waiting to read playerID");
        playerID=Integer.parseInt(client.in.readUTF());
        System.out.println("PlayerID is : "+playerID);
        client.start();
        //Display message that gameID is port
        gb.pushTextUpdate("Your Game ID is : "+port);
        return port;
    }
    
    /**
     * This initializes the sockets for joining a pre-existing game.
     * @param gameID 
     * @param gb 
     * @throws java.io.IOException 
     */
    public static void joinGame(int gameID, GameBoard gb) throws IOException{
        System.out.println("Connecting to existing server");
        client=new ClientCon("ec2-3-17-66-140.us-east-2.compute.amazonaws.com",gameID,gb);
        System.out.println("Waiting to read playerID");
        playerID=Integer.parseInt(client.in.readUTF());
        System.out.println("PlayerID is : "+playerID);
        client.start();
    }
    
    /**
     * This function will add new player names to the lobby as each new player
     * joins that game instance.
     * @param userID 
     */
    public static void sendUserName(String userID){
        // add handling code here for networking end.
        client.writeToServer("UserID:"+userID);
    }
    
    /**
     * This function initializes the cards shown for each user.
     * @param cards 
     */
    
    public static void initCards(int suspects[],int rooms[],int weapons[]){
        String cards[] = new String[suspects.length+rooms.length+weapons.length];
        for(int i = 0 ; i<suspects.length ; i++ ){
            cards[i] = ""+suspects[i];
        }
        for(int i = 0 ; i<rooms.length ; i++ ){
            cards[i] = ""+suspects[i];
        }
        for(int i = 0 ; i<weapons.length ; i++ ){
            cards[i] = ""+weapons[i];
        }
        
        gb.setCardList(cards);
    }
    
    /**
     * This function initializes the suspect list in the detective note sheet.
     * @param suspects 
     */
    public static void initSuspectList(String suspects[]){
        gb.genSuspectList(suspects);
    }
    
    
    public static void gbaddPlayer(String playerName){
        gb.addPlayer(playerName);
    }
    
    public static void switchScreens(String screen){
        gb.switchScreens(screen);
    }
    /**
     * This function will cue the UI that it's the user's turn, giving it the
     * available move options the player has and letting the UI know if they may
     * make a suggestion in their current room (if moved by another player)
     * @param stayNSuggest
     * @param availableMoves 
     */
    public static void cueUsersTurn(boolean stayNSuggest, int availableMoves[]){
        gb.startTurn(stayNSuggest,availableMoves);
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
    
    public static void writeMsg(String msg){
        try{
            client.writeToServer(msg);
        }
        catch(Exception e){
            
        }
    }
    
    /**
     * Send a message to the server that a player ended their turn.
     */
    public static void endTurn(){
        
    }
    
    /**
     * The server should call this function when a player solves the game by 
     * making a correction accusation. This ends the game.
     * @param gb 
     */
    public static void endGameWinning(GameBoard gb){
        gb.switchScreens("Winning");
    }
    
    /**
     * The server should call this function for a player who has lost, but only
     * at the very end of the game. Players who make false accusations should
     * only receive text updates.
     * @param gb 
     */
    public static void endGameLosing(GameBoard gb){
        gb.switchScreens("Losing");
    }
}