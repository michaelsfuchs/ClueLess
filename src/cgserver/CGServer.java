/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cgserver;

import game.*;

/**
 *
 * @author nakulkar
 */
import java.net.*; 
import java.io.*; 
import java.util.HashMap;
import java.util.Map;
  
public class CGServer extends Thread
        
{ 
    //initialize socket and input stream 
    private Socket          socket   = null; 
    private ServerSocket    server   = null; 
    private DataInputStream in       =  null; 
    private DataOutputStream out     = null;
    private int port = 0;
    //public static HashMap<String,Client> clients=new HashMap<String,Client>();
    public static HashMap<Integer,Client> clients=new HashMap<Integer,Client>();

    Game game = new Game();
    static boolean startGame = false;
    
    // constructor with port 
    public CGServer(int port) 
    {
        try{
            this.port=port; 
            server = new ServerSocket(port); 
            System.out.println("Server started"); 
            System.out.println("Waiting for a client ..."); 
            int count = 0;    
            while(count<6 || (count >=3 && startGame)){
                socket = server.accept();
                String uname=new String(socket.getInetAddress().getHostName());
                String ipadd=new String(socket.getInetAddress().getHostAddress());
                Client c=new Client(uname,ipadd,socket);
                //clients.put(uname+":"+ipadd, c);
                clients.put(count, c);
                c.out.writeUTF("You are Player:" + count);
                c.start();
                
                game.addPlayer(count);
                
                System.out.println("Client "+socket.getInetAddress().getHostName() +" accepted");
                count+=1;
            }
        }
        catch(Exception e){
            
        }
    } 
    
    public static String processRequest(String inmsg) throws IOException {
        String ret="Message type does not match any type";
        if(inmsg.contains("Acc")){
            //Accusation logic here
            ret=new String("Accusation does not match. You Lose");
        }
        if(inmsg.contains("Sugg")){
            //Accusation logic here
            ret=new String("Accusation does not match. You Lose");
        }
        if(inmsg.contains("Acc1")){
            //Accusation logic here
            ret=new String("Accusation does not match. You Lose");
        }
        if(inmsg.contains("Acc2")){
            //Accusation logic here
            ret=new String("Accusation does not match. You Lose");
        }
        return ret;
    }
  
    public static void sendToOneClient (String userName, String ipAddress,String msg) throws IOException
    {
        Client c = (Client)clients.get(userName+":"+ipAddress);
        // Sending the response back to the client.
        // Note: Ideally you want all these in a try/catch/finally block
        c.out.writeUTF(msg);
    }
    
    public static void sendToAllClients(String msg) throws IOException
    {
        for(Client c : clients.values()){
        	
            c.writeMsg(msg);
        }
    }
    
    public void run()
    {         
        game.runFirstTurn();
        game.runGame();
    } 
} 
