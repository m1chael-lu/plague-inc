package src;

import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Scraper wikiScraper = new Scraper();
        List<CityNode> allCities = wikiScraper.returnCitiesList();
        Graph citiesModel = new Graph(allCities);
        Infection infection = new Virus("Ashish");
        Modeling model = new Modeling(citiesModel, infection, "New York");
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < 24; i++) {
            // Prompting
            // TODO: Need to incorporate some type of evolution/status prompts before each user input
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
