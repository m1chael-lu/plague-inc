package src;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class GUI extends Application {
    private static List<CityNode> cities;

    public GUI() {
        super();
    }

    public static void setCityList(List<CityNode> cityList) {
        cities = cityList;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();
        Image mapImage = new Image("/public/USTopological.png");
        ImageView mapView = new ImageView(mapImage);
        mapView.setFitWidth(921);
        mapView.setFitHeight(768);
        root.getChildren().add(mapView);

        for (CityNode city : cities) {
            Circle cityCircle = createCityCircle(city);
            root.getChildren().add(cityCircle);
        }

        Scene scene = new Scene(root, 921, 768);
        primaryStage.setTitle("US Topological Map with Cities");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Circle createCityCircle(CityNode city) {
        double[] screenCoords = mapCoordinatesToScreen(city.retrieveLatitude(), city.retrieveLongitude());
        double radius = Math.sqrt(city.retrievePopulation()) / 300; // Scale population to a reasonable size
        double infectionRate = city.retrievePercentInfected();
        Color color = Color.WHITE.interpolate(Color.RED, infectionRate / 100.0);
        Circle circle = new Circle(screenCoords[0], screenCoords[1], radius, color);
        return circle;
    }

    private double[] mapCoordinatesToScreen(double latitude, double longitude) {
        // Placeholder: convert latitude and longitude to x, y coordinates
        double x = (longitude + 180) * (1000/ 360.0); // Example conversion
        double y = (90 - latitude) * (1700 / 180.0);   // Example conversion
        return new double[]{x, y};
    }

    public static void main(String[] args) {
        launch(args);
    }
}

