import java.io.*; // For input and output streams
import java.net.*; // For networking
import java.util.Scanner; // To get user input from console

public class WordChainClient {

    private Socket socket; // This connects the client to the server
    private BufferedReader in; // To read data from the server
    private PrintWriter out; // To send data to the server
    private String username; // Client's name
    private boolean isMyTurn = false; // Flag to track if it's this client's turn

    public WordChainClient(String address, int port) {
        try {
            this.socket = new Socket(address, port); // Connect to server
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Setup input stream
            this.out = new PrintWriter(socket.getOutputStream(), true); // Setup output stream
            Scanner scanner = new Scanner(System.in);

            // Ask for username
            System.out.print("Enter your username: ");
            username = scanner.nextLine();
            out.println(username); // Send to server

            // Start a thread to listen to server messages
            new Thread(() -> listen()).start();

            // Loop to send words when it's your turn
            while (true) {
                if (isMyTurn) {
                    System.out.print("Your turn! Enter a word: ");
                    String word = scanner.nextLine().trim().toLowerCase();
                    out.println(word); // Send word to server
                    isMyTurn = false; // Turn is done
                }
                Thread.sleep(500); // Sleep briefly to avoid CPU overuse
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Listen to incoming server messages
    public void listen() {
        String line;
        try {
            while ((line = in.readLine()) != null) {
                System.out.println(line); // Show message to user

                // If server tells you it's your turn
                if (line.contains("Your turn")) {
                    isMyTurn = true; // Allow input
                }
            }
        } catch (IOException e) {
            System.out.println("Connection lost.");
        }
    }

    // Main method to start the client
    public static void main(String[] args) {
        new WordChainClient("localhost", 5000); // Connect to server
    }
}
