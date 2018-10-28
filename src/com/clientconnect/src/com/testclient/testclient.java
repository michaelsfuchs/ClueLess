/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testclient;

import com.clientconnect.*;

/**
 *
 * @author nakulkar
 */
public class testclient {
    
    
    public static void main(String args[]){
        ClientCon client=new ClientCon(args[0],Integer.parseInt(args[1]));
        client.start();
        client.writeToServer("Test connection 1");
        client.writeToServer("Test connection 2");
        client.writeToServer("Test connection :W");
        client.readFromServer();
        client.stopClient();
    }
}
