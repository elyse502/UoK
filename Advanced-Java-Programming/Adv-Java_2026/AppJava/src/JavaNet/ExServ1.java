package JavaNet;

import java.io.*;
import java.net.*;

public class ExServ1 {

    public static void main(String[] args) {
        try {
            ServerSocket srv = new ServerSocket(3333);
            
            Socket scl = srv.accept();
            
            DataInputStream dis = new DataInputStream(scl.getInputStream());
            
            String str = (String) dis.readUTF();
            
            System.out.println("Message is " + str);
            
            srv.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }  
}
