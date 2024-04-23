public class TransmissionEdge {
    CityNode start;
    CityNode end;
    float flightTransmissionConstant; // Will be determined by combined score of population sizes
    float landTransmissionConstant; // Will be determined by distance between cities
    float proximityTransmissionConstant; // Will be determined by
    public TransmissionEdge(
            CityNode start, CityNode end
    ) {

    }

    CityNode getStart() {
        return start;
    }

    CityNode getEnd() {
        return end;
    }

}
