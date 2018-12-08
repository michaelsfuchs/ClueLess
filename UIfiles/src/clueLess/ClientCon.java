package clueLess;



/**
 *
 * @author nachiket
 */
import clueLess.GameBoard;
import clueLess.clueLess;
import java.net.*; 
import java.io.*; 
import java.util.Arrays;
  
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
                System.out.println(""+received); 
                if(!received.isEmpty() || !(received == null) || !received.equals("")){
                 //  System.out.println("Read line : "+received);
                   String processedMsg = processRequest(received);
                   //write to updates area the processedMsg
                   gb.pushTextUpdate(processedMsg);
                   received="";
                }
            }
            
        }
        catch(Exception e){
            System.out.println("Connection to server failed");
            e.printStackTrace();
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
        String ret = msgtoproc;
        String msgsplit[] = msgtoproc.split(":");
        String msgid = "-1"; 
        if(msgsplit.length>1){
            msgid = msgsplit[1];
        }
        System.out.println("Read message : "+msgtoproc);
        if(msgid.equals("0")){
            //Inform that player has moved to a location
            ret=msgsplit[2];
        }
        if(msgid.equals("1")){
            //Inform that player has moved to a location
            ret="Player "+ msgsplit[2]+" has moved to location "+msgsplit[3];
            clueLess.updatePlayerLocation(Integer.parseInt(msgsplit[2]),Integer.parseInt(msgsplit[3]));
        }
        if(msgid.equals("2")){
            //Inform the turn options to the player
            String finalmsg="";
            int [] availableMoves = new int[0];
            for(int i=3 ; i<msgsplit.length ; i++){
                finalmsg=finalmsg+" "+msgsplit[i];
                availableMoves = addElement(availableMoves,Integer.parseInt(msgsplit[i]));
            }
            //cueTurn function has to be called
            Boolean canSugg = false;
            if(msgsplit[2]=="1"){
                canSugg = true;
            }
            
            clueLess.cueUsersTurn(canSugg, availableMoves);
            ret="Moves are possible to "+ finalmsg;
        }
        if(msgid.equals("4")){
            //Inform the player that a suggestion has been made by another player
            ret="Suggestion: "+returnS(msgsplit)+returnR(msgsplit)+returnW(msgsplit)+" has been made by player: "+msgsplit[0];
        }
        if(msgid.equals("8")){
            //Inform the player that he or she loses
            ret="YOU LOSE";
        }
        if(msgid.equals("10")){
            //Inform the player of their initial hand
            //Call inithand function
            ret="Initial hand is : "+returnS(msgsplit)+returnR(msgsplit)+returnW(msgsplit);
        }
        if(msgid.equals("11")){
            //Inform that player of cards that can be disapproved
            //New function
            
            ret="New Player has joined with player ID : "+msgsplit[2];
            gb.addPlayer(msgsplit[2]);
        }
        if(msgid.equals("12")){
            ret = " PlayerID : ";
            for(int i=3;i<msgsplit.length;i=i+2){
                ret=ret + msgsplit[i-1]+" Player Name : "+msgsplit[i];
                if(!Arrays.asList(gb.users).contains(msgsplit[i])){
                    gb.addPlayer(msgsplit[i]);
                }
                
            }    
            
        }
        return ret;   
    }
    
    public int[] addElement(int[] org, int added) {
        int[] result = Arrays.copyOf(org, org.length +1);
        result[org.length] = added;
        return result;
    }
    
    public String[] addElement(String[] org, String added) {
        String[] result = Arrays.copyOf(org, org.length +1);
        result[org.length] = added;
        return result;
    }
    public void writeToServer(String line)
    {
        //while (!line.contains(":W")) 
        //{ 
            try
            { 
                System.out.println("Sending to server : "+line);
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