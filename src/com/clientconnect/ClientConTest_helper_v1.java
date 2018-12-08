package com.clientconnect;

import static org.junit.jupiter.api.Assertions.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.jupiter.api.Test;

/**
 * helper .java file to test ClientCon.java
 * 
 * @author gideon.hou
 *
 */
class ClientConTest_helper_v1 {

	/*
	 * helper method for constructor test
	 */
	@Test
	void constructor_test_helper() throws IOException {
		
		// create a new  server socket that will be used to listen for a connection request 
		// from ClientCon object
		// socket: 50000
		// playerID: 0
		// backlog: 5
		int socket = 60000;
		int playerID = 0;
		int backlog = 5;
		InetAddress thisAddress = InetAddress.getLocalHost();
				
		//System.out.println("ipaddr: " + thisAddress.getHostAddress());
				
		ServerSocket serversocket = new ServerSocket(socket, backlog, thisAddress);
		
		Socket clientcon_socket = serversocket.accept();
		
		// set input/output datastreams so we can communicate
		DataInputStream in = new DataInputStream(clientcon_socket.getInputStream());
		DataOutputStream out = new DataOutputStream(clientcon_socket.getOutputStream());
		
		
		// close sockets and input/output streams
		in.close();
		out.close();
		clientcon_socket.close();
				
	}
	
	/*
	 * helper method for constructor test - failure example
	 */
	@Test
	void constructor_test_helper_fail() throws IOException {
		
		// create a new  server socket that will be used to listen for a connection request 
		// from ClientCon object
		// socket: 50000
		// playerID: 0
		// backlog: 5
		int socket = 60001;
		int playerID = 0;
		int backlog = 5;
		InetAddress thisAddress = InetAddress.getLocalHost();
				
		//System.out.println("ipaddr: " + thisAddress.getHostAddress());
				
		ServerSocket serversocket = new ServerSocket(socket, backlog, thisAddress);
		
		Socket clientcon_socket = serversocket.accept();
		
		// set input/output datastreams so we can communicate
		DataInputStream in = new DataInputStream(clientcon_socket.getInputStream());
		DataOutputStream out = new DataOutputStream(clientcon_socket.getOutputStream());
		
		
		// close sockets and input/output streams
		in.close();
		out.close();
		clientcon_socket.close();
				
	}

	/*
	 * helper method for send/receive messages test
	 */
	@Test
	void send_receive_test_helper() throws IOException {
		
		// prepare 1 string array to send and 1 to receive
		String[] messages_to_receive = {"this", "message", "comes", "from", "ClientCon"};
		String[] messages_to_send = {"received", "from", "helper"};
				
		// create a new  server socket that will be used to listen for a connection request 
		// from ClientCon object
		// socket: 50000
		// playerID: 0
		// backlog: 5
		int socket = 60003;
		int playerID = 0;
		int backlog = 5;
		InetAddress thisAddress = InetAddress.getLocalHost();
				
		//System.out.println("ipaddr: " + thisAddress.getHostAddress());
				
		ServerSocket serversocket = new ServerSocket(socket, backlog, thisAddress);
		
		Socket clientcon_socket = serversocket.accept();
		
		// set input/output datastreams so we can communicate
		DataInputStream in = new DataInputStream(clientcon_socket.getInputStream());
		DataOutputStream out = new DataOutputStream(clientcon_socket.getOutputStream());
		
		// send messages to clientcon
		for (int i = 0; i < messages_to_send.length; i++) {
			out.writeUTF(messages_to_send[i]);
		}
		// close sockets and input/output streams
		in.close();
		out.close();
		clientcon_socket.close();
				
	}
}
