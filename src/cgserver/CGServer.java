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
            int count = 0;    
            while(count<6 && !startGame){
                socket = server.accept();
                String uname=socket.getInetAddress().getHostName();
                String ipadd=socket.getInetAddress().getHostAddress();
                ClientHandler c=new ClientHandler(count,socket);
                clients.put(count, c);
                game.addPlayer(count);
                c.start();
                System.out.println("Client "+socket.getInetAddress().getHostName() +" accepted");
                count+=1;
                sendToAllClients("0:8:Game Started");
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

