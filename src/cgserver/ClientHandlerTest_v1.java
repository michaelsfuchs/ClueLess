package cgserver;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.List;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

/**
 * This class tests ClientHandler.java
 * 
 * I will test for the following:
 * 
 * 1.	Can ClientHandler.java send strings messages?
 * 2.	Can ClientHandler.java receive string messages?
 * 3.	Are received messages stored?
 * 4.	Are values in the constructor set?
 * 
 * @author gideon.hou
 *
 */
class ClientHandlerTest_v1 {

	// This tests if ClientHandler can initialize
	@Test
	void constructor_test() throws IOException {
		
		// create a new  server socket that will be used to return a socket that will be used to invoke a ClientHandler object
		// socket: 50000
		// playerID: 0
		// backlog: 5
		int socket = 60000;
		int playerID = 0;
		int backlog = 5;
		InetAddress thisAddress = InetAddress.getLocalHost();
		
		//System.out.println("ipaddr: " + thisAddress.getHostAddress());
		
		ServerSocket serversocket = new ServerSocket(socket, backlog, thisAddress);
		
		// accept a connection request and then use the new socket to invoke a ClientHandler object
		Socket clienthandler_socket = serversocket.accept();
		
		ClientHandler test_clienthandler = new ClientHandler(playerID, clienthandler_socket);
	
		/*
		// check object attributes after object invocation
		System.out.println(test_clienthandler.userName);
		System.out.println(test_clienthandler.ipAddress);
		System.out.println(test_clienthandler.socket.getInetAddress().getHostAddress());
		System.out.println(test_clienthandler.socket.getInetAddress().getHostName());
		*/
		
		// use assertions to check if values were set properly. We can test input/output streams in another test
		//assert(test_clienthandler.userName == playerID);
		//assert(test_clienthandler.ipAddress.equals("127.0.0.1"));
		assert(test_clienthandler.socket.equals(clienthandler_socket));
		assert(test_clienthandler.isloggedin);
		
		
		// close datastreams and sockets
		test_clienthandler.in.close();
		test_clienthandler.out.close();
		serversocket.close();
		test_clienthandler.socket.close();
	}
	
	// This tests if ClientHandler can communicate
	@Test
	void communication_test() throws IOException {
		
		// prepare two string arrays. One array will be sent to the ClientHandler object sequentially
		// The other array is expected to be received from the ClientHandler object sequentially
		String[] strings_to_receive = {"This", "is", "test", "version", "one"};
		String[] strings_to_receive2 = {"This", "is", "test", "version", "one", "LOGOUT", "black", "blue", "green", "Start Game"};
		String[] strings_to_send = {"Clue", "Less", "Game", "dot", "java"};

		// create a new  server socket that will be used to return a socket that will be used to invoke a ClientHandler object
		// socket: 50002
		// playerID: 0
		// backlog: 5
		int socket = 60001;
		int playerID = 0;
		int backlog = 5;
		InetAddress thisAddress = InetAddress.getLocalHost();
								
		ServerSocket serversocket = new ServerSocket(socket, backlog, thisAddress);
				
		// accept a connection request and then use the new socket to invoke a ClientHandler object
		Socket clienthandler_socket = serversocket.accept();
		ClientHandler test_clienthandler = new ClientHandler(playerID, clienthandler_socket);
			
		// write several messages from the ClientHandler object
		for (String message_to_send_from_ClientHandler : strings_to_send) {
			test_clienthandler.writeMsg(message_to_send_from_ClientHandler);
		}
		
		// call "run" to begin receiving first set of messages
		test_clienthandler.run();
		
		// check if the received messages that were saved are correct
		for (int i = 0; i < strings_to_receive.length; i++) {
			String correct_message = strings_to_receive[i];
			String received_message = test_clienthandler.newMessages.get(i);
			assertEquals(correct_message, received_message);
		}
		// check if CGServer.startGame is still false
		assert(!CGServer.startGame);
		
		// send another message to the other side to signal that the ClientHandler object is about to receive messages again
		test_clienthandler.writeMsg("hello1");
		
		test_clienthandler.run();
		
		// check if the received messages that were saved are correct
		System.out.println("this:\n" + test_clienthandler.newMessages.toString());
		
		for (int i = 0; i < test_clienthandler.newMessages.size(); i++) {
			String correct_message = strings_to_receive2[i];
			String received_message = test_clienthandler.newMessages.get(i);
			assertEquals(correct_message, received_message);
		}
		
		// check if CGServer.startGame is still false
		assert(CGServer.startGame);
		// close datastreams and sockets
		test_clienthandler.in.close();
		test_clienthandler.out.close();
		serversocket.close();
		test_clienthandler.socket.close();
	}
	
}
