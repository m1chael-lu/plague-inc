package src;

/**
 * Represents a city node in a disease spread simulation model.
 * Stores both raw data and derived information concerning a city's population,
 * geographical area, and disease transmission characteristics.
 */
public class CityNode {
    static double populationTransmissionFactor = 0.0000075;
    static double densityTransmissionFactor = 0.000075;
    static double areaTransmissionFactor = 0.005;
    String cityName;
    int population;
    double landArea;
    double populationDensity;
    double latitude;
    double longitude;
    double proximityTransmissionConstant;
    double percentInfected;
    int currentlyInfected;
    int totalRecovered;
    double percentRecovered;
    int totalKilled;

    /**
     * Constructs a CityNode with specified parameters, calculates population density,
     * and initializes disease-related metrics.
     *
     * @param cityName   The name of the city.
     * @param population The estimated population of the city for the year 2022.
     * @param landArea   The total land area of the city in square miles as of 2020.
     * @param latitude   The geographical latitude of the city.
     * @param longitude  The geographical longitude of the city.
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
        this.populationDensity = population / landArea;
        this.latitude = latitude;
        this.longitude = longitude;
        this.percentInfected = 0.0;
        currentlyInfected = 0;
        totalRecovered = 0;
        percentRecovered = 0.0;
        recalculate();
    }

    /**
     * Recalculates the derived metrics related to disease transmission based on the current state
     * of the city's population and disease progression.
     */
    public void recalculate() {
        this.percentRecovered = (double) totalRecovered / (double) population;
        this.percentInfected = ((double) currentlyInfected) / ((double) this.population);
        this.populationDensity = (double) population / landArea;
        this.proximityTransmissionConstant = 1.0 / (1.0 +
                Math.exp(-(populationTransmissionFactor * population * percentInfected
                        + densityTransmissionFactor * populationDensity
                        - areaTransmissionFactor * landArea)));
    }

    /**
     * Returns the name of the city.
     *
     * @return The name of the city.
     */
    public String getName() {
        return cityName;
    }
}
