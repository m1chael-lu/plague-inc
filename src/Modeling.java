package src;

import java.util.*;

class Modeling {
    Graph unitedStates;
    Infection infection;
    int monthCount;
    Set<CityNode> infectedCities;
    int medicinalUpgradeCounter = 0;

    public static int baseSocialInteractions = 12; // daily rate
    public static double populationGrowthRate = 0.0073; // monthlyRate

    public Modeling(Graph unitedStates, Infection infection, String srcCity) {
        this.unitedStates = unitedStates;
        this.infection = infection;
        monthCount = 0;
        infectedCities = new HashSet<>();
        CityNode source = null;

        if (unitedStates.containsCity(srcCity)) {
            source = unitedStates.getCity(srcCity);
        }

        if (source == null) {
            System.out.println("An error occurred. Please enter a valid city.");
        }
        infectedCities.add(source);
    }


    public void simulateOneMonth() {
        System.out.println("Now simulating one month");
        monthCount++;
        for (CityNode city : infectedCities) {
            if (city.currentlyInfected == 0) {
                city.currentlyInfected = city.currentlyInfected + 1;
            } else {
                simulateDeathsAndRecoveries(city);
            }
            System.out.println(city.cityName + ": total infected after month (accounting for recovery and deaths) " + monthCount + ": " + city.currentlyInfected);
            city.recalculate();
            simulateNewInfections(city);
        }
        unitedStates.recalculateGraph(infectedCities);
        simulateBFS();
        growPopulations();
        System.out.println("Cities that have been affected: ");
        for (CityNode city : infectedCities) {
            System.out.println("- " + city.cityName);
        }
        evaluateWinOrLoss();
        medicinalUpgrade();
        if (monthCount % 12 == 0) {
            System.out.println("Time to upgrade: Select which upgrade you want:");
        }
        medicinalUpgradeCounter += 0.5;
    }

    private void simulateDeathsAndRecoveries(CityNode city) {
        int totalKilled = (int)(Math.random() * city.currentlyInfected * infection.getFatalityRate());
        totalKilled = Math.max(Math.min(city.currentlyInfected, totalKilled), 0);
        int totalRecovered = (int)(Math.random() * city.currentlyInfected * (1 - infection.getFatalityRate()));
        totalRecovered = Math.max(Math.min(totalRecovered, city.currentlyInfected - totalKilled), 0);
        city.currentlyInfected -= (totalKilled + totalRecovered);
        city.totalRecovered += totalRecovered;
        city.population -= totalKilled;
        System.out.println(city.cityName + ": From month " + (monthCount - 1) + ": " + totalKilled + " killed " + totalRecovered + " recovered");
    }

    private void simulateNewInfections(CityNode city) {
        int newInteractions = (int)(Math.random() * baseSocialInteractions * 30 * (1 +
                city.proximityTransmissionConstant) * city.currentlyInfected);
        int newSusceptible = (int)(Math.random() * newInteractions * infection.getSusceptibilityRate() * (1 - city.percentRecovered));
        int newInfected = (int)(Math.random() * newSusceptible * infection.getInfectionRate());
        newInfected = Math.min(newInfected, city.population - city.currentlyInfected - city.totalRecovered);
        city.currentlyInfected += newInfected;
        System.out.println(city.cityName + ": total infected after month " + monthCount + ": " + city.currentlyInfected);
    }

    private void growPopulations() {
        for (CityNode city : unitedStates.adjList.keySet()) {
            city.population = (int)(city.population * (1 + populationGrowthRate));
            city.recalculate();
        }
    }

    private void simulateBFS() {
        Set<CityNode> newlyInfectedCities = new HashSet<>();
        for (CityNode city : infectedCities) {
            if (city.currentlyInfected == 0) {
                continue;
            }
            List<TransmissionEdge> adjacentCities = unitedStates.getAdjacentCities(city);
            for (TransmissionEdge edge : adjacentCities) {
                if (infectedCities.contains(edge.getEnd())) {
                    continue;
                }
                double probabilityOfFlight = edge.getFlightTransmissionConstant();
                double probabilityOfLand = edge.getLandTransmissionConstant();
                double probabilityOfTraversal = Math.random() * probabilityOfFlight + Math.random() * probabilityOfLand;
                if (probabilityOfTraversal > 1.0) {
                    newlyInfectedCities.add(edge.getEnd());
                }
            }
        }
        infectedCities.addAll(newlyInfectedCities);
    }

    private void evaluateWinOrLoss() {

    }

    private void medicinalUpgrade() {

    }

}
