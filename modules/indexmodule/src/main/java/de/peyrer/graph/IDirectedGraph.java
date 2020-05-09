package de.peyrer.graph;

import java.util.Map;

public interface IDirectedGraph {

    public Map<String,Double> computePageRank();

    public String setVertex(String vertex);

    public String[] setEdge(String sourceVertex, String targetVertex);

    public Iterable<String[]> getEdges();
}
