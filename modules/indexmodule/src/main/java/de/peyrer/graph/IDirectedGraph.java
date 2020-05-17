package de.peyrer.graph;

import java.util.Map;

public interface IDirectedGraph {

    Map<String,Double> computeAndSavePageRank();

    Iterable<String[]> getEdges();

    Iterable<String[]> getOutgoingEdges(String vertex);
}
