import java.util.ArrayList;
import java.util.HashMap;

public class Graph {
    HashMap<CityNode, ArrayList<TransmissionEdge>> adjList;
    int nodeCount;
    public Graph(int nodeCount) {
        this.adjList = new HashMap<>();
        this.nodeCount = nodeCount;
    }
}
