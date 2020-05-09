package de.peyrer.graph;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.LinkedList;

public class JGraphTAdapter implements IDirectedGraph {
    private SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> graph;

    JGraphTAdapter()
    {
        this.graph = new SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
    }

    @Override
    public IDirectedGraph computePageRank() {
        return null;
    }

    @Override
    public String setVertex(String vertex) {
        boolean added = graph.addVertex(vertex);

        return added ? vertex : null;
    }

    @Override
    public String[] setEdge(String sourceVertex, String targetVertex) {
        DefaultWeightedEdge edge = graph.addEdge(sourceVertex, targetVertex);

        return edge == null ? null : new String[]{graph.getEdgeSource(edge), graph.getEdgeTarget(edge)};
    }

    @Override
    public Iterable<String[]> getEdges() {
        LinkedList<String[]> edges = new LinkedList<String[]>();
        for(DefaultWeightedEdge edge : this.graph.edgeSet()){
            String[] edgeAsArray = new String[2];
            edgeAsArray[0] = graph.getEdgeSource(edge);
            edgeAsArray[1] = graph.getEdgeTarget(edge);
            edges.add(edgeAsArray);
        }

        return edges;
    }
}
