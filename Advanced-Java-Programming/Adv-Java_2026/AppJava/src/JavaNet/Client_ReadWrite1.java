
package JavaNet;

import java.net.*;
import java.io.*;
        
public class Client_ReadWrite1 {

    public static void main(String[] args) {
        try {
            Socket clie = new Socket("localhost", 3333);            
            DataInputStream din = new DataInputStream(clie.getInputStream());
            DataOutputStream dout = new DataOutputStream(clie.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            
            String str = "", str1 = "";
            
            while(!str.equals("stop")) {
                str = br.readLine();
                dout.writeUTF(str);
                dout.flush();
                str1 = din.readUTF();
                
                System.out.println("Server Says: " + str1);
            }
            dout.close();           
            clie.close();
            System.out.println("Connection Closed.");
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
    
}
