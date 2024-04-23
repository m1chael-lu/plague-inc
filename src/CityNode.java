public class CityNode {
    String cityName;
    int population;
    double latitude;
    double longitude;

    float proximityTransmissionConstant; // Will be determined by

    public CityNode(
            String cityName,
            int population,
            double latitude,
            double longitude
    ) {
        this.cityName = cityName;
        this.population = population;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
