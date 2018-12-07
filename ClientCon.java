package clueLess;



/**
 *
 * @author nachiket
 */
import clueLess.GameBoard;
import clueLess.clueLess;
import java.net.*; 
import java.io.*; 
  
public class ClientCon extends Thread
{ 
    // initialize socket and input output streams 
    public Socket socket           = null; 
    public DataInputStream  in   = null; 
    public DataOutputStream out     = null; 
    public String address = "" ;
    public GameBoard gb;
    //public String myUserName="";
    //public String myIpAddress="";
    public int port = 0;
    // constructor to put ip address and port 
    public ClientCon(String address, int port, GameBoard gb) 
    { 
        // establish a connection 
        try
        { 
             this.address=address;
             this.port=port;
             //this.myUserName=InetAddress.getLocalHost().getHostName();
             //this.myIpAddress=InetAddress.getLocalHost().getHostAddress();
             socket = new Socket(address, port); 
            System.out.println("Connection to server successful"); 
            // takes input from terminal 
            in  = new DataInputStream(socket.getInputStream()); 
            // sends output to the socket 
            out    = new DataOutputStream(socket.getOutputStream());
            this.gb=gb;
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
                   String processedMsg = processRequest(received);
                   //write to updates area the processedMsg
                   gb.pushTextUpdate(processedMsg);
                   received="";
                }
            }
            
        }
        catch(Exception e){
            System.out.println("Connection to server failed");
        }
    } 
    
    /*
    Contains the code to process messages from the
    server and call relevant functions 
    to affect UI changes
    */
    public static String returnR(String msgsplit[]){
        String finalmsg="";
        String room = null;
        for(int i=2;i<msgsplit.length;i=i+2){
            if(msgsplit[i].contains("1")){
                room=msgsplit[i+1];
                finalmsg=finalmsg+" Room: "+room;
            }
        }
        
        return finalmsg;
    }
    
    public static String returnS(String msgsplit[]){
        String finalmsg="";
        String suspect = null;
        for(int i=2;i<msgsplit.length;i=i+2){

            if(msgsplit[i].contains("0")){
                suspect=msgsplit[i+1];
                finalmsg=finalmsg+" Suspect: "+suspect;
            }
        }
        
        return finalmsg;
    }
    
    public static String returnW(String msgsplit[]){
        String finalmsg="";
        String weapon = null;
        for(int i=2;i<msgsplit.length;i=i+2){

            if(msgsplit[i].contains("2")){
                weapon=msgsplit[i+1];
                finalmsg=finalmsg+" Weapon: "+weapon;
            }
        }
        
        return finalmsg;
    }
    
    public String processRequest(String msgtoproc) throws UnknownHostException{
        String ret= "# No Msg ";
        String msgsplit[] = msgtoproc.split(":");
        String msgid = msgsplit[1];
        
        if(msgid.contains("1")){
            //Inform that player has moved to a location
            ret="Player "+ msgsplit[2]+" has moved to location "+msgsplit[3];
        }
        if(msgid.contains("2")){
            //Inform the turn options to the player
            String finalmsg="";
            for(int i=3 ; i<msgsplit.length ; i++){
                finalmsg=finalmsg+" "+msgsplit[i];
            }
            ret="Moves are possible to "+ finalmsg;
        }
        if(msgid.contains("4")){
            //Inform the player that a suggestion has been made by another player
            ret="Suggestion: "+returnS(msgsplit)+returnR(msgsplit)+returnW(msgsplit)+" has been made by player: "+msgsplit[0];
        }
        if(msgid.contains("8")){
            //Inform the player that he or she loses
            ret="YOU LOSE";
        }
        if(msgid.contains("10")){
            //Inform the player of their initial hand
            ret="Initial hand is : "+returnS(msgsplit)+returnR(msgsplit)+returnW(msgsplit);
        }
        if(msgid.contains("11")){
            //Inform that player of cards that can be disapproved
            ret="New Player has joined with player ID : "+msgsplit[2];
            gb.addPlayer(msgsplit[2]);
        }
        if(msgid.contains("12")){
            //Assigns player id to the player
            ret="Player has joined :" + msgsplit[2];
            if( clueLess.playerID == -1)
            {
                clueLess.playerID = Integer.parseInt(msgsplit[2]);
            }
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