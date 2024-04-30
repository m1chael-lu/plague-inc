package src;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.*;
import java.io.IOException;

public class Scraper {
    Document wikiPage;

    List<CityNode> allCities;

    public Scraper() {
        allCities = new ArrayList<>();
        try {
            wikiPage = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_United_States" +
                    "_cities_by_population").get();
            Elements tables = wikiPage.select("table");

            Element targetTable = null;

            if (tables.isEmpty()) {
                System.out.println("No tables found.");
            }
            else {
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
                        replaceAll(",", "").replaceAll(" ","");
                int population = Integer.parseInt(populationString);
                String landAreaString = rowContents.get(5).text().
                        replaceAll(",", "").replaceAll(" ","");
                double landArea = Double.parseDouble(landAreaString);
                String populationDensityString = rowContents.get(7).text().
                        replaceAll(",", "").replaceAll(" ","");
                double populationDensity = Double.parseDouble(populationDensityString);
                String[] coordinatesSplit = rowContents.get(9).text().split(" / ");
                String correctCoordinates = coordinatesSplit[1];
                String[] correctCoordinatesSplit = correctCoordinates.split(" ");
                String latitudeString = correctCoordinatesSplit[0].
                        replaceAll("\\D", "");
                String longitudeString = correctCoordinatesSplit[1].
                        replaceAll("\\D", "");
                double latitude = Double.parseDouble(latitudeString);
                double longitude = Double.parseDouble(longitudeString);
                CityNode currCity = new CityNode(cityName, population, landArea, populationDensity,
                        latitude, longitude);
                allCities.add(currCity);
            }
        } catch (IOException e) {
            System.out.println("error caught in web scraping");
        }
    }

    public List<CityNode> returnCitiesList() {
        return allCities;
    }

    public static void main(String[] args) {
        Scraper wikiScraper = new Scraper();
        List<CityNode> allCities = wikiScraper.returnCitiesList();
        System.out.println(allCities.size());
    }
}
