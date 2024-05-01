package src;

import javax.swing.*;

/**
 * Main class to launch the game using a Swing-based framework.
 */
public class Game {

    /**
     * The main method that starts the game. It creates an instance of the game
     * and schedules it to be run on the Swing event dispatch thread.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        Runnable game = new src.Plague();
        SwingUtilities.invokeLater(game);
    }
}
