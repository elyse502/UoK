package JavaNet;

/**
 * Chat Server for Multiple Clients
 * @author STUDENTS
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static List<ClientHandler> clients = new ArrayList<>(); // Initialize an empty list of client handlers
    
    public static void main(String[] args) throws IOException {
        ServerSocket servSocket = new ServerSocket(5000);
        System.out.println("Server started. Waiting for clients...");
    }
    
}
