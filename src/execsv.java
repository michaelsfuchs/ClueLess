package execcmd;


import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import java.io.*;
import java.util.logging.Logger;
import java.util.*;
/**
 *
 * @author nakulkar
 */
public class execsv {
    
    public String svhostname= null;
    public String svuser= null ;
    public String svpassword= null;
    
public execsv(String svhost, String svuser, String svpassword){
    this.svhostname=svhost;
    this.svuser=svuser;
    this.svpassword=svpassword;
}    
    
public void runCommand(String command) throws Exception {
    
      String lastCommandOutput = null;
      Logger logger=Logger.getLogger(execsv.class.getName());
      // Setup ssh session with endpoint
      logger.info("starting connection with " + svhostname);
      Connection connection = new Connection(svhostname);
      logger.info("connection object created..");
      connection.connect();
      connection.authenticateWithPassword(svuser, svpassword);
      Session session = connection.openSession();
      InputStream stdout = new StreamGobbler(session.getStdout());                 
      BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(stdout));
      logger.info("connected");
// Run command
      String tempCommand = command;
      logger.info("sending command: " + tempCommand);
      session.execCommand(tempCommand);// + " && sleep 5");
      // Get output
      StringBuilder sb = new StringBuilder();
      while(true){
            String line = stdoutReader.readLine();
            if(line == null)
                  break;
            sb.append(line).append("\n");
      }
      String output = sb.toString();
      lastCommandOutput = output;
      System.out.println("" + output);
      logger.info("got output: " + output);
}
}
