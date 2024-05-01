package src;

public class TransmissionEdge {
    CityNode start;
    CityNode end;
    double flightTransmissionConstant; // Will be determined by combined score of population sizes
    double landTransmissionConstant; // Will be determined by distance between cities
    double distanceBetweenCities; // units in miles

    static final double EARTH_RADIUS = 3956.0;
    static double flightPopulationInfectedConstant = 0.000000075;
    static double proximityFactorConstant = 0.25;
    static double landPopulationInfectedConstant = flightPopulationInfectedConstant;

    public TransmissionEdge(CityNode start, CityNode end) {
        distanceBetweenCities = haversineDistance(start.latitude, start.longitude,
                end.latitude, end.longitude);
        this.start = start;
        this.end = end;

        recalculate();
    }

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
                        + proximityFactorConstant * (startProximityConstant + endProximityConstant) *
                        (1.0 / (Math.pow(distanceBetweenCities, (0.5))) * flightAdjustment
                        ))));

        landTransmissionConstant = 1.0 / (1.0 + Math.exp(-(
                landPopulationInfectedConstant * (startInfectedPercent * startPopulation +
                        endInfectedPercent * endPopulation)
                        + proximityFactorConstant * (startProximityConstant + endProximityConstant) *
                        (1.0 / (Math.pow(distanceBetweenCities, (0.6)))
                        ))));
    }

    public CityNode getStart() {
        return start;
    }

    public CityNode getEnd() {
        return end;
    }

    public double getFlightTransmissionConstant() {
        return flightTransmissionConstant;
    }

    public double getLandTransmissionConstant() {
        return landTransmissionConstant;
    }
}
