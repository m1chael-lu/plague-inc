package src;

import java.util.*;

class Modeling {
    Graph unitedStates;
    Infection infection;
    int monthCount;
    Set<CityNode> infectedCities;
    double medicinalUpgradeCounter = 0;
    int totalMedicinalUpgrades = 0;
    boolean gameOver = false;
    boolean userWon = false;
    String medicineUpdate;

    public static int baseSocialInteractions = 12;
    public static double populationGrowthRate = 0.0073;

    int totalInfectedMonth;
    int totalKilledMonth;
    int totalRecoveredMonth;

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
        totalInfectedMonth = 0;
        totalKilledMonth = 0;
        totalRecoveredMonth = 0;
        medicineUpdate = "";
    }


    public boolean simulateOneMonth() {
        if (gameOver) {
            return true;
        }
        System.out.println("Now simulating one month");
        monthCount++;

        totalInfectedMonth = 0;
        totalKilledMonth = 0;
        totalRecoveredMonth = 0;

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
        boolean outcome = false;
        if (monthCount % 12 == 0 && monthCount > 24) {
            outcome = evaluateWinOrLoss();
        }
        medicinalUpgrade();
        if (monthCount % 12 == 0) {
            System.out.println("Time to upgrade: Select which upgrade you want:");

        }
        medicinalUpgradeCounter += 0.1;
        return outcome;
    }

    private void simulateDeathsAndRecoveries(CityNode city) {
        int totalKilled = (int)(Math.random() * Math.random() * city.currentlyInfected * infection.getFatalityRate());
        totalKilled = Math.max(Math.min(city.currentlyInfected, totalKilled), 0);
        int totalRecovered = (int)(Math.random() * Math.random() * city.currentlyInfected * (1 - infection.getFatalityRate()));
        totalRecovered = Math.max(Math.min(totalRecovered, city.currentlyInfected - totalKilled), 0);
        city.currentlyInfected -= (totalKilled + totalRecovered);
        city.totalRecovered += totalRecovered;
        city.population -= totalKilled;
        city.totalKilled += totalKilled;
        totalKilledMonth += totalKilled;
        totalRecoveredMonth += totalRecovered;
        System.out.println(city.cityName + ": From month " + (monthCount - 1) + ": " + totalKilled + " killed " + totalRecovered + " recovered");
    }

    private void simulateNewInfections(CityNode city) {
        int newInteractions = (int)(Math.random() * baseSocialInteractions * 30 * (1 +
                city.proximityTransmissionConstant) * city.currentlyInfected);
        int newSusceptible = (int)(Math.random() * newInteractions * infection.getSusceptibilityRate() * (1 - city.percentRecovered));
        int newInfected = (int)(Math.random() * newSusceptible * infection.getInfectionRate());
        newInfected = Math.min(newInfected, city.population - city.currentlyInfected - city.totalRecovered);
        city.currentlyInfected += newInfected;
        totalInfectedMonth += newInfected;
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
                    CityNode target = edge.getEnd();
                    target.currentlyInfected += 1;
                }
            }
        }
        infectedCities.addAll(newlyInfectedCities);
    }

    private boolean evaluateWinOrLoss() {
        if (totalMedicinalUpgrades >= 16) {
            System.out.println("Game over. You lost - medicinal upgrade");
            gameOver = true;
        }
        if (((int)(totalRecoveredMonth * 0.05) > totalInfectedMonth + totalKilledMonth) || (totalInfectedMonth < 100)) {
            System.out.println("Game over. You lost - multi conditions");
            gameOver = true;
        }
        if (unitedStates.evaluateWin()) {
            System.out.println("Game over. You won.");
            userWon = true;
            gameOver = true;
        }
        return gameOver;

    }

    private void medicinalUpgrade() {
        boolean yesUpgrade = false;
        for (int i = 0; i < (int)medicinalUpgradeCounter; i++) {
            if (Math.random() > 0.9) {
                yesUpgrade = true;
            }
        }
        if (!yesUpgrade) {
            return;
        }
        medicinalUpgradeCounter = 0;
        totalMedicinalUpgrades += 1;

        int optionToAttack = (int)(Math.random() * 3);
        medicineUpdate += infection.attackAttr(optionToAttack) + "\n";
        System.out.println("Medicinal upgrade option: " + optionToAttack);
    }

    public Infection getInfection() {
        return infection;
    }

    public String provideUpdate() {
        String update = "Congratulations! You survived a year. Here is an update!\n";
        if (!medicineUpdate.equals("")) {
            update += "Important!!!!\n";
            update += medicineUpdate;
        }
        update += "\nYour disease, " + infection.getInfectionName() + ", has infected " + infectedCities.size();
        if (infectedCities.size() == 1) {
            update += " city:\n";
        } else {
            update += " cities:\n";
        }
        for (CityNode infected : infectedCities) {
            update += infected.getName() + ":\n    " + infected.currentlyInfected + " people currently infected";
            update += "\n    " + infected.totalRecovered + " total people recovered";
            update += "\n    " + infected.totalKilled + " total people killed";
            update += "\n    " + (infected.population - infected.currentlyInfected) + " remaining safe people.\n";
        }
        medicineUpdate = "";
        return update;
    }

}
