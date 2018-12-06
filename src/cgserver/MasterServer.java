/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cgserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author nakulkar
 */
public class MasterServer {
    private Socket          socket   = null; 
    private ServerSocket    server   = null; 
    private DataInputStream in       =  null; 
    private DataOutputStream out     = null;
    private int port = 0;
    ArrayList<Integer> portsblocked=new ArrayList<Integer>();
    
    public MasterServer() throws IOException{
        server= new ServerSocket(5000);
        try{
            System.out.println("MASTER SERVER STARTED");
            int count = 0;    
            while(true){
                
                socket = server.accept();
                System.out.println("RECEIVED NEW REQUEST");
                //Generate a port for the a new server
                Random r=new Random();
                //make the server port 4 digit
                int randport=r.nextInt(1000);
                System.out.println("Random port is :"+randport);
                for(int i=0; i < portsblocked.size() ; i++){
                    int c = portsblocked.get(i);
                    if(c == randport){
                        randport = r.nextInt();
                    }
                }
                port = 9000 + randport;
                portsblocked.add(randport);
                
                System.out.println("ALLOCATED SERVER ON PORT :"+ port);
                //Run a new server
                CGServer newSv=new CGServer(port);          
                out = new DataOutputStream(socket.getOutputStream());                
                out.writeUTF(""+port);
                
                //close the socket with the master server
                socket.close();
            }
        }
        catch(Exception e){
            
        }
    }
    
//    public static void main(String args[]) throws IOException{
//        MasterServer sv=new MasterServer();
//        
//    }
}