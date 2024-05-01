package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Implements the main setup and control flow for the Plague Simulator game, handling the
 * initialization and interaction of various GUI components.
 */
public class Plague implements Runnable {

    /**
     * Sets up and displays the main game and instructional components.
     */
    public void run() {

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Plague Simulator");
        frame.setLocation(450, 150);
        JFrame instructionsFrame = new JFrame("Plague Simulator Instructions");
        instructionsFrame.setSize(500, 500);
        instructionsFrame.setLocation(450, 100);
        instructionsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        // Example text with simple formatting for clarity
        String instructionsText = "PLAGUE SIMULATOR INSTRUCTIONS\n\n"
                + "STARTING THE SIMULATION\n"
                + "-----------------------\n"
                + "Select the type of infection (virus, bacteria, or fungus) and the name of the " +
                "infection. "
                + "Choose the initial city to infect. Click 'Start' to begin the simulation.\n\n"
                + "MONITORING THE SIMULATION\n"
                + "-------------------------\n"
                + "Watch the progress of the infection spread across different cities. "
                + "The simulator updates the infection status every month, showing how many " +
                "people are infected in each city, along with more data points to track your " +
                "progress in the game.\n\n"
                + "UPGRADING THE INFECTION\n"
                + "-----------------------\n"
                + "At the end of each year, you have the option to upgrade your infection's " +
                "capabilities. "
                + "Upgrade types are dependent on the infection type you select!\n\n"
                + "ENDING THE SIMULATION\n"
                + "---------------------\n"
                + "The simulation can be ended manually at any time or it will stop automatically" +
                " once the infection "
                + "reaches critical mass or medicine eliminates it.\n\n"
                + "WINNING THE GAME\n"
                + "----------------\n"
                + "You win the game by successfully reaching a critical mass in the" +
                " United States.\n\n"
                + "MISCELLANEOUS NOTES\n"
                + "-------------------\n"
                + "The outcome of the game depends heavily on your strategic planning in choosing" +
                " upgrades and managing "
                + "the spread of the infection.\n\n"
                + "Good luck!!\n";

        textArea.setText(instructionsText);
        JScrollPane scrollPane = new JScrollPane(textArea);

        instructionsFrame.add(scrollPane);
        instructionsFrame.setVisible(true);

        JButton startGameButton = new JButton("Start Game");

        // Adding Start Game button to instructions frame
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startGameButton);
        instructionsFrame.add(buttonPanel, BorderLayout.SOUTH);

        instructionsFrame.pack();
        instructionsFrame.setVisible(true);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Game board
        final PlagueGame simulator = new PlagueGame(status);

        frame.add(simulator);
        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulator.reset();
            }
        });
        control_panel.add(reset);

        final JButton simulateYear = new JButton("Simulate Year");
        simulateYear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!simulator.isGameOver()) {
                    simulator.simulateYear();
                }
            }
        });
        control_panel.add(simulateYear);

        // instructionsButton creation and inclusion in the control panel
        final JButton instructionsButton = new JButton("Instructions");
        instructionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                instructionsFrame.setVisible(!instructionsFrame.isVisible());
                return;
            }
        });
        control_panel.add(instructionsButton);

        final JButton statsDescription = new JButton("Infection Stats");
        JFrame statsFrame = new JFrame("Infection Stats");
        statsFrame.setSize(400, 200);
        statsFrame.setLocation(550, 200);

        JTextArea textAreaStats = new JTextArea();
        textAreaStats.setEditable(false);
        textAreaStats.setLineWrap(true);
        textAreaStats.setWrapStyleWord(true);

        JScrollPane scrollPaneStats = new JScrollPane(textAreaStats);

        statsFrame.add(scrollPaneStats);

        statsDescription.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String stats = simulator.getStats();
                textAreaStats.setText(stats);
                statsFrame.setVisible(!statsFrame.isVisible());
                return;
            }
        });

        control_panel.add(statsDescription);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(false);

        // Start the game
        startGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                instructionsFrame.dispose();
                instructionsFrame.remove(buttonPanel);
                frame.setVisible(true);
                simulator.reset();
            }
        });
    }
}
