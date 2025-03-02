import java.io.*;  // Importing I/O classes for input/output operations
import java.net.*; // Importing networking classes for socket programming

public class SimpleClient { 
    public static void main(String[] args) throws IOException {
        // Create a socket to connect to the server running on localhost (127.0.0.1) at port 5000
        try (Socket socket = new Socket("127.0.0.1", 5000)) {
            System.out.println("Connected to Server");

            // BufferedReader to read user input from the console
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

            // Create DataOutputStream to send data to the server
            try (DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
                String message;

                // Continuously read user input and send to the server until "Over" is entered
                while (!(message = input.readLine()).equalsIgnoreCase("Over")) {
                    out.writeUTF(message);  // Send message to the server
                }
            } 
        } 
    }
}
