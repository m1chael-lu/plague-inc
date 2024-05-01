package src;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Scraper class for extracting city data from a Wikipedia page.
 * This class connects to Wikipedia, extracts city information, and constructs a list of CityNode
 * objects.
 */
public class Scraper {
    Document wikiPage;
    List<CityNode> allCities;

    /**
     * Constructor for Scraper. Initializes the list of all cities and starts the scraping process.
     */
    public Scraper() {
        allCities = new ArrayList<>();
        try {
            wikiPage = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_United_States" +
                    "_cities_by_population").get();
            Elements tables = wikiPage.select("table");
            Element targetTable = null;
            if (tables.isEmpty()) {
                System.out.println("No tables found.");
            } else {
                for (Element table : tables) {
                    if (table.text().contains("2020 census")) {
                        targetTable = table;
                        break;
                    }
                }
            }
            if (targetTable == null) {
                return;
            }
            Element container = targetTable.select("tbody").first();
            if (container == null) {
                return;
            }
            Elements rows = container.select("tr");
            for (Element row : rows) {
                Elements rowContents = row.select("td");
                if (rowContents.isEmpty()) {
                    continue;
                }
                String cityName = rowContents.get(0).select("a").first().text();
                String populationString = rowContents.get(2).text().
                        replaceAll(",", "").replaceAll(" ", "");
                int population = Integer.parseInt(populationString);
                String landAreaString = rowContents.get(5).text().
                        replaceAll(",", "").replaceAll(" ", "");
                double landArea = Double.parseDouble(landAreaString);
                String[] coordinatesSplit = rowContents.get(9).text().split(" / ");
                String correctCoordinates = coordinatesSplit[1];
                String[] correctCoordinatesSplit = correctCoordinates.split(" ");
                String latitudeString = correctCoordinatesSplit[0].
                        replaceAll("\\D", "");
                String longitudeString = correctCoordinatesSplit[1].
                        replaceAll("\\D", "");
                latitudeString = latitudeString.substring(0, latitudeString.length() - 2) + "." +
                        latitudeString.substring(latitudeString.length() - 2);
                longitudeString = longitudeString.substring(0, longitudeString.length() - 2) + "."
                        + longitudeString.substring(longitudeString.length() - 2);
                double latitude = Double.parseDouble(latitudeString);
                double longitude = Double.parseDouble(longitudeString);
                CityNode currCity = new CityNode(cityName, population, landArea, latitude,
                        longitude);
                allCities.add(currCity);
            }
        } catch (IOException e) {
            System.out.println("error caught in web scraping");
        }
    }

    /**
     * Returns the list of all cities that have been scraped.
     *
     * @return A list of CityNode objects containing city data.
     */
    public List<CityNode> returnCitiesList() {
        return allCities;
    }
}
