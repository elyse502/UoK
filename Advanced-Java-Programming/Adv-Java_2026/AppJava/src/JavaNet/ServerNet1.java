/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaNet;

/**
 * Server Side
 * @author STUDENTS
 */

import java.io.*;
import java.net.*;

public class ServerNet1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ServerSocket serv = new ServerSocket(6666);
            System.out.print("Server listening on port 6666...");
            
            // Accept incoming connection from a client
            Socket clie = serv.accept(); // Blocks until a connection is established
            System.out.println("Client connected: " + clie.getInetAddress().getHostAddress());
            
            // Create a datainputstream to read data from the client
            DataInputStream dis = new DataInputStream(clie.getInputStream());
            
            // Reading a UTF-encoded string sent by the client
            String str = (String) dis.readUTF();
            System.out.println("Message received from Clien: " + str);
            
            serv.close();
            System.out.println("Server socket closed");
        } catch (Exception e) {
            System.out.println("Could not connect: " + e);
        }
    }
    
}
