package src;

import java.util.List;
import java.util.Scanner;

/**
 * This is the tester class for simulating an infection spread across a network of cities.
 * It initializes components like the scraper for city data, infection model, and the graph model of
 * cities. The simulation runs a monthly cycle, influenced by user input, to demonstrate how
 * infections spread and are managed.
 */
public class Main {
    public static void main(String[] args) {
        Scraper wikiScraper = new Scraper();
        List<CityNode> allCities = wikiScraper.returnCitiesList();
        Graph citiesModel = new Graph(allCities);
        Infection infection = new Virus("Ashish");
        Modeling model = new Modeling(citiesModel, infection, "New York");
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < 24; i++) {
            System.out.println("Filler prompt, Press Enter to continue");

            // User Input
            String userInput = scanner.nextLine();
            boolean outcome = model.simulateOneMonth();
            if (outcome) {
                break;
            }
        }
    }
}
