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
    public int port = 0;
    public HashMap<Integer,ClientHandler> clients=new HashMap<Integer,ClientHandler>();
    public boolean startGame = false;
    public int count;
    public Boolean endGame = true;
    Game game = new Game(this);
    
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
            count = 0;    
            server.setSoTimeout(1000);
            while(count<6 && startGame == false){
                try
                {
                  if(endGame == true){
                      endServer();
                      break;
                  }  
                  socket = server.accept();
                  System.out.println("------------------------------------------");
                  System.out.println("Accepted a client on : "+port);
                  ClientHandler c=new ClientHandler(count,socket,this);

                  System.out.println("Created clienthandler object");
                  clients.put(count, c);
                  System.out.println("Added client to clients list");

                  c.out.writeUTF(""+(count));

                  System.out.println("Calling client handler thread");
                  c.start();

                  System.out.println("Client "+socket.getInetAddress().getHostName() +" accepted");
                  count+=1;
                }
                catch(Exception e)
                {
                  // no available socket yet, keep polling
                }
            }
            
            if(endGame == false){
                sendToAllClients("M:0:Game Started");    
                game.runFirstTurn();
                game.runGame();
            }
        }
        catch(Exception e){
            
        }
    }
    public void sendToOneClient (int playerID,String msg) throws IOException
    {
        ClientHandler c = (ClientHandler)clients.get(playerID);
        // Sending the response back to the client.
        // Note: Ideally you want all these in a try/catch/finally block
        c.out.writeUTF(msg);
    }
    
    public void sendToAllClients(String msg) throws IOException
    {
        for(ClientHandler c : clients.values()){
            c.writeMsg(msg);
        }
    }
    
    public void endServer() throws IOException{
        System.out.println("Stopping Server");
        in.close();
        out.close();
        socket.close();
    }
} 

