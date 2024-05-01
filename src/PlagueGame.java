package src;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * This class instantiates a MineSweeper object, which is the model for the
 * game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 * <p>
 * This game adheres to a Model-View-Controller design framework.
 * <p>
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class PlagueGame extends JPanel {

    // Game constants
    public int BOARD_WIDTH;
    public int BOARD_HEIGHT;
    private Modeling model;
    private final JLabel status; // current status text
    BufferedImage topomap;

    List<CityNode> allCities;
    Graph graphObj;

    /**
     * Initializes the game board.
     */
    public PlagueGame(JLabel statusInit) {

        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        BufferedImage topomap1;
        topomap1 = null;
        try {
            topomap1 = ImageIO.read(new File("src/mapJPEG.jpg"));
        } catch ( IOException e) {
            System.out.println("Exception caught");
        }
        topomap = topomap1;
        BOARD_HEIGHT = topomap.getHeight();
        BOARD_WIDTH = topomap.getWidth();

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        Scraper scraper = new Scraper();
        allCities = scraper.returnCitiesList();
        Graph citiesModel = new Graph(allCities);
        graphObj = citiesModel;
        Infection infection = new Virus("Ashish");
        model = new Modeling(citiesModel, infection, "New York");

        status = statusInit; // initializes the status JLabel
        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();
                boolean isRight;
                isRight = e.getButton() != MouseEvent.BUTTON1;
                // updates the model given the coordinates of the mouseclick
                // m.play(ms, p.x / 50, p.y / 50, isRight);
                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state. Tells user to start playing the game
     */
    public void reset() {
        // Get the type of infection from the user
        String[] infectionTypes = {"Virus", "Bacteria", "Fungus"};
        String infectionType = (String) JOptionPane.showInputDialog(
                null,
                "Choose the type of infection:",
                "Select Infection Type",
                JOptionPane.QUESTION_MESSAGE,
                null,
                infectionTypes,
                infectionTypes[0]
        );

        // Check if user pressed cancel
        if (infectionType == null) return;

        // Get the name of the infection from the user
        String infectionName = JOptionPane.showInputDialog("Enter the name of the infection:");
        // Check if user pressed cancel
        if (infectionName == null) return;

        // Get the initial city to infect from the user
        // Assuming Scraper and returnCitiesList method provides a List of city names
        Scraper scraper = new Scraper();
        allCities = scraper.returnCitiesList();
        String[] cityNames = allCities.stream().map(CityNode::getName).toArray(String[]::new);

        String initialCity = (String) JOptionPane.showInputDialog(
                null,
                "Choose the initial city to infect:",
                "Select Initial City",
                JOptionPane.QUESTION_MESSAGE,
                null,
                cityNames,
                cityNames[0]
        );

        // Check if user pressed cancel
        if (initialCity == null) return;

        // Create the infection object based on the user's choice of infection type
        Infection infection;
        switch (infectionType) {
            case "Virus":
                infection = new Virus(infectionName);
                break;
            case "Bacteria":
                infection = new Bacteria(infectionName);
                break;
            case "Fungus":
                infection = new Fungus(infectionName);
                break;
            default:
                return; // in case of an unexpected input
        }

        // Initialize the graph model with the list of cities
        Graph citiesModel = new Graph(allCities);

        // Create the model with the selected city, infection, and model type
        model = new Modeling(citiesModel, infection, initialCity);

        // Update UI elements
        graphObj = citiesModel;

        status.setText("Start Playing!");
        repaint();
        requestFocusInWindow();
    }

    public void simulateYear() {
        Thread simulationThread = new Thread(() -> {
            long startTime = System.currentTimeMillis();
            long interval = 500; // 500 milliseconds

            for (int i = 0; i < 12; i++) {
                boolean outcome = model.simulateOneMonth();
                if (outcome) {
                    status.setText("Unfortunately you lost! Your plague has failed to reach " +
                            "critical mass");
                }
                final int month = i + 1;
                final int year = model.monthCount / 12;
                final int currentMonth = model.monthCount % 12;

                // Update the GUI on the Event Dispatch Thread
                SwingUtilities.invokeLater(() -> {
                    status.setText("Year: " + year + ", Month: " + currentMonth);
                    repaint();
                    requestFocusInWindow();
                });

                if (outcome) {
                    break; // Stop if the simulation outcome dictates
                }

                // Calculate the next time the loop should run
                long nextRunTime = startTime + month * interval;

                // Delay until the next run time
                while (System.currentTimeMillis() < nextRunTime) {
                    try {
                        Thread.sleep(Math.max(1, nextRunTime - System.currentTimeMillis()));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Properly handle interrupted exception
                        return;
                    }
                }
            }
            SwingUtilities.invokeLater(() -> {
                String plagueStatus = model.provideUpdate();
                JTextArea textArea = new JTextArea(plagueStatus);
                textArea.setEditable(false);
                textArea.setWrapStyleWord(true);
                textArea.setLineWrap(true);

                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(350, 150));
                JOptionPane.showMessageDialog(null, scrollPane, "Update", JOptionPane.INFORMATION_MESSAGE);

                // Determine the infection type and get upgrade options
                Infection currentInfection = model.getInfection();
                String[] upgradeOptions;
                String title = "Select an Upgrade";

                if (currentInfection instanceof Virus) {
                    upgradeOptions = new String[]{"Upgrade Mutation Rate", "Upgrade Host Dependency", "Upgrade Transmission Effectiveness"};
                } else if (currentInfection instanceof Bacteria) {
                    upgradeOptions = new String[]{"Upgrade Reproduction Rate", "Upgrade Resistance", "Upgrade Environmental Tolerance"};
                } else if (currentInfection instanceof Fungus) {
                    upgradeOptions = new String[]{"Upgrade Environmental Growth Rate", "Upgrade Spore Reproduction", "Upgrade Survivability"};
                } else {
                    return; // If the type is unknown, exit the method
                }

                String selectedUpgrade = (String) JOptionPane.showInputDialog(
                        null,
                        "Choose an upgrade for the year:",
                        title,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        upgradeOptions,
                        upgradeOptions[0]
                );

                // Check if user pressed cancel or closed the dialog
                if (selectedUpgrade == null) return;

                // Apply the selected upgrade based on the type and choice
                switch (selectedUpgrade) {
                    case "Upgrade Mutation Rate":
                    case "Upgrade Environmental Growth Rate":
                    case "Upgrade Reproduction Rate":
                        currentInfection.upgradeAttr1();
                        break;
                    case "Upgrade Host Dependency":
                    case "Upgrade Resistance":
                    case "Upgrade Spore Reproduction":
                        currentInfection.upgradeAttr2();
                        break;
                    case "Upgrade Transmission Effectiveness":
                    case "Upgrade Environmental Tolerance":
                    case "Upgrade Survivability":
                        currentInfection.upgradeAttr3();
                        break;
                }

            });
        });

        simulationThread.start(); // Start the simulation thread
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (model.gameOver && !model.userWon) {
            status.setText("You lost!");
        } else if (model.gameOver && model.userWon) {
            status.setText("You won!");
        }
    }

    /**
     * Draws the game board. If the game is over, printBoard is called. This
     * printBoard method
     * is not to be confused with the printBoard method which prints to the
     * terminal.
     * If the current cell is neither flagged nor is not clicked (i.e. the cell is
     * opened
     * and has a value associated with adjacent mines) a helper method is called to
     * print.
     *
     * @param g Graphics to print to the GUI
     */

    private void drawCity(Graphics2D g, CityNode city) {
        Point screenCoords = mapCoordinatesToScreen(city.latitude, city.longitude);
        int radius = (int) Math.sqrt(city.population) / 250;
        int redValue = (int) (255 * (city.percentInfected));
        if (city.cityName.equals("Chicago")) {
            System.out.println(redValue);
        }
        Color color = new Color(redValue, 0, 0);

        // Set color and draw the circle
        g.setColor(color);
        g.fillOval(screenCoords.x - radius, screenCoords.y - radius, radius * 2, radius * 2);
    }

    private void drawEdge(Graphics2D g, TransmissionEdge edge) {
        if (edge.flightTransmissionConstant > 0.506) {
            Point start = mapCoordinatesToScreen(edge.start.latitude, edge.start.longitude);
            Point control = mapCoordinatesToScreen(
                    (edge.start.latitude + edge.end.latitude)/2 + Math.random() * 4 - 2,
                    (edge.start.longitude + edge.end.longitude)/2 + Math.random() * 4 - 2
            );
            Point end = mapCoordinatesToScreen(edge.end.latitude, edge.end.longitude);
            QuadCurve2D q = new QuadCurve2D.Float();
            q.setCurve(start.x, start.y, control.x, control.y, end.x, end.y);

            Color semiTransparentWhite = new Color(255, 255, 255, 80);
            g.setColor(semiTransparentWhite);

            // Set the stroke as dashed
            float[] dash = {2, 2};
            BasicStroke bs = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, dash, 0);
            g.setStroke(bs);

            // Draw the curve
            g.draw(q);
        }

//        if (edge.landTransmissionConstant > 0.506) {
//            Point start = mapCoordinatesToScreen(edge.start.latitude, edge.start.longitude);
//            Point end = mapCoordinatesToScreen(edge.end.latitude, edge.end.longitude);
//            Color semiTransparentBrown = new Color(150, 75, 0, 15);
//            g.setColor(semiTransparentBrown);
//
//            float lineWidth = 0.1f;
//            BasicStroke stroke = new BasicStroke(lineWidth);
//            g.setStroke(stroke);
//
//            g.draw(new Line2D.Float(start.x, start.y, end.x, end.y));
//        }
    }
    private Point mapCoordinatesToScreen(double latitude, double longitude) {
        // Example conversion formula, adjust according to your map's coordinate system
        int x = (int) ((longitude * -10.12) + 1297.6);
        int y = (int) ((latitude * -14.27) + 728.16);
        return new Point(x, y);
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(topomap, 0, 0, null);
        g2.setFont(new Font("TimesRoman", Font.PLAIN, 20));

        HashMap<CityNode, ArrayList<TransmissionEdge>> tempAdjList = graphObj.getAdjList();
        HashSet<TransmissionEdge> seen = new HashSet<>();
        for (CityNode city : graphObj.getAdjList().keySet()) {
            for (TransmissionEdge e : tempAdjList.get(city)) {
                if (!seen.contains(e)) {
                    drawEdge(g2, e);
                    seen.add(e);
                }
            }
        }
        for (CityNode city : allCities) {
            drawCity(g2, city);
        }
    }

    /**
     * Draws the current cell to the GUI. Uses an algorithm to color-code the
     * different
     * adjacent mines values. Draws the string in approximately the center of the
     * cell
     *
     * @param g Graphics to print to the GUI
     * @param i x value of cell
     * @param j y value of cell
     */
//    private void helpPrintBoard(Graphics g, int i, int j) {
//        int numMines = ms[i][j].getMineNumber();
//        if (m.getStatus() == 2) {
//            g.setColor(Color.RED);
//        } else if (m.getStatus() == 1) {
//            g.setColor(new Color(51, 153, 255));
//        } else {
//            if (numMines == 0) {
//                g.setColor(new Color(51, 153, 255));
//            } else if (numMines == 1) {
//                g.setColor(new Color(0, 152, 51));
//            } else if (numMines == 2) {
//                g.setColor(new Color(76, 0, 153));
//            } else if (numMines == 3) {
//                g.setColor(new Color(0, 0, 204));
//            } else if (numMines == 4) {
//                g.setColor(new Color(204, 102, 0));
//            } else if (numMines == 5) {
//                g.setColor(Color.BLACK);
//            } else if (numMines == 6) {
//                g.setColor(Color.CYAN);
//            } else {
//                g.setColor(Color.RED);
//            }
//        }
//        g.drawString("" + numMines, i * 50 + 20, j * 50 + 30);
//        g.setColor(Color.BLACK);
//    }

    /**
     * Prints the entire gameBoard to the GUI screen with true values displayed.
     * This method is
     * executed when the game is over.
     *
     * @param g    Graphics to print to the GUI
     * @param game contains the GameBoard
     */
//    public void printBoard(Cell[][] game, Graphics g) {
//        for (int i = 0; i < 10; i++) {
//            for (int j = 0; j < 10; j++) {
//                if (game[i][j].getIsMine()) {
//                    g.setColor(Color.RED);
//                    g.drawOval(i * 50 + 20, j * 50 + 20, 15, 15);
//                    g.fillOval(i * 50 + 20, j * 50 + 20, 15, 15);
//                    g.setColor(Color.BLACK);
//                } else {
//                    helpPrintBoard(g, i, j);
//                }
//            }
//        }
//    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
