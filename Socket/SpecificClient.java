import java.io.*;
import java.net.Socket;

// This is the main class for the chat client.
// It connects to the server and lets the user chat.
public class SpecificClient {
    // This is the IP address or host name of the server.
    // "localhost" means the server is running on the same computer.
    private static final String SERVER_ADDRESS = "localhost";

    // This must match the port that the server is listening on.
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        try (
            // Try to connect to the server using a socket.
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);

            // For reading messages sent by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // For sending messages to the server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // For reading user input from the keyboard
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Connected to chat server");

            // The server will ask for a username; read and send it
            System.out.print(in.readLine() + " ");
            String username = userInput.readLine();
            out.println(username); // Send the username to the server

            // Start a new thread to constantly read messages from the server
            Thread readThread = new Thread(() -> {
                try {
                    String message;
                    // Keep printing messages from server
                    while ((message = in.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            });
            readThread.start(); // Start the reading thread

            // Main thread: keep reading user input and send it to the server
            String userMessage;
            while ((userMessage = userInput.readLine()) != null) {
                out.println(userMessage); // Send typed message to server
            }

        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }
}
