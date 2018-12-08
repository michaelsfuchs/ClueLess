package cgserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends Thread{
   private String userName;
   private Socket socket = null;
   public DataInputStream in=null;
   public DataOutputStream out=null;
   public Boolean isloggedin=true;
   public CGServer server;
   public ArrayList<String> newMessages = new ArrayList<String>();
   public int playerID;
   
   public ClientHandler (int playerID, java.net.Socket socket, CGServer server) throws IOException
   {
      this.playerID = playerID; 
      this.userName = userName;
      this.socket = socket;
      this.in=new DataInputStream(socket.getInputStream());
      this.out=new DataOutputStream(socket.getOutputStream());
      this.server=server;
   }
    public java.net.Socket getSocket()
   {
       return this.socket;
   }
   public void writeMsg(String msg) throws IOException{
       out.writeUTF(msg);
   }
   
   @Override
   public void run(){
        String received=""; 
        while (!received.equals("LOGOUT"))  
        { 
            try
            { 
                // receive the string 
                received = in.readUTF(); 
                  
                System.out.println(received); 
                  
                if(received.equals("Start Game") && CGServer.startGame != true){ 
                    System.out.println("start hame message received"); 
                	CGServer.startGame = true; 
                } 
                else if(received.contains("UserID:")){ 
                    String username = received.split(":")[1];
                    server.game.addPlayer(playerID,username); 
                }
                else{
                    newMessages.add(received);
        			System.out.println(newMessages.isEmpty());
                }
            } catch (SocketException e) {
                System.out.println("Client : "+playerID+" has disconnected");
                try {
                    in.close();
                    out.close();
                    socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            } 
            catch(IOException i){
                
            }
              
        } 
   }
} 