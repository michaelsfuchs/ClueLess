
package com.clientconnect;

/**
 *
 * @author nachiket
 */
import java.net.*; 
import java.io.*; 
  
public class ClientCon
{ 
    // initialize socket and input output streams 
    private Socket socket            = null; 
    private DataInputStream  input   = null; 
    private DataOutputStream out     = null; 
  
    // constructor to put ip address and port 
    public ClientCon(String address, int port) 
    { 
        // establish a connection 
        try
        { 
            socket = new Socket(address, port); 
            System.out.println("Connected"); 
            // takes input from terminal 
            input  = new DataInputStream(socket.getInputStream()); 
            // sends output to the socket 
            out    = new DataOutputStream(socket.getOutputStream()); 
        } 
        catch(UnknownHostException u) 
        { 
            System.out.println(u); 
        } 
        catch(IOException i) 
        { 
            System.out.println(i); 
        } 
    } 
  
    public void writeToServer(String line)
    {
        while (!line.contains(":D")) 
        { 
            try
            { 
                out.writeUTF(line); 
            } 
            catch(IOException i) 
            { 
                System.out.println(i); 
            } 
        }
    }
    
    public void readFromServer(String line)
    {
        while (!line.contains(":D")) 
        { 
            try
            { 
                line=input.readLine(); 
            } 
            catch(IOException i) 
            { 
                System.out.println(i); 
            } 
        }
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