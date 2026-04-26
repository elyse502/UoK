
package JavaNet;

import java.io.*;
import java.net.*;

public class Server_ReadWrite1 {

    public static void main(String[] args) {
        try {
            ServerSocket srv = new ServerSocket(3333);
            
            Socket scl = srv.accept();
            
            System.out.println("Connection made!");
            
            DataInputStream din = new DataInputStream(scl.getInputStream());
            DataOutputStream dout = new DataOutputStream(scl.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            
            String str = "", str1 = "";
            
            while(!str.equals("stop")) {
                str = din.readUTF();
                System.out.println("Client Says: " + str);
                str1 = br.readLine();
                dout.writeUTF(str1);
                dout.flush();
            }
            
            din.close();
            scl.close();
            srv.close();
            
            System.out.println("Server Closed.");
            
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
    
}
