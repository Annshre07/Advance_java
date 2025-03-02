import java.util.Random;
import java.util.Scanner;
//Interface for Random Number Generator which generates the number between 1-100.
interface RandomNumberGenerator {
   int generate(int min, int max);

}
//Concrete implementation of Random Number Generator which generates random number.
class DefaultRandomNumberGenerator implements RandomNumberGenerator {
    @Override
    public int generate(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
    
}
//Interface for User Input Handler user input.
interface InputHandler {
    int getUserGuess();
}
//Concrete Implementation for User Input Handling
class ConsoleInputHandler implements InputHandler {
    @Override
        public int getUserGuess() {
        System.out.println("Lu ta  Number hala ta!!");
        Scanner sc=new Scanner(System.in);
        int guess=sc.nextInt();
        return(guess);
       
    }
}
//Interface for Game Rules.
interface GameRules {
    boolean checkGuess(int guess, int target);
}
//Concrete Implementation of Game Rules (SRP)
class DefaultGameRules implements GameRules {
    @Override
        public  boolean checkGuess(int guess, int target) {
        if (guess == target) 
        {
            System.err.println("Dammi hw sahi jawab dyew...");
            return true;
        }
        else if (guess > target)
        {
            System.out.println("Sorry la jyadaii va!!");
        }
        else
        {
            System.out.println("Sorry la kaamti va!!");
        }
        return false;
    }
}

//Scoring System Interface
interface ScoringSystem {
    void updateScore(int attempts);
    int getScore();
}
//Concrete Implementation for Scoring System
class DefaultScoringSystem implements ScoringSystem {
    private int score=0;
    @Override
    public void updateScore(int attempts) {
        score=(attempts%10)*(attempts+(attempts/10));
    }
    @Override
    public int getScore() {
        return(score);
    }
}
//Game Engine to manage game flow
class NumberGuessingGame {
    RandomNumberGenerator rng;
    InputHandler inputHandler;
    GameRules rules;
    ScoringSystem scoring;

    NumberGuessingGame(RandomNumberGenerator rng, InputHandler inputHandler, GameRules rules, ScoringSystem scoring) {
        this.rng = rng;
        this.inputHandler = inputHandler;
        this.rules = rules;
        this.scoring = scoring;
    }

   void  startGame() {
    Scanner sc =new Scanner(System.in);
        int max_attempts = 3;
        while (true)
        {
            int target = rng.generate(1, 100);
            int attempts = 0;
            boolean guessedCorrectly = false;

            while (attempts < max_attempts && !guessedCorrectly)
            {
                int guess = inputHandler.getUserGuess();
                guessedCorrectly = rules.checkGuess(guess, target);
                attempts++;

            if (guessedCorrectly)
            {
                System.out.println("Jeetyo hai ta lu!!!");
                scoring.updateScore(attempts);
            }
            
            }
            if (!guessedCorrectly) {
                System.out.println("Game over! Sorry la number chai  " + target+" theo!!!");
            }
           
            System.out.println("Feri Khelnay??(yes/no)");
            String playAgain = sc.next();
            System.out.println("Timro score: " + scoring.getScore());
            if (playAgain.equalsIgnoreCase("no")) {
                break;
            }
    }
        }
}
//Main Class to Run the Game
class Task_1{
    public static void main(String[] args) {
        RandomNumberGenerator rng = new DefaultRandomNumberGenerator();
        InputHandler inputHandler = new ConsoleInputHandler();
        GameRules rules = new DefaultGameRules();
        ScoringSystem scoring = new DefaultScoringSystem();
        NumberGuessingGame game = new NumberGuessingGame(rng, inputHandler, rules, scoring);
        game.startGame();
    }
}
