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
    public static String serverIP;
    
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
    
        serverIP = args[0]; //args[0];
        // Initialize the UI
        gb = new GameBoard();
        gb.setVisible(true);
       
        // May switch between screens by sending a string as appropriate.
        // strings are "Welcome", "Lobby", "Gameboard", "Winning", and "Losing"
        // Note that a false accusation is a message, not a losing screen
        gb.switchScreens("Welcome");
                
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
        int port = ClientCon.connectMaster(serverIP);
        System.out.println("SERVER PORT IS :" + port);
        //client=new ClientCon("ec2-3-17-66-140.us-east-2.compute.amazonaws.com",port,gb);
        /*client=new ClientCon("localhost",port,gb);
        System.out.println("Waiting to read playerID");
        playerID=Integer.parseInt(client.in.readUTF());
        System.out.println("PlayerID is : "+playerID);
        client.start(); */
        int gameID = joinGame(port,gb);
        return gameID;
        //Display message that gameID is port
    }
    
    /**
     * This initializes the sockets for joining a pre-existing game.
     * @param gameID 
     * @param gb 
     * @throws java.io.IOException 
     */
    public static int joinGame(int gameID, GameBoard gb) throws IOException{
        System.out.println("Connecting to server");
        //client=new ClientCon("ec2-3-17-66-140.us-east-2.compute.amazonaws.com",gameID,gb);
        client=new ClientCon(serverIP,gameID,gb);
        System.out.println("Waiting to read playerID");
        playerID=Integer.parseInt(client.in.readUTF());
        System.out.println("PlayerID is : "+playerID);
        client.start();
        gb.pushTextUpdate("Your Game ID is : "+gameID);
        return gameID;
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
     * This function initializes the cards shown for each player.
     * @param roomType
     * @param roomID 
     */
    public static void initCards(int cardType[], int cardID[]){
        gb.setCardList(cardType,cardID);
    }
    
    /**
     * This function initializes the suspect list in the detective note sheet.
     * @param suspects 
     */
    public static void initSuspectList(String suspects[]){
        gb.genSuspectList(suspects);
    }
    
    /**
     * Add player to the lobby list.
     * @param playerName 
     */
    public static void addPlayer(String playerName){
        gb.addPlayer(playerName);
    }
    
    /**
     * Switch the screen shown in the UI.
     * @param screen 
     */
    public static void switchScreens(String screen){
        gb.switchScreens(screen);
    }
    
    /**
     * This function is used to initialize the gameboard.
     */
    public static void startGame(){
        clueLess.initSuspectList(gb.users);
        gb.setupGameBoard();
        gb.switchScreens("GameBoard");
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
    public static void playerMakesSuggestion(int weapon,int room,int suspect){
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
    public static void playerMakesAccusation(int weapon,int room,int suspect){
        try{
           String accusation = playerID+":5:0:"+suspect+":1:"+room+":2:"+weapon;
           client.writeToServer(accusation);
        }
        catch(Exception e){
            
        }
    }
    
    /**
     * This function is called to ask a player to disprove a suggestion.
     * @param cardType
     * @param cardID 
     */
    public static void disprovePlayerSuggestion(int cardType[],int cardID[]){
        gb.disproveSuggestion(cardType, cardID);
    }
    
    /**
     * The function sends the user's card choice back to the server.
     * @param CardType
     * @param CardID 
     */
    public static void showCard(int CardType, int CardID){
        // insert code to send message here.
        try{
            String showCard = playerID+":6:"+CardType+":"+CardID;
            client.writeToServer(showCard);
        }
        catch(Exception e){
            
        }
    }

    
    /**
     * Called when a player is forced to reveal a card to disprove a suggestion.
     * @param cardTypeToReveal
     * @param cardNumber
     */
    public static void playerCardReveal(int cardTypeToReveal, int cardNumber){
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
        client.writeToServer(playerID+":9");
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