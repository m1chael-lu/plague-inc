package src;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Manages the simulation of an infection spread within a network of cities.
 * This class handles the dynamics of infection spread, including interactions, recoveries,
 * deaths, and population growth within a network modeled as a graph of cities.
 */
class Modeling {
    public static int baseSocialInteractions = 12;
    public static double populationGrowthRate = 0.0073;
    Graph unitedStates;
    Infection infection;
    int monthCount;
    Set<CityNode> infectedCities;
    double medicinalUpgradeCounter = 0;
    int totalMedicinalUpgrades = 0;
    boolean gameOver = false;
    boolean userWon = false;
    String medicineUpdate;
    int totalInfectedMonth;
    int totalKilledMonth;
    int totalRecoveredMonth;

    /**
     * Constructor initializes the modeling of the infection spread starting from a specific city.
     *
     * @param unitedStates The graph of cities representing the United States.
     * @param infection    The type of infection to be simulated.
     * @param srcCity      The initial city where the infection starts.
     */
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

    /**
     * Simulates one month of infection spread, updating the states of infected cities and the
     * overall model.
     *
     * @return true if the simulation indicates the game is over, either by win or loss conditions
     * being met.
     */
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
            System.out.println(city.cityName + ": total infected after month (accounting for " +
                    "recovery and deaths) " + monthCount + ": " + city.currentlyInfected);
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

    /**
     * Simulates the deaths and recoveries within an infected city.
     *
     * @param city The city in which to simulate deaths and recoveries.
     */
    private void simulateDeathsAndRecoveries(CityNode city) {
        int totalKilled = (int) (Math.random() * Math.random() * city.currentlyInfected *
                infection.getFatalityRate());
        totalKilled = Math.max(Math.min(city.currentlyInfected, totalKilled), 0);
        int totalRecovered = (int) (Math.random() * Math.random() * city.currentlyInfected * (1 -
                infection.getFatalityRate()));
        totalRecovered = Math.max(Math.min(totalRecovered, city.currentlyInfected - totalKilled),
                0);
        city.currentlyInfected -= (totalKilled + totalRecovered);
        city.totalRecovered += totalRecovered;
        city.population -= totalKilled;
        city.totalKilled += totalKilled;
        totalKilledMonth += totalKilled;
        totalRecoveredMonth += totalRecovered;
        System.out.println(city.cityName + ": From month " + (monthCount - 1) + ": " + totalKilled
                + " killed " + totalRecovered + " recovered");
    }

    /**
     * Simulates new infections within a city based on the current infection status and social
     * interactions.
     *
     * @param city The city in which to simulate new infections.
     */
    private void simulateNewInfections(CityNode city) {
        int newInteractions = (int) (Math.random() * baseSocialInteractions * 30 * (1 +
                city.proximityTransmissionConstant) * city.currentlyInfected);
        int newSusceptible = (int) (Math.random() * newInteractions *
                infection.getSusceptibilityRate() * (1 - city.percentRecovered));
        int newInfected = (int) (Math.random() * newSusceptible * infection.getInfectionRate());
        newInfected = Math.min(newInfected, city.population - city.currentlyInfected -
                city.totalRecovered);
        city.currentlyInfected += newInfected;
        totalInfectedMonth += newInfected;
        System.out.println(city.cityName + ": total infected after month " + monthCount + ": " +
                city.currentlyInfected);
    }

    /**
     * Simulates population growth across all cities within the graph.
     */
    private void growPopulations() {
        for (CityNode city : unitedStates.adjList.keySet()) {
            city.population = (int) (city.population * (1 + populationGrowthRate));
            city.recalculate();
        }
    }

    /**
     * Simulates the spread of infection between cities using a breadth-first search approach.
     */
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
                double probabilityOfTraversal = Math.random() * probabilityOfFlight + Math.random()
                        * probabilityOfLand;
                if (probabilityOfTraversal > 1.0) {
                    newlyInfectedCities.add(edge.getEnd());
                    CityNode target = edge.getEnd();
                    target.currentlyInfected += 1;
                }
            }
        }
        infectedCities.addAll(newlyInfectedCities);
    }

    /**
     * Evaluates whether the game is won or lost based on various conditions.
     *
     * @return true if the game is over (either won or lost), false otherwise.
     */
    private boolean evaluateWinOrLoss() {
        if (totalMedicinalUpgrades >= 16) {
            System.out.println("Game over. You lost - medicinal upgrade");
            gameOver = true;
        }
        if (((int) (totalRecoveredMonth * 0.05) > totalInfectedMonth + totalKilledMonth) ||
                (totalInfectedMonth < 100)) {
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

    /**
     * Handles the timing and effects of medicinal upgrades.
     */
    private void medicinalUpgrade() {
        boolean yesUpgrade = false;
        for (int i = 0; i < (int) medicinalUpgradeCounter; i++) {
            if (Math.random() > 0.9) {
                yesUpgrade = true;
            }
        }
        if (!yesUpgrade) {
            return;
        }
        medicinalUpgradeCounter = 0;
        totalMedicinalUpgrades += 1;

        int optionToAttack = (int) (Math.random() * 3);
        medicineUpdate += infection.attackAttr(optionToAttack) + "\n";
        System.out.println("Medicinal upgrade option: " + optionToAttack);
    }

    /**
     * Provides a status update on the infection and recovery statistics across all affected cities.
     *
     * @return A summary of infection statistics for the current year.
     */
    public String provideUpdate() {
        String update = "Congratulations! You survived a year. Here is an update!\n";
        if (!medicineUpdate.equals("")) {
            update += "Important!!!!\n";
            update += medicineUpdate;
        }
        update += "\nYour disease, " + infection.getInfectionName() + ", has infected " +
                infectedCities.size();
        if (infectedCities.size() == 1) {
            update += " city:\n";
        } else {
            update += " cities:\n";
        }
        for (CityNode infected : infectedCities) {
            update += infected.getName() + ":\n    " + infected.currentlyInfected + " people " +
                    "currently infected";
            update += "\n    " + infected.totalRecovered + " total people recovered";
            update += "\n    " + infected.totalKilled + " total people killed";
            update += "\n    " + (infected.population - infected.currentlyInfected) +
                    " remaining safe people.\n";
        }
        medicineUpdate = "";
        return update;
    }

    /**
     * Provides the infection itself
     *
     * @return the infection used to model the simulation
     */
    public Infection getInfection() {
        return infection;
    }
}
