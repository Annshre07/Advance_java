import java.io.*;
import java.net.Socket;

// This class is responsible for acting as a client that connects to the chat server.
public class ChatClient {

    // The address and port of the server you want to connect to
    private static final String SERVER_ADDRESS = "localhost"; // If server is on another machine, replace with its IP
    private static final int SERVER_PORT = 5000; // Must match the port the server is listening on

    public static void main(String[] args) {

        // Try-with-resources ensures that all resources (sockets, streams) are automatically closed when done
        try (
            // This creates a connection to the server. If the server is running and reachable, this succeeds.
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);

            // Input stream to read data sent *from* the server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Output stream to send data *to* the server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // `true` enables auto-flushing

            // This is used to read what the user types on their keyboard (from the terminal)
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))
        ) {
            // If we reach here, connection is successful
            System.out.println("Connected to chat server");

            // The server will first ask us to enter a username
            System.out.print(in.readLine() + " "); // Print prompt from server: "Enter your username:"
            String username = userInput.readLine(); // Wait for user to type a name and press Enter
            out.println(username); // Send the entered username to the server

            // This thread is responsible for constantly listening to messages from the server
            Thread readThread = new Thread(() -> {
                try {
                    String message;
                    // Keep listening unless server disconnects
                    while ((message = in.readLine()) != null) {
                        // Print the received message from the server on this client's terminal
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    // This happens when server shuts down or network disconnects
                    System.out.println("Disconnected from server.");
                }
            });
            readThread.start(); // Start the background thread

            // Now, the main thread listens to user input and sends messages to the server
            String userMessage;
            // As long as the user keeps typing messages, send them to the server
            while ((userMessage = userInput.readLine()) != null) {
                out.println(userMessage); // Send what the user typed to the server
            }

            // If we ever reach here, it means user stopped typing (or pressed Ctrl+D)

        } catch (IOException e) {
            // If connection fails or something goes wrong during communication
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }
}
