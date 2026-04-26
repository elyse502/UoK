package JavaNet;

/**
 * Client Side
 * @author STUDENTS
 */

import java.io.*;
import java.net.*;

public class ClientNet1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Socket clie = new Socket("localhost", 6666);
            
            // Create a DataOutputStream to send to the server
            DataOutputStream dout = new DataOutputStream(clie.getOutputStream());
            
            // Send a UTF -encoded String message to the server
            dout.writeUTF("Hello Server");
            
            // Flush the output stream to ensure all data is sent to the server
            dout.flush();
            
            // Close the output stream to free resources
            dout.close();
            
            // Close the socket connection
            clie.close();
            
            System.out.println("Connection Closed");
        } catch (Exception e) {
            System.out.println("Error occured: " + e);
        }
    }
    
}
