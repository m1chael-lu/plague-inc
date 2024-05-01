package src;
import java.util.*;
import javafx.application.Application;
public class Graph {
    HashMap<CityNode, ArrayList<TransmissionEdge>> adjList;
    int nodeCount;
    List<CityNode> cityStore;
    GUI infectionMap;
    public Graph(List<CityNode> allCities) {
        nodeCount = allCities.size();
        adjList = new HashMap<>();
        cityStore = allCities;

        GUI.setCityList(allCities);
        Application.launch(GUI.class, new String[]{});
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

    public List<TransmissionEdge> getAdjacentCities(CityNode city) {
        return adjList.get(city);
    }

    public boolean containsCity(String srcCity) {
        for (Map.Entry<CityNode, ArrayList<TransmissionEdge>> city : adjList.entrySet()) {
            if (city.getKey().cityName.equals(srcCity)) {
                return true;
            }
        }
        return false;
    }

    public CityNode getCity(String srcCity) {
        for (Map.Entry<CityNode, ArrayList<TransmissionEdge>> city : adjList.entrySet()) {
            if (city.getKey().cityName.equals(srcCity)) {
                return city.getKey();
            }
        }
        return null;
    }

    public void recalculateGraph(Set<CityNode> infectedCities) {
        for (CityNode city : infectedCities) {
            List<TransmissionEdge> currentEdges = adjList.get(city);
            for (TransmissionEdge edge : currentEdges) {
                edge.recalculate();
            }
        }
    }
}
