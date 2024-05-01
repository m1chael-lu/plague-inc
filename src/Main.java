package src;

import java.util.List;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scraper wikiScraper = new Scraper();
        List<CityNode> allCities = wikiScraper.returnCitiesList();
        Graph citiesModel = new Graph(allCities);
        Infection infection = new Fungus("Ashish");
        Modeling model = new Modeling(citiesModel, infection, "New York");

        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < 12; i++) {
            // Prompting
            //TODO: Will need to provide some sort of prompting, maybe some information asw
            System.out.println("Insert Prompt. Press Enter to Proceed");
            // User input handling
            String userInput = scanner.nextLine();

            // Iterating the model one step
            model.simulateOneMonth();
        }
    }
}
