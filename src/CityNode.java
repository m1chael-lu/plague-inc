package src;

public class CityNode {
    String cityName;
    int population;
    double landArea;
    double populationDensity;
    double latitude;
    double longitude;
    double proximityTransmissionConstant;
    double percentInfected;

    static double populationTransmissionFactor = 0.0000075;
    static double densityTransmissionFactor = 0.000075;
    static double areaTransmissionFactor = 0.005;

    /**
     *
     * @param cityName - the name of the city
     * @param population - the 2022 estimate of the population of the city
     * @param landArea - the total land area of the city as of 2020 (units: square miles)
     * @param populationDensity - the city's population density as of 2020 (population / area)
     * @param latitude - the city's latitude
     * @param longitude - the city's longitude
     */
    public CityNode(
            String cityName,
            int population,
            double landArea,
            double populationDensity,
            double latitude,
            double longitude
    ) {
        this.cityName = cityName;
        this.population = population;
        this.landArea = landArea;
        this.populationDensity = populationDensity;
        this.latitude = latitude;
        this.longitude = longitude;
        this.percentInfected = 0.0;

        // proximityTransmissionConstant is a measure of the transmission of the disease within the
        // city. We employ a logistic model to compute this value, we used the formula explained in
        // the write-up.

        recalculate();


    }

    public void recalculate() {
        this.proximityTransmissionConstant = 1.0 / (1.0 +
                Math.exp(-(populationTransmissionFactor * population * percentInfected
                        + densityTransmissionFactor * populationDensity
                        - areaTransmissionFactor * landArea)));
    }
}
