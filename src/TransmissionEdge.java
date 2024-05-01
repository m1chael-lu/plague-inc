package src;

/**
 * Represents a connection or edge between two cities in terms of infection transmission.
 * This class computes and stores transmission constants for both flight and land-based transmission
 * based on the distance between cities and the infection statistics of each city.
 */
public class TransmissionEdge {
    static final double EARTH_RADIUS = 3956.0;
    static double flightPopulationInfectedConstant = 0.000000075;
    static double proximityFactorConstant = 0.25;
    static double landPopulationInfectedConstant = flightPopulationInfectedConstant;
    CityNode start;
    CityNode end;
    double flightTransmissionConstant;
    double landTransmissionConstant;
    double distanceBetweenCities;

    /**
     * Constructor initializes a transmission edge between two cities and calculates the initial
     * transmission constants.
     *
     * @param start The starting city node.
     * @param end   The ending city node.
     */
    public TransmissionEdge(CityNode start, CityNode end) {
        distanceBetweenCities = haversineDistance(start.latitude, start.longitude,
                end.latitude, end.longitude);
        this.start = start;
        this.end = end;

        recalculate();
    }

    /**
     * Calculates the geographic distance between two points using the Haversine formula.
     *
     * @param lat1 Latitude of the first point.
     * @param lon1 Longitude of the first point.
     * @param lat2 Latitude of the second point.
     * @param lon2 Longitude of the second point.
     * @return The distance between the two points in miles.
     */
    public static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);
        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;
        double a = Math.pow(Math.sin(deltaLat / 2), 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.pow(Math.sin(deltaLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    /**
     * Recalculates the transmission constants based on the current infection data of the start and
     * end cities.
     */
    public void recalculate() {
        double startProximityConstant = start.proximityTransmissionConstant;
        double endProximityConstant = end.proximityTransmissionConstant;

        double startInfectedPercent = start.percentInfected;
        double startPopulation = start.population;

        double endInfectedPercent = end.percentInfected;
        double endPopulation = end.population;

        double flightAdjustment = 1.0;
        if (distanceBetweenCities < 200.0) {
            flightAdjustment = 0.0;
        }

        flightTransmissionConstant = 1.0 / (1.0 + Math.exp(-(
                flightPopulationInfectedConstant * (startInfectedPercent * startPopulation +
                        endInfectedPercent * endPopulation)
                        + proximityFactorConstant * (startProximityConstant + endProximityConstant)
                        * (1.0 / (Math.pow(distanceBetweenCities, (0.5))) * flightAdjustment))));

        landTransmissionConstant = 1.0 / (1.0 + Math.exp(-(
                landPopulationInfectedConstant * (startInfectedPercent * startPopulation +
                        endInfectedPercent * endPopulation)
                        + proximityFactorConstant * (startProximityConstant + endProximityConstant)
                        * (1.0 / (Math.pow(distanceBetweenCities, (0.6)))))));
    }

    /**
     * Getter method for start of the edge
     *
     * @return start node
     */
    public CityNode getStart() {
        return start;
    }

    /**
     * Getter method for end of the edge
     *
     * @return end node
     */
    public CityNode getEnd() {
        return end;
    }

    /**
     * Getter method for flight transmission constant
     *
     * @return flight transmission constant
     */
    public double getFlightTransmissionConstant() {
        return flightTransmissionConstant;
    }

    /**
     * Getter method for land transmission constant
     *
     * @return land transmission constant
     */
    public double getLandTransmissionConstant() {
        return landTransmissionConstant;
    }
}
