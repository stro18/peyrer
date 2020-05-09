package de.peyrer.graph;

import java.util.Map;

public interface IDirectedGraph {

    Map<String,Double> computePageRank();

    String addVertex(String vertex);

    String[] addEdge(String sourceVertex, String targetVertex);

    Iterable<String[]> getEdges();
}
