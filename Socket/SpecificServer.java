import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.*;

// This is the main server class — it handles all client connections and messaging.
public class SpecificServer {

    // The port number on which the server will "listen" for clients to connect
    private static final int PORT = 5000;

    // A thread-safe list to store all clients currently connected to the server.
    // Think of this like a list of "active users" chatting on the server.
    private static List<ClientHandler> clientHandlers = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Chat server started on port " + PORT);

            // The server keeps running forever, waiting for new people to join
            while (true) {
                // This line waits (blocks) until a new client connects
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                // We create a helper object (ClientHandler) for the new client
                ClientHandler handler = new ClientHandler(clientSocket);

                // Add this client to our list of all active clients
                clientHandlers.add(handler);

                // Start a new thread to handle communication with this client
                new Thread(handler).start();
            }
        } catch (IOException e) {
            e.printStackTrace(); // If something goes wrong, print the error
        }
    }

    // This function sends a message to all connected clients, except the one who sent it
    public static void broadcast(String message, ClientHandler sender) {
        synchronized (clientHandlers) {
            for (ClientHandler client : clientHandlers) {
                if (client != sender) {
                    client.getWriter().println(message);
                }
            }
        }
    }

    // This function sends a private message to a specific client (identified by username)
    public static void sendPrivateMessage(String recipientUsername, String message, ClientHandler sender) {
        synchronized (clientHandlers) {
            boolean found = false;
            for (ClientHandler client : clientHandlers) {
                // If we find the recipient in the list
                if (client.getUsername().equalsIgnoreCase(recipientUsername)) {
                    client.getWriter().println("(Private from " + sender.getUsername() + "): " + message);
                    found = true;
                    break;
                }
            }

            // If the username wasn't found in the list, inform the sender
            if (!found) {
                sender.getWriter().println("User '" + recipientUsername + "' not found.");
            }
        }
    }

    // Removes a client from the list when they disconnect
    public static void removeClient(ClientHandler client) {
        clientHandlers.remove(client);
    }

    // This function logs any message with the current date and time
    public static void log(String message) {
        System.out.println("[" + LocalDateTime.now() + "] " + message);
    }
}

// This class handles communication with a single client
class ClientHandler implements Runnable {
    private final Socket socket; // A socket represents the connection to the client
    private PrintWriter out;     // Used to send data to the client
    private BufferedReader in;   // Used to receive data from the client
    private String username;     // Stores the client's name

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            // Setup input and output communication streams with the client
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Ask the client for a username (a friendly name)
            out.println("Enter your username:");
            username = in.readLine(); // Receive the username from client

            // Notify everyone that this user has joined
            SpecificServer.broadcast("User " + username + " has joined the chat.", this);
            SpecificServer.log("User " + username + " connected.");

            String message;

            // Keep listening for messages from this client
            while ((message = in.readLine()) != null) {

                // Check if it's a private message using '@'
                if (message.startsWith("@")) {
                    int spaceIndex = message.indexOf(" ");

                    // Check if message format is valid (@username message)
                    if (spaceIndex != -1) {
                        String recipient = message.substring(1, spaceIndex); // Extract username
                        String privateMessage = message.substring(spaceIndex + 1); // Actual message
                        SpecificServer.sendPrivateMessage(recipient, privateMessage, this);
                    } else {
                        out.println("Invalid format. Use: @username message");
                    }
                } else {
                    // It's a public message; send it to everyone
                    String formattedMessage = username + ": " + message;
                    SpecificServer.log(formattedMessage);
                    SpecificServer.broadcast(formattedMessage, this);
                }
            }
        } catch (IOException e) {
            System.out.println("Connection error with user " + username);
        } finally {
            // This block runs whether there's an error or not
            SpecificServer.removeClient(this); // Remove from active clients
            SpecificServer.broadcast("User " + username + " has left the chat.", this);
            SpecificServer.log("User " + username + " disconnected.");

            try {
                socket.close(); // Close the connection to this client
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // A method to get the output stream to send messages
    public PrintWriter getWriter() {
        return out;
    }

    // A method to get the client's username
    public String getUsername() {
        return username;
    }
}
