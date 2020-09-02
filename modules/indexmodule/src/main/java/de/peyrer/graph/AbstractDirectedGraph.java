package de.peyrer.graph;

public abstract class AbstractDirectedGraph implements IDirectedGraph {

    public static final double dampingFactor = 0.85;

    public static final int maxIterations = 100;

    public static final double toleranceDefault = 0.000001;

    public abstract String addVertex(String vertex);

    public abstract String[] addEdge(String sourceVertex, String targetVertex, String premiseId, double weight);
}
