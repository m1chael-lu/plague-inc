package src;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Scraper wikiScraper = new Scraper();
        List<CityNode> allCities = wikiScraper.returnCitiesList();
        Graph citiesModel = new Graph(allCities);
        Infection infection = new Virus("Ashish");
        Modeling model = new Modeling(citiesModel, infection, "New York");
        for (int i = 0; i < 24; i++) {
            boolean outcome = model.simulateOneMonth();
            if (outcome) {
                break;
            }
        }
    }
}
