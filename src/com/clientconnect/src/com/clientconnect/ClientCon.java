
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
    private Socket socket            = null; 
    public DataInputStream  input   = null; 
    public DataOutputStream out     = null; 
    private String address = "" ;
    private int port = 0;
    // constructor to put ip address and port 
    public ClientCon(String address, int port) 
    { 
        // establish a connection 
        try
        { 
             this.address=address;
             this.port=port;
        } 
        catch(Exception u) 
        { 
            u.printStackTrace();
        }
        
    } 
  
    public void run()
    {
        try{
            socket = new Socket(address, port); 
            System.out.println("Connection to server successfull"); 
            // takes input from terminal 
            input  = new DataInputStream(socket.getInputStream()); 
            // sends output to the socket 
            out    = new DataOutputStream(socket.getOutputStream());
        }
        catch(Exception e){
            System.out.println("Connection to server failed");
        }
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
    
    public void readFromServer()
    {
        String line;
        //while (!line.contains(":R")) 
        //{ 
            try
            { 
                line=input.readUTF();
                System.out.println(line);
                
            } 
            catch(IOException i) 
            { 
                System.out.println(i); 
            } 
        //}
    }
    
    public void stopClient(){
        try
        { 
            input.close(); 
            out.close(); 
            socket.close(); 
        } 
        catch(IOException i) 
        { 
            System.out.println(i); 
        } 
    }
} 