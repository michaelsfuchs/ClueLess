/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cgserver;

import game.*;
import java.util.ArrayList;
/**
 *
 * @author nakulkar
 */
import java.net.*; 
import java.util.HashMap;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;
  
public class CGServer extends Thread
        
{ 
    //initialize socket and input stream 
    private Socket          socket   = null; 
    private ServerSocket    server   = null; 
    private DataInputStream in       =  null; 
    private DataOutputStream out     = null;
    private int port = 0;
    public static HashMap<Integer,ClientHandler> clients=new HashMap<Integer,ClientHandler>();
    public static boolean startGame = false;
    
    Game game = new Game();
    
    // constructor with port 
    public CGServer(int port) 
    { 
        try{
            this.port=port; 
            server = new ServerSocket(port); 
            System.out.println("Server started"); 
            System.out.println("Waiting for a client ..."); 
            
        }
        catch(Exception e){
            
        }
    } 
    
    @Override
    public void run(){
    	try{
            System.out.println("Started server thread");
            int count = 0;    
            while(count<6 && startGame == false){
                System.out.println("------------------------------------------");
                socket = server.accept();
                System.out.println("Accepted a client");
                ClientHandler c=new ClientHandler(count,socket,this);
                
                System.out.println("Created clienthandler object");
                clients.put(count, c);
                System.out.println("Added client to clients list");
                
                c.out.writeUTF(""+(count));
                
                System.out.println("Calling client handler thread");
                c.start();
                
                System.out.println("Client "+socket.getInetAddress().getHostName() +" accepted");
                count+=1;
                
                System.out.println("Added player to game");
                //game.addPlayer(count);
                sendToAllClients("M:0:Game Started");    
            }
            
            game.runFirstTurn();
            game.runGame();
        }
        catch(Exception e){
            
        }
    }
    public static void sendToOneClient (int playerID,String msg) throws IOException
    {
        ClientHandler c = (ClientHandler)clients.get(playerID);
        // Sending the response back to the client.
        // Note: Ideally you want all these in a try/catch/finally block
        c.out.writeUTF(msg);
    }
    
    public static void sendToAllClients(String msg) throws IOException
    {
        for(ClientHandler c : clients.values()){
            c.writeMsg(msg);
        }
    }
} 

