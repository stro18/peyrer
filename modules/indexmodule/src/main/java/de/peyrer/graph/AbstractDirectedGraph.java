package de.peyrer.graph;

public abstract class AbstractDirectedGraph implements IDirectedGraph {

    abstract String addVertex(String vertex);

    abstract String[] addEdge(String sourceVertex, String targetVertex);
}
