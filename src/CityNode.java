package src;

public class CityNode {
    // webscraped information
    String cityName;
    int population;
    double landArea;
    double populationDensity;
    double latitude;
    double longitude;

    // calculated values based on the model for the infection
    double proximityTransmissionConstant;
    double percentInfected;
    int currentlyInfected;
    int totalRecovered;
    double percentRecovered;
    int totalKilled;

    // static parameters for the city model
    static double populationTransmissionFactor = 0.0000075;
    static double densityTransmissionFactor = 0.000075;
    static double areaTransmissionFactor = 0.005;

    /**
     *
     * @param cityName - the name of the city
     * @param population - the 2022 estimate of the population of the city
     * @param landArea - the total land area of the city as of 2020 (units: square miles)
     * @param latitude - the city's latitude
     * @param longitude - the city's longitude
     */
    public CityNode(
            String cityName,
            int population,
            double landArea,
            double latitude,
            double longitude
    ) {
        this.cityName = cityName;
        this.population = population;
        this.landArea = landArea;
        this.populationDensity = (double)population / landArea;
        this.latitude = latitude;
        this.longitude = longitude;
        this.percentInfected = 0.0;
        currentlyInfected = 0;
        totalRecovered = 0;
        percentRecovered = 0.0;

        // proximityTransmissionConstant is a measure of the transmission of the disease within the
        // city. We employ a logistic model to compute this value, we used the formula explained in
        // the write-up.

        recalculate();


    }

    public void recalculate() {
        this.percentRecovered = (double)totalRecovered / (double)population;
        this.percentInfected = ((double)currentlyInfected) / ((double)this.population);
        this.populationDensity = (double)population / landArea;
        this.proximityTransmissionConstant = 1.0 / (1.0 +
                Math.exp(-(populationTransmissionFactor * population * percentInfected
                        + densityTransmissionFactor * populationDensity
                        - areaTransmissionFactor * landArea)));
    }

    public String getName() {
        return cityName;
    }
}
