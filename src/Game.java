package src;

import javax.swing.*;

public class Game {
    /**
     * Main method run to start and run the game.
     */
    public static void main(String[] args) {
        Runnable game = new src.Plague();
        SwingUtilities.invokeLater(game);
    }
}
