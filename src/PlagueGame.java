package src;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


@SuppressWarnings("serial")
public class PlagueGame extends JPanel {

    private final JLabel status; // current status text
    // Game constants
    public int BOARD_WIDTH;
    public int BOARD_HEIGHT;
    BufferedImage topomap;
    List<CityNode> allCities;
    List<CityNode> originalCities;
    HashMap<String, Integer> originalPopulations;
    Graph graphObj;
    private Modeling model;


    public PlagueGame(JLabel statusInit) {

        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        BufferedImage topomap1;
        topomap1 = null;
        try {
            topomap1 = ImageIO.read(new File("src/mapJPEG.jpg"));
        } catch (IOException e) {
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
        List<CityNode> originalCities = scraper.returnCitiesList();
        originalPopulations = new HashMap<>();
        for (CityNode c : originalCities) {
            originalPopulations.put(c.cityName, c.population);
        }
        Graph citiesModel = new Graph(allCities);
        graphObj = citiesModel;
        Infection infection = new Virus("Ashish");
        model = new Modeling(citiesModel, infection, "New York");

        status = statusInit; // initializes the status JLabel
    }

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
        if (model.gameOver) {
            SwingUtilities.invokeLater(this::updateStatus);
            return;
        }
        Thread simulationThread = new Thread(() -> {
            long startTime = System.currentTimeMillis();
            long interval = 500; // 500 milliseconds

            for (int i = 0; i < 12; i++) {
                boolean outcome = model.simulateOneMonth();
                if (outcome) {
                    SwingUtilities.invokeLater(this::updateStatus);
                    return;
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
            if (model.gameOver) {
                SwingUtilities.invokeLater(this::updateStatus);
            } else {
                SwingUtilities.invokeLater(() -> {
                    String plagueStatus = model.provideUpdate();
                    JTextArea textArea = new JTextArea(plagueStatus);
                    textArea.setEditable(false);
                    textArea.setWrapStyleWord(true);
                    textArea.setLineWrap(true);

                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(350, 150));
                    JOptionPane.showMessageDialog(null, scrollPane, "Update",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Determine the infection type and get upgrade options
                    Infection currentInfection = model.getInfection();
                    String[] upgradeOptions;
                    String title = "Select an Upgrade";

                    if (currentInfection instanceof Virus) {
                        upgradeOptions = new String[]{"Upgrade Mutation Rate", "Upgrade Host " +
                                "Dependency", "Upgrade Transmission Effectiveness"};
                    } else if (currentInfection instanceof Bacteria) {
                        upgradeOptions = new String[]{"Upgrade Reproduction Rate", "Upgrade " +
                                "Resistance", "Upgrade Environmental Tolerance"};
                    } else if (currentInfection instanceof Fungus) {
                        upgradeOptions = new String[]{"Upgrade Environmental Growth Rate",
                                "Upgrade Spore Reproduction", "Upgrade Survivability"};
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
            }
        });

        simulationThread.start(); // Start the simulation thread
    }

    private void updateStatus() {
        if (model.gameOver && !model.userWon) {
            status.setText("You lost! Your disease has been eradicated! Press reset to play " +
                    "again.");
        } else if (model.gameOver && model.userWon) {
            status.setText("You won! Your disease has reached critical mass! Press reset to " +
                    "play again.");
        }
        repaint();
    }


    private void drawCity(Graphics2D g, CityNode city) {
        Point screenCoords = mapCoordinatesToScreen(city.latitude, city.longitude);
        int radius = (int) Math.sqrt(city.population) / 250;
        int redValue = (int) (255 * (city.percentInfected + city.totalKilled / originalPopulations.
                get(city.cityName)));
        if (redValue > 255) {
            redValue = 255;
        }
        if (redValue < 0) {
            redValue = 0;
        }
        Color color = new Color(redValue, 0, 0);
        g.setColor(color);
        g.fillOval(screenCoords.x - radius, screenCoords.y - radius, radius * 2,
                radius * 2);
    }

    private void drawEdge(Graphics2D g, TransmissionEdge edge) {
        if (edge.flightTransmissionConstant > 0.506) {
            Point start = mapCoordinatesToScreen(edge.start.latitude, edge.start.longitude);
            Point control = mapCoordinatesToScreen(
                    (edge.start.latitude + edge.end.latitude) / 2 + Math.random() * 4 - 2,
                    (edge.start.longitude + edge.end.longitude) / 2 + Math.random() * 4 - 2
            );
            Point end = mapCoordinatesToScreen(edge.end.latitude, edge.end.longitude);
            QuadCurve2D q = new QuadCurve2D.Float();
            q.setCurve(start.x, start.y, control.x, control.y, end.x, end.y);

            Color semiTransparentWhite = new Color(255, 255, 255, 80);
            g.setColor(semiTransparentWhite);
            float[] dash = {2, 2};
            BasicStroke bs = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                    0, dash, 0);
            g.setStroke(bs);
            g.draw(q);
        }
    }

    private Point mapCoordinatesToScreen(double latitude, double longitude) {
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

    public boolean isGameOver() {
        return model.gameOver;
    }

    public String getStats() {
        return model.infection.getStats();
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
