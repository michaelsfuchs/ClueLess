package clueLess;



/**
 *
 * @author nachiket
 */
import clueLess.GameBoard;
import clueLess.clueLess;
import static clueLess.clueLess.gb;
import java.net.*; 
import java.io.*; 
import java.util.Arrays;
import javax.swing.JOptionPane;
  
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
            JOptionPane.showMessageDialog(gb, "Cannot Connect to the Game ID provided", "Invalid port ID", JOptionPane.ERROR_MESSAGE);
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
    public String returnR(String msgsplit[]){
        String finalmsg="";
        String room = null;
        for(int i=2;i<msgsplit.length;i=i+2){
            if(msgsplit[i].contains("1")){
                room=gb.getRoom(Integer.parseInt(msgsplit[i+1]));
                finalmsg=finalmsg+" Room: "+room;
            }
        }
        
        return finalmsg;
    }
    
    public String returnS(String msgsplit[]){
        String finalmsg="";
        String suspect = null;
        for(int i=2;i<msgsplit.length;i=i+2){

            if(msgsplit[i].contains("0")){
                suspect=gb.getSuspect(Integer.parseInt(msgsplit[i+1]));
                finalmsg=finalmsg+" Suspect: "+suspect;
            }
        }
        
        return finalmsg;
    }
    
    public String returnW(String msgsplit[]){
        String finalmsg="";
        String weapon = null;
        for(int i=2;i<msgsplit.length;i=i+2){

            if(msgsplit[i].contains("2")){
                weapon=gb.getWeapon(Integer.parseInt(msgsplit[i+1]));
                finalmsg=finalmsg+" Weapon: "+weapon;
            }
        }
        
        return finalmsg;
    }
    
    public String processRequest(String msgtoproc) throws UnknownHostException{
        String ret = msgtoproc;
        String msgsplit[] = msgtoproc.split(":");
        String msgid = "-1"; 
        if(msgtoproc.contains("Start Game")){
            clueLess.switchScreens("GameBoard");
        }
        if(msgsplit.length>1){
            msgid = msgsplit[1];
        }
        System.out.println("Read message : "+msgtoproc);
        if(msgid.equals("0")){
            //Inform players that the game was started.
            System.out.println("Msg Rcvd: game start msg \n");
            ret=msgsplit[2];
            clueLess.startGame();
        }
        if(msgid.equals("1")){
            //Inform that player has moved to a location
            System.out.println("Msg Rcvd: player location update \n");
            int temp1 = Integer.parseInt(msgsplit[2]);
            int temp2 = Integer.parseInt(msgsplit[3]);
            ret="Player "+ gb.getSuspect(temp1)+" has moved to the "+gb.getRoom(temp2);
            clueLess.updatePlayerLocation(temp1,temp2);
        }
        if(msgid.equals("2")){
            //Inform the turn options to the player
            System.out.println("Msg Rcvd: player move options \n");
            String finalmsg="";
            int [] availableMoves = new int[0];
            for(int i=3 ; i<msgsplit.length ; i++){
                finalmsg=finalmsg+" "+gb.getRoom(Integer.parseInt(msgsplit[i]));
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
            System.out.println("Msg Rcvd: suggestion from another player \n");
            ret="Suggestion: "+returnS(msgsplit)+returnR(msgsplit)+returnW(msgsplit)+" has been made by player: "+msgsplit[0];
        }
        if(msgid.equals("6")){
            //Inform the player that a player has revealed a card
            System.out.println("Msg Rcvd: player has revealed a card \n");
            String user = gb.getSuspect(Integer.parseInt(msgsplit[0]));
            ret="Player : "+user+" has revealed card : "+returnS(msgsplit)+returnR(msgsplit)+returnW(msgsplit);
        }
        if(msgid.equals("8")){
            //Inform the player that he or she loses
            System.out.println("Msg Rcvd: false accusation \n");
            ret=msgtoproc;
        }
        if(msgid.equals("10")){
            //Inform the player of their initial hand
            System.out.println("Msg Rcvd: init cards \n");
            ret="Initial hand is : "+returnS(msgsplit)+returnR(msgsplit)+returnW(msgsplit);
            
            int cardType[] = new int[(msgsplit.length-2)/2];
            int cardID[] = new int[(msgsplit.length-2)/2];
            
            int x=0;
            for(int ii=3;ii<msgsplit.length;ii=ii+2,x++){
                cardType[x] = Integer.parseInt(msgsplit[ii-1]);
                cardID[x] = Integer.parseInt(msgsplit[ii]);
            }
            clueLess.initCards(cardType,cardID);
            
        }
        if(msgid.equals("11")){
            //Inform that player of cards that can disprove a suggestion
            System.out.println("Msg Rcvd: disprove suggestion with card \n");
            
            int numCards = ((msgsplit.length-2)/2);
            int cardType[] = new int[numCards];
            int cardID[] = new int[numCards];
            
            int x=0;
            for(int ii=3;ii<msgsplit.length;ii=ii+2,x++){
                cardType[x] = Integer.parseInt(msgsplit[ii-1]);
                cardID[x] = Integer.parseInt(msgsplit[ii]);
            }
            clueLess.disprovePlayerSuggestion(cardType,cardID);
            
            ret="Need to disprove suggestion : "+returnS(msgsplit)+returnR(msgsplit)+returnW(msgsplit);
        }
        if(msgid.equals("12")){
            System.out.println("Msg Rcvd: player added \n");
            ret = " PlayerID : ";
            for(int i=3;i<msgsplit.length;i=i+2){
                ret=ret + msgsplit[i-1]+" Player Name : "+msgsplit[i];
                if(!Arrays.asList(gb.users).contains(msgsplit[i])){
                    clueLess.addPlayer(msgsplit[i]);
                }
                
            }    
            
        }
        if(msgid.equals("13")){
            ret = "Game has ended" ;
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
    
    public static int connectMaster(String ipj){
        try{
            //connects to the master server
            //Socket masterSocket=new Socket("ec2-3-17-66-140.us-east-2.compute.amazonaws.com",5000);
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