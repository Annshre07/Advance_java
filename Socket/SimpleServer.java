import java.io.*;  // It is used for input/output operations.
import java.net.*; // It is used for networking functionalities.

public class SimpleServer {  
    public static void main(String[] args) throws IOException {
        // Creating a ServerSocket that listens on port 5000
        try (ServerSocket server = new ServerSocket(5000)) {
            System.out.println("Server started, waiting for client...");

            // Accept an incoming client connection. It blocks until the client is connected.
            //Program blocks until the connection gets established.
            try (Socket socket = server.accept();
                 // Create a DataInputStream to receive messages from the client
                 DataInputStream in = new DataInputStream(socket.getInputStream())) {
                 
                System.out.println("Client connected");  

                // Read messages from the client until "Over" is received
                for (String message; !(message = in.readUTF()).equalsIgnoreCase("Over");)
                    System.out.println("Client: " + message);  // Print the received message
            }
        }
    }
}
