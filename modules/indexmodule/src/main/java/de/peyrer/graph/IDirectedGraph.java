package de.peyrer.graph;

public interface IDirectedGraph {

    public IDirectedGraph computePageRank();

    public String setVertex(String vertex);

    public String[] setEdge(String sourceVertex, String targetVertex);

    public Iterable<String[]> getEdges();
}
