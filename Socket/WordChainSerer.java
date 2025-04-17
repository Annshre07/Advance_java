import java.io.*; // Importing classes for input and output
import java.net.*; // Importing classes for networking (sockets, server)
import java.util.*; // Importing utility classes like ArrayList
import java.util.concurrent.*; // For handling multiple clients concurrently

public class WordChainServer {

    private ServerSocket serverSocket; // This is the server's socket that listens for incoming client connections
    private ExecutorService pool; // Thread pool to manage multiple clients efficiently
    private List<ClientHandler> clients; // List to keep track of connected clients
    private List<String> usedWords; // Stores words already used in the game
    private volatile boolean isGameRunning = false; // Flag to check if the game is in progress
    private int currentTurnIndex = 0; // Keeps track of whose turn it is

    public WordChainServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.pool = Executors.newCachedThreadPool(); // Creates a thread pool that grows as needed
        this.clients = new ArrayList<>();
        this.usedWords = new ArrayList<>();
    }

    public void start() throws IOException {
        System.out.println("Word Chain Server is running...");
        
        // Ask for the first word to start the game
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the starting word: ");
        String startingWord = scanner.nextLine().trim().toLowerCase();
        usedWords.add(startingWord); // Add starting word to used list

        while (!serverSocket.isClosed()) {
            Socket socket = serverSocket.accept(); // Accept a new client connection
            System.out.println("A new client connected!");

            // Create handler for new client
            ClientHandler clientHandler = new ClientHandler(socket, this);
            clients.add(clientHandler); // Add client to the list
            pool.execute(clientHandler); // Run client in a new thread

            // Start game if more than 1 player
            if (clients.size() >= 2 && !isGameRunning) {
                isGameRunning = true;
                clients.get(currentTurnIndex).setTurn(true); // Give first player the turn
                broadcast("Game started! Starting word: " + startingWord);
            }
        }
    }

    // Method to broadcast messages to all players
    public void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    // Check if a word is valid: new, starts with last letter, exists in dictionary
    public boolean isValidWord(String word, String lastWord) {
        if (usedWords.contains(word)) return false; // Word already used
        if (!word.startsWith(Character.toString(lastWord.charAt(lastWord.length() - 1)))) return false; // Doesn't start with required letter
        return new File("dictionary.txt").exists() && checkDictionary(word); // Check if word is real
    }

    // Read the dictionary file and check if the word exists
    private boolean checkDictionary(String word) {
        try (BufferedReader reader = new BufferedReader(new FileReader("dictionary.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equalsIgnoreCase(word)) return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Process a word played by a client
    public void processTurn(String word, ClientHandler player) {
        if (!isGameRunning) return;

        String lastWord = usedWords.get(usedWords.size() - 1); // Get last word in chain

        // If word is valid, add to used list
        if (isValidWord(word, lastWord)) {
            usedWords.add(word);
            broadcast(player.getUsername() + " played: " + word);
            nextTurn();
        } else {
            broadcast(player.getUsername() + " gave an invalid word and is eliminated.");
            eliminatePlayer(player);
        }
    }

    // Give the next player the turn
    private void nextTurn() {
        if (clients.size() <= 1) {
            broadcast("Game Over! Winner: " + clients.get(0).getUsername());
            isGameRunning = false;
            return;
        }

        clients.get(currentTurnIndex).setTurn(false); // Remove turn from current player

        // Move to next player
        currentTurnIndex = (currentTurnIndex + 1) % clients.size();
        clients.get(currentTurnIndex).setTurn(true); // Give turn to next player
    }

    // Remove a player from the game
    public void eliminatePlayer(ClientHandler player) {
        player.setTurn(false);
        clients.remove(player); // Remove from list
        try {
            player.closeConnection(); // Close connection
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (clients.size() == 1) {
            broadcast("Game Over! Winner: " + clients.get(0).getUsername());
            isGameRunning = false;
        } else {
            nextTurn();
        }
    }

    // Main method to start the server
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            WordChainServer server = new WordChainServer(serverSocket);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
