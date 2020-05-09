package de.peyrer.graph;

import org.jgrapht.alg.scoring.PageRank;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.util.LinkedList;
import java.util.Map;

public class JGraphTAdapter implements IDirectedGraph {
    private SimpleDirectedGraph<String, DefaultEdge> graph;

    private static final double dampingFactor = 0.85;

    JGraphTAdapter()
    {
        this.graph = new SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
    }

    @Override
    public Map<String, Double> computePageRank() {
        PageRank<String, DefaultEdge> pageRanker = new PageRank<>(graph, dampingFactor);

        return pageRanker.getScores();
    }

    @Override
    public String addVertex(String vertex) {
        boolean added = graph.addVertex(vertex);

        return added ? vertex : null;
    }

    @Override
    public String[] addEdge(String sourceVertex, String targetVertex) {
        DefaultEdge edge = graph.addEdge(sourceVertex, targetVertex);

        return edge == null ? null : new String[]{graph.getEdgeSource(edge), graph.getEdgeTarget(edge)};
    }

    @Override
    public Iterable<String[]> getEdges() {
        LinkedList<String[]> edges = new LinkedList<String[]>();
        for(DefaultEdge edge : this.graph.edgeSet()){
            String[] edgeAsArray = new String[2];
            edgeAsArray[0] = graph.getEdgeSource(edge);
            edgeAsArray[1] = graph.getEdgeTarget(edge);
            edges.add(edgeAsArray);
        }

        return edges;
    }
}
