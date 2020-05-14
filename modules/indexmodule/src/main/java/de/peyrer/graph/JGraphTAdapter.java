package de.peyrer.graph;

import org.jgrapht.alg.scoring.PageRank;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.util.LinkedList;
import java.util.Map;

class JGraphTAdapter extends AbstractDirectedGraph {
    private SimpleDirectedGraph<String, DefaultEdgeWithPremiseNumber> graph;

    JGraphTAdapter()
    {
        this.graph = new SimpleDirectedGraph<String, DefaultEdgeWithPremiseNumber>(DefaultEdgeWithPremiseNumber.class);
    }

    @Override
    public Map<String, Double> computePageRank() {
        PageRank<String, DefaultEdgeWithPremiseNumber> pageRanker = new PageRank<>(graph, dampingFactor);

        return pageRanker.getScores();
    }

    @Override
    String addVertex(String vertex) {
        boolean added = graph.addVertex(vertex);

        return added ? vertex : null;
    }

    @Override
    String[] addEdge(String sourceVertex, String targetVertex, String premiseId) {
        boolean success = graph.addEdge(sourceVertex, targetVertex, new DefaultEdgeWithPremiseNumber(premiseId));

        return success ? null : new String[]{sourceVertex, targetVertex, premiseId};
    }

    @Override
    public Iterable<String[]> getEdges() {
        LinkedList<String[]> edges = new LinkedList<String[]>();
        for(DefaultEdgeWithPremiseNumber edge : this.graph.edgeSet()){
            String[] edgeAsArray = new String[3];
            edgeAsArray[0] = graph.getEdgeSource(edge);
            edgeAsArray[1] = graph.getEdgeTarget(edge);
            edgeAsArray[2] = edge.getPremiseNumber();
            edges.add(edgeAsArray);
        }

        return edges;
    }
    
    @Override
    //THIS IS A PLACEHOLDER!! STEPHAN DO SOMETHING!! HALP!!
    public Iterable<String[]> getEdges(String vertex){
    	LinkedList<String[]> result = new LinkedList<>();
    	return result;
    }
}
