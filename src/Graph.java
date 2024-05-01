package src;

import java.util.*;

public class Graph {
    HashMap<CityNode, ArrayList<TransmissionEdge>> adjList;
    int nodeCount;

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
}
