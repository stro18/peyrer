package de.peyrer.graph;

import java.util.Map;

public interface IDirectedGraph {

    Map<String,Double> computeAndSavePageRank();

    int getNumberOfEdges();

    Iterable<String[]> getOutgoingEdges(String vertex);
}
