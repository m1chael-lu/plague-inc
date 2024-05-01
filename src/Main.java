package src;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Scraper wikiScraper = new Scraper();
        List<CityNode> allCities = wikiScraper.returnCitiesList();
        Graph citiesModel = new Graph(allCities);
        Infection infection = new Infection("Ashish", 0.9, 0.5, 0.6);
        Modeling model = new Modeling(citiesModel, infection, "New York");
        for (int i = 0; i < 12; i++) {
            model.simulateOneMonth();
        }
    }
}
