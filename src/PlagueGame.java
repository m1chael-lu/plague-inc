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
        Scraper scraper = new Scraper();
        allCities = scraper.returnCitiesList();
        Graph citiesModel = new Graph(allCities);
        graphObj = citiesModel;
        Infection infection = new Virus("Ashish");
        model = new Modeling(citiesModel, infection, "New York");
        status.setText("Start Playing!");
        repaint();
        requestFocusInWindow();
    }

    public void simulateYear() {
        boolean outcome = model.simulateOneMonth();
        repaint();
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
        // Convert latitude and longitude to x, y coordinates
        Point screenCoords = mapCoordinatesToScreen(city.latitude, city.longitude);
        // Calculate radius based on population
        int radius = (int) Math.sqrt(city.population) / 250;
        // Determine color based on % infected (0% white, 100% red)
        int redValue = (int) (255 * (city.percentInfected));
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
        // Draws board grid

        for (int i = 0; i < 10; i++) {
            g2.drawLine(0, i * 50, BOARD_WIDTH, i * 50);
            g2.drawLine(i * 50, 0, i * 50, BOARD_HEIGHT);
        }


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
