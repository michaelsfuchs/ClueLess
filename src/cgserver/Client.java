/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cgserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 *
 * @author nakulkar
 */
public class Client extends Thread{
   private String userName;
   private String ipAddress;
   private java.net.Socket socket = null;
   public DataInputStream in=null;
   public DataOutputStream out=null;
   public Boolean isloggedin=true;
   
   public Client (String userName, String ipAddress, java.net.Socket socket) throws IOException
   {
      this.userName = userName;
      this.ipAddress = ipAddress;
      this.socket = socket;
      this.in=new DataInputStream(socket.getInputStream());
      this.out=new DataOutputStream(socket.getOutputStream());
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
        String received; 
        while (true)  
        { 
            try
            { 
                // receive the string 
                received = in.readUTF(); 
                  
                System.out.println(received); 
                  
                if(received.equals("logout")){ 
                    this.isloggedin=false; 
                    this.socket.close(); 
                    break; 
                } 
                  
                // break the string into message and recipient part 
                StringTokenizer st = new StringTokenizer(received, "#"); 
                String MsgToSend = st.nextToken(); 
                String recipient = st.nextToken(); 
  
                // search for the recipient in the connected devices list. 
                // ar is the vector storing client of active users 
                for (Client mc : CGServer.clients.values())  
                { 
                    // if the recipient is found, write on its 
                    // output stream 
                    if (mc.userName.equals(recipient))  
                    { 
                        CGServer.processRequest(MsgToSend);
                        mc.out.writeUTF(MsgToSend); 
                        break; 
                    } 
                } 
            } catch (IOException e) { 
                  
                e.printStackTrace(); 
            } 
              
        } 
   }
}
