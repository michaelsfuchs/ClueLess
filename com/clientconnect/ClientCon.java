
package com.clientconnect;

/**
 *
 * @author nachiket
 */
import java.net.*; 
import java.io.*; 
  
public class ClientCon extends Thread
{ 
    // initialize socket and input output streams 
    public Socket socket           = null; 
    public DataInputStream  in   = null; 
    public DataOutputStream out     = null; 
    public String address = "" ;
    public String myUserName="";
    public String myIpAddress="";
            public int port = 0;
    // constructor to put ip address and port 
    public ClientCon(String address, int port) 
    { 
        // establish a connection 
        try
        { 
             this.address=address;
             this.port=port;
             this.myUserName=InetAddress.getLocalHost().getHostName();
             this.myIpAddress=InetAddress.getLocalHost().getHostAddress();
             socket = new Socket(address, port); 
            System.out.println("Connection to server successful"); 
            // takes input from terminal 
            in  = new DataInputStream(socket.getInputStream()); 
            // sends output to the socket 
            out    = new DataOutputStream(socket.getOutputStream());
        } 
        catch(Exception u) 
        { 
        }
        
    } 
  
    @Override
    public void run()
    {
        try{
            String received="";
            while(true){
                received=in.readUTF(); 
                //System.out.println(""+received); 
                if(!received.isEmpty() || !(received == null) || !received.equals("")){
                   out.writeUTF(processRequest(received));
                   received="";
                }
            }
            
        }
        catch(Exception e){
            System.out.println("Connection to server failed");
        }
    } 
    
    public String processRequest(String msgtoproc) throws UnknownHostException{
        String ret=myUserName+ "# No Msg ";
        if(msgtoproc.contains("Move to")){
            //call move to room function
            ret=new String("Move made");
        }
        if(msgtoproc.contains("You Lose")){
            //call display message(You Lose)
        }
        if(msgtoproc.contains("Sugg:")){
            
        }
        if(msgtoproc.contains("Game ends")){
            ret=new String(myUserName+ "# My Game Ends");
            stopClient();
            
        }
        return ret;   
    }
    
    public void writeToServer(String line)
    {
        //while (!line.contains(":W")) 
        //{ 
            try
            { 
                out.writeUTF(line); 
            } 
            catch(IOException i) 
            { 
                System.out.println(i); 
            } 
        //}
    }
    
    public void readFromServer() throws IOException
    {
        String line;
        line=in.readUTF();
        System.out.println(line);
    }
    
    public void stopClient(){
        try
        { 
            in.close(); 
            out.close(); 
            socket.close(); 
        } 
        catch(IOException i) 
        { 
            System.out.println(i); 
        } 
    }
    
    public static int connectMaster(){
        try{
            //connects to the master server
            Socket masterSocket=new Socket("localhost",5000);
            DataInputStream inp = new DataInputStream(masterSocket.getInputStream());
            DataOutputStream outp = new DataOutputStream(masterSocket.getOutputStream());
            
            //gets the port to connect to the actual server
            int p= Integer.parseInt(inp.readUTF());
            masterSocket.close();
            
            //connects to the actual server
            return p;
        }
        catch(Exception e){
            return 0;
        }
    }
} 