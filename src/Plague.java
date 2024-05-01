package src;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class Plague implements Runnable {
    public void run() {

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Plague Simulator");
        frame.setLocation(450, 150);

        // Frame for Instructions
        JFrame instructionsPane = new JFrame("Plague Instructions");
        instructionsPane.setSize(475, 480);
        instructionsPane.setLocation(466, 200);


        // TODO: Need to set the instructions text
        JEditorPane instructionsText = new JEditorPane();
        instructionsText.setEditable(false);
        instructionsText.setContentType("text/html");
        String text = "<h1 style=\"text-align:center\">MineSweeper Instructions</h1>\n" +
                "<h2 style=\"text-align:center\">Selecting a Cell</h2>\n" +
                "<p>Click a cell at random to start the game. The cells that have a mine number" +
                " indicate the number of mines directly surrounding the cell (there are 8 total" +
                " cells that surround each cell). Based on these values, open empty cells " +
                "strategically to not hit a mine. If you hit a mine, you lose the game.</p>\n" +
                "<h2 style=\"text-align:center\">Flagging a Cell</h2>\n<p> Flag a cell if you" +
                " suspect that it is a mine. You can flag by right clicking on the cell.</p>\n" +
                "<h2 style=\"text-align:center\">Winning the Game</h2>\n<p>You win the game by " +
                "opening all the empty cells and not touching any of the mines. You do not need" +
                " to have mine cells flagged in order to win. There are between 12 to 15 mines" +
                " to find!</p>\n<h2 style=\"text-align:center\">Misc. Notes</h2>\n" +
                "<p>You can't flag an opened cell. You can't open a flagged cell.</p>\n" +
                "<h2 style=\"text-align:center\">Good luck!!</h2>\n" +
                "\n";
        instructionsText.setText(text);
        instructionsPane.setContentPane(instructionsText);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Game board
        final PlagueGame simulator = new PlagueGame(status);
        simulator.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Print the x, y coordinates of the mouse click
                System.out.println("Mouse clicked at: (" + e.getX() + ", " + e.getY() + ")");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // Not used in this example
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Not used in this example
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Not used in this example
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Not used in this example
            }
        });

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
                simulator.simulateYear();
            }
        });
        control_panel.add(simulateYear);

        // instructionsButton creation and inclusion in the control panel
        final JButton instructionsButton = new JButton("Instructions");

        instructionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                instructionsPane.setVisible(!instructionsPane.isVisible());
            }
        });
        control_panel.add(instructionsButton);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        simulator.reset();
    }
}