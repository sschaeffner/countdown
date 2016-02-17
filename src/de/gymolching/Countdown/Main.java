package de.gymolching.Countdown;

/**
 * @author sschaeffner
 */
public class Main {

    public static void main(String[] args) {
        printHelp();
        new CountdownController(600, 0, "Source Code Pro");
    }

    /**
     * Prints a help with all the keys to the console.
     */
    public static void printHelp() {
        System.out.println("s - start:    starts the countdown");
        System.out.println("k - kill:     stops the countdown");
        System.out.println("b - blackout: blacks out the screen");
        System.out.println("w - whiteout: whites out the screen");
        System.out.println("Q - quit:     quits the program");
    }
}
