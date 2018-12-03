/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cgserver;

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
    public static String newMsg = null;
    public static Boolean msgrecd = false; 
    public static HashMap<String,ClientHandler> clients=new HashMap<String,ClientHandler>();
    
    public static String processRequest(String inmsg) throws IOException {
        String ret="Message type does not match any type";
        if(inmsg.contains("Acc")){
            //Accusation logic here
            ret="Accusation does not match. You Lose";
        }
        if(inmsg.contains("Sugg")){
            //Accusation logic here
            ret="Accusation does not match. You Lose";
        }
        if(inmsg.contains("Acc1")){
            //Accusation logic here
            ret="Accusation does not match. You Lose";
        }
        if(inmsg.contains("Acc2")){
            //Accusation logic here
            ret="Accusation does not match. You Lose";
        }
        return ret;
    }
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
            while(count<6){
                socket = server.accept();
                String uname=socket.getInetAddress().getHostName();
                String ipadd=socket.getInetAddress().getHostAddress();
                ClientHandler c=new ClientHandler(uname,ipadd,socket);
                clients.put(uname+":"+ipadd, c);
                c.start();
                System.out.println("Client "+socket.getInetAddress().getHostName() +" accepted");
                count+=1;
            }
        }
        catch(Exception e){
            
        }
    }
    public void sendToOneClient (String userName, String ipAddress,String msg) throws IOException
    {
        ClientHandler c = (ClientHandler)clients.get(userName+":"+ipAddress);
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
    
    public static void main(String args[]) 
    { 
        CGServer server = new CGServer(Integer.parseInt("5000")); 
        //server.runSV();
    } 
} 

class ClientHandler extends Thread{
   private String userName;
   private String ipAddress;
   private java.net.Socket socket = null;
   public DataInputStream in=null;
   public DataOutputStream out=null;
   public Boolean isloggedin=true;
   
   public ClientHandler (String userName, String ipAddress, java.net.Socket socket) throws IOException
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
        String received=""; 
        while (!received.equals("LOGOUT"))  
        { 
            try
            { 
                // receive the string 
                received = new String(in.readUTF()); 
                  
                System.out.println(received); 
                  
                if(received.equals("logout")){ 
                    this.isloggedin=false; 
                    this.socket.close(); 
                    break; 
                } 
                  
                // break the string into message and recipient part 
                StringTokenizer st = new StringTokenizer(received, "#"); 
                String recipient = st.nextToken(); 
                String MsgToSend = st.nextToken(); 
                
                System.out.println("RECIPIENT IS :"+recipient);
                System.out.println("MSG IS:"+MsgToSend);
                // search for the recipient in the connected devices list. 
                // ar is the vector storing client of active users 
                //for (Client mc : CGServer.clients.values())  
               //{ 
                    // if the recipient is found, write on its 
                    // output stream 
                    //if (mc.userName.equals(recipient))  
                    //{ 
                        //System.out.println("USERNAME IS:"+ mc.userName);
                        //CGServer.processRequest(MsgToSend);
                        //mc.out.writeUTF(CGServer.processRequest(MsgToSend)); 
                        //break;
                        CGServer.msgrecd=true;
                        CGServer.newMsg=MsgToSend;
                    //} 
               // } 
            } catch (IOException e) { 
                  
                e.printStackTrace(); 
            } 
              
        } 
   }
}
