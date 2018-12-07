package cgserver;

import static org.junit.jupiter.api.Assertions.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import org.junit.jupiter.api.Test;

/**
 * This java file is to be used as a pseudo-client for ClientHandlerTest_v1.java
 * 
 * This file acts as the other side of the network interactions
 * 
 * These tests assume that  both sides of the network interactions reside on the same computer
 * 
 * @author gideon.hou
 *
 */
class ClientHandlerTest_helper_v1 {

	/*
	 * 
	 * Test constructor helper
	 * 
	 * This function just creates a socket. This socket will try to connect to the serversocket created by ClientHandlerTest_v1.java
	 * and then that is all
	 * 
	 */
	@Test
	void constructor_test_helper() throws IOException {
		
		// get ip address of this computer and set a socket
		InetAddress thisAddress = InetAddress.getLocalHost();
		int socket = 60000;
		
		// create a new socket
		Socket helper_socket = new Socket(thisAddress, socket);
		
		// close the socket
		helper_socket.close();
	}
	
	/*
	 * This tests if ClientHandler can communicate
	 */
	@Test
	void communication_test_helper() throws IOException {

		// prepare two string arrays. 1 array will be sent to the ClientHandler object sequentially
		// The other array is expected to be received from the ClientHandler object sequentially
		String[] strings_to_send = {"This", "is", "test", "version", "one"};
		String[] strings_to_send2 = {"black", "blue", "green", "Start Game"};
		String[] strings_to_receive = {"Clue", "Less", "Game", "dot", "java"};
		
		// get ip address of this computer and set a socket
		InetAddress thisAddress = InetAddress.getLocalHost();
		int socket = 60001;
				
		// create a new socket
		Socket helper_socket = new Socket(thisAddress, socket);
		
		// set input/output datastreams so we can communicate
		DataInputStream in = new DataInputStream(helper_socket.getInputStream());
		DataOutputStream out = new DataOutputStream(helper_socket.getOutputStream());
		
		// receive a message from ClientHandler object and check if message is correct
		for (String el : strings_to_receive) {
			String received_message = new String(in.readUTF());
			assert(received_message.equals(el));
		}
		
		// begin sending messages
		for (String message_to_send : strings_to_send) {
			out.writeUTF(message_to_send);
		}
		
		// Send "LOGOUT" so run can terminate on the other side
		out.writeUTF("LOGOUT");
		
		// receive another message
		String received_message1 = new String(in.readUTF());
		assert(received_message1.equals("hello1"));
		
		
		// begin sending messages
		for (String message_to_send : strings_to_send2) {
			out.writeUTF(message_to_send);
		}
		
		out.writeUTF("LOGOUT");
		
		// close the socket
		helper_socket.close();
	}

}
