import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.*;

// This is the main server class where everything begins
public class ChatServer {
    private static final int PORT = 5000; // This is the port number where the server will "listen" for client connections

    // This is a list to keep track of all the connected clients.
    // ArrayList is not safe when many threads (clients) access it at the same time.
    // So we use Collections.synchronizedList to make it thread-safe.
    private static List<ClientHandler> clientHandlers = Collections.synchronizedList(new ArrayList<>());

    public static void main(String args[]) {
        // Try-with-resources to create a server socket. This socket listens on port 5000.
        // A ServerSocket waits (listens) for incoming client connections.
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Chat server started on port " + PORT);

            // The server runs forever (while true) and keeps accepting new clients
            while (true) {
                // serverSocket.accept() is a blocking method. It waits until a client connects.
                Socket clientSocket = serverSocket.accept(); // When a client connects, it returns a socket

                System.out.println("New client connected: " + clientSocket);

                // Create a handler (a helper thread) for this connected client
                ClientHandler handler = new ClientHandler(clientSocket);

                // Add this handler to the list of all connected clients
                clientHandlers.add(handler);

                // Start this handler as a separate thread so that it can communicate independently
                new Thread(handler).start();
            }

        } catch (Exception e) {
            e.printStackTrace(); // If anything goes wrong, show the error
        }
    }

    // This function sends a message to ALL connected clients, EXCEPT the one who sent it
    public static void broadcast(String message, ClientHandler sender) {
        synchronized (clientHandlers) { // Synchronize so that multiple threads don’t clash
            for (ClientHandler client : clientHandlers) {
                if (client != sender) {
                    // Send the message to all other clients
                    ((PrintWriter) client.getWriter()).println(message);
                }
            }
        }
    }

    // This function removes a client from the list when they leave or disconnect
    public static void removeClient(ClientHandler client) {
        clientHandlers.remove(client);
    }

    // This function logs a message on the server console with the current timestamp
    public static void log(String message) {
        System.out.println("[" + LocalDateTime.now() + "] " + message);
    }
}

// This is the class that handles communication with a single client.
// Each connected client will have one such handler running in a separate thread.
class ClientHandler implements Runnable {
    private final Socket socket; // This is the connection between the client and server
    private PrintWriter out;     // This is used to send data/messages to the client
    private BufferedReader in;   // This is used to receive data/messages from the client
    private String username;     // The username of the client (taken when they join)

    // Constructor initializes the socket for this client
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    // This method runs when the thread is started
    @Override
    public void run() {
        try {
            // Setting up input stream to receive data from client
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Setting up output stream to send data to client
            out = new PrintWriter(socket.getOutputStream(), true); // true for auto-flush

            // Ask for username when client connects
            out.println("Enter your username:");
            username = in.readLine(); // Read username input from client

            // Notify all clients that this user has joined
            ChatServer.broadcast("User " + username + " has joined the chat.", this);
            ChatServer.log("User " + username + " connected.");

            String message;
            // Continuously read messages from the client
            while ((message = in.readLine()) != null) {
                // Format message with username (e.g., Alice: Hello)
                String formattedMessage = username + ": " + message;

                // Log this message on the server console
                ChatServer.log(formattedMessage);

                // Send this message to all other clients
                ChatServer.broadcast(formattedMessage, this);
            }
        } catch (IOException e) {
            // If an error occurs (like client forcefully disconnects), catch it here
            System.out.println("Connection error with user " + username);
        } finally {
            // This block always runs, whether error occurs or not (used for cleanup)
            ChatServer.removeClient(this); // Remove this client from active list

            // Notify everyone that this user has left
            ChatServer.broadcast("User " + username + " has left the chat.", this);
            ChatServer.log("User " + username + " disconnected.");

            try {
                socket.close(); // Close the connection properly
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // A helper function that returns the output stream to send messages
    public PrintWriter getWriter() {
        return out;
    }
}
