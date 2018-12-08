package com.clientconnect;

import static org.junit.jupiter.api.Assertions.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.jupiter.api.Test;

/**
 * This test file tests ClientCon.java
 * 
 * This file verifies 
 * 
 * 1.	ClientCon object can be invoked
 * 2.	ClientCon object can send and receive data
 * 3.	ClientCon object can close its own input/output datastreams and socket
 * 
 * @author gideon.hou
 *
 */
class ClientConTest_v1 {
	
	/*
	 * Constructor test
	 */
	@Test
	void constructor_test() throws UnknownHostException {
		// after the ClientConTest_v* has started, instantiate a new CLientCon object
		InetAddress thisAddress = InetAddress.getLocalHost();
		
		String thisAddress_string = thisAddress.getHostAddress();

		//System.out.println(InetAddress.getLocalHost().toString());
		ClientCon test_clientcon = new ClientCon(thisAddress_string, 60000);
		
		// check if object variables were initialzed
		assertNotNull(test_clientcon.socket);
		assertNotNull(test_clientcon.input);
		assertNotNull(test_clientcon.out);
		// close socket
		try {
			test_clientcon.stopClient();
		} catch (Exception e){
			fail("unable to close ClientCon object's input/output datastreams and socket");
		}
	}
	
	/*
	 * Constructor test - failure example
	 */
	@Test
	void constructor_test_fail() throws UnknownHostException {
		// after the ClientConTest_v* has started, instantiate a new CLientCon object
		//InetAddress thisAddress = InetAddress.getLocalHost();
		
		//String thisAddress_string = thisAddress.getHostAddress();
		String thisAddress_string = "999.888.777.666"; // create an incorrect ip address
		
		//System.out.println(InetAddress.getLocalHost().toString());
		ClientCon test_clientcon = new ClientCon(thisAddress_string, 60001);
		
		// check if object variables were initialized
		assertNotNull(test_clientcon.socket);
		assertNotNull(test_clientcon.input);
		assertNotNull(test_clientcon.out);
		// close socket
		try {
			test_clientcon.stopClient();
		} catch (Exception e){
			fail("unable to close ClientCon object's input/output datastreams and socket");
		}
	}
	
	/*
	 * receive messages test
	 */
	@Test
	void send_receive_test() throws UnknownHostException {
		
		// prepare 1 string array to send and 1 to receive
		String[] messages_to_send = {"this", "message", "comes", "from", "ClientCon"};
		String[] messages_to_receive = {"received", "from", "helper"};
		
		// after the ClientConTest_v* has started, instantiate a new CLientCon object
		InetAddress thisAddress = InetAddress.getLocalHost();
				
		String thisAddress_string = thisAddress.getHostAddress();

		//System.out.println(InetAddress.getLocalHost().toString());
		ClientCon test_clientcon = new ClientCon(thisAddress_string, 60003);
				
		// receive messages from ClientConTest_helper_v*
		try {
			test_clientcon.readFromServer("");
		} catch (Exception e) {
			fail("unable to receive messages from server");
		}
		
		// close socket
		try {
			test_clientcon.stopClient();
		} catch (Exception e){
			fail("unable to close ClientCon object's input/output datastreams and socket");
		}
	}

}
