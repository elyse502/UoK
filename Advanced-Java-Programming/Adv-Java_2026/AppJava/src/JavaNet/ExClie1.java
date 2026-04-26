package JavaNet;

import java.net.*;
import java.io.*;

public class ExClie1 {

    public static void main(String[] args) {
        try {
            Socket clie = new Socket("localhost", 3333);
            
            DataOutputStream dout = new DataOutputStream(clie.getOutputStream());
            
            dout.writeUTF("Hi There, Danny here!");

            dout.flush();
            
            dout.close();
            
            clie.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
    
}
