package src;

import java.util.*;

/**
 * Represents a graph of cities connected by transmission edges.
 * This class manages a graph structure where each city node is linked to other cities through edges
 * that represent potential transmission paths for an infection.
 */
public class Graph {
    HashMap<CityNode, ArrayList<TransmissionEdge>> adjList;
    int nodeCount;

    /**
     * Constructs a Graph from a list of city nodes.
     * Initializes each city with edges to every other city except itself.
     *
     * @param allCities List of all cities to be included in the graph.
     */
    public Graph(List<CityNode> allCities) {
        nodeCount = allCities.size();
        adjList = new HashMap<>();

        for (CityNode source : allCities) {
            ArrayList<TransmissionEdge> edgesFromSource = new ArrayList<>();
            for (CityNode target : allCities) {
                if (target == source) {
                    continue;
                }
                TransmissionEdge srcToTarget = new TransmissionEdge(source, target);
                edgesFromSource.add(srcToTarget);
            }
            adjList.put(source, edgesFromSource);
        }
    }

    /**
     * Returns the list of transmission edges for a given city.
     *
     * @param city The city node for which edges are requested.
     * @return List of TransmissionEdge connecting the specified city to others.
     */
    public List<TransmissionEdge> getAdjacentCities(CityNode city) {
        return adjList.get(city);
    }

    /**
     * Checks if the graph contains a city with the specified name.
     *
     * @param srcCity The name of the city to search for.
     * @return true if the city is found in the graph, false otherwise.
     */
    public boolean containsCity(String srcCity) {
        for (Map.Entry<CityNode, ArrayList<TransmissionEdge>> city : adjList.entrySet()) {
            if (city.getKey().cityName.equals(srcCity)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves a city node based on its name.
     *
     * @param srcCity The name of the city to retrieve.
     * @return The CityNode object if found, null otherwise.
     */
    public CityNode getCity(String srcCity) {
        for (Map.Entry<CityNode, ArrayList<TransmissionEdge>> city : adjList.entrySet()) {
            if (city.getKey().cityName.equals(srcCity)) {
                return city.getKey();
            }
        }
        return null;
    }

    /**
     * Recalculates the properties of the transmission edges for all infected cities.
     *
     * @param infectedCities A set of cities that are currently infected.
     */
    public void recalculateGraph(Set<CityNode> infectedCities) {
        for (CityNode city : infectedCities) {
            List<TransmissionEdge> currentEdges = adjList.get(city);
            for (TransmissionEdge edge : currentEdges) {
                edge.recalculate();
            }
        }
    }

    /**
     * Evaluates if the game condition for a win has been met.
     *
     * @return true if more than 200 cities have currently infected individuals, false otherwise.
     */
    public boolean evaluateWin() {
        int countCities = 0;
        for (Map.Entry<CityNode, ArrayList<TransmissionEdge>> cityEntry : adjList.entrySet()) {
            CityNode city = cityEntry.getKey();
            if (city.currentlyInfected > 0) {
                countCities++;
            }
        }
        return countCities > 200;
    }

    /**
     * Retrieves the adjacency list representing the graph.
     *
     * @return A HashMap representing the adjacency list of the graph.
     */
    public HashMap<CityNode, ArrayList<TransmissionEdge>> getAdjList() {
        return adjList;
    }
}
