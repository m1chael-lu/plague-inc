package src;

import java.util.*;

class Modeling {
    Graph unitedStates;
    Infection infection;
    int dayCount;
    CityNode source;

    public static int dailySocialInteractions = 12;

    public Modeling(Graph unitedStates, Infection infection, String srcCity) {
        this.unitedStates = unitedStates;
        this.infection = infection;
        dayCount = 0;
        for (Map.Entry<CityNode, ArrayList<TransmissionEdge>> city : this.unitedStates.adjList.entrySet()) {
            if (city.getKey().cityName.equals(srcCity)) {
                source = city.getKey();
                break;
            }
        }
        if (source == null) {
            System.out.println("An error occurred. Please enter a valid city.");
        }
    }

    // Assumes 12 social interactions a day
    public void simulateOneDay() {
        System.out.println("Now simulating one day");
        dayCount++;
        if (dayCount == 1) {
            source.percentInfected = 1.0 / source.population;
        }

    }
}
