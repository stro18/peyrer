package de.peyrer.graph;

import de.peyrer.repository.ArgumentRepository;
import de.peyrer.repository.IArgumentRepository;
import org.jgrapht.alg.scoring.PageRank;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.util.LinkedList;
import java.util.Map;

public class JGraphTAdapter extends AbstractDirectedGraph {
    private SimpleDirectedGraph<String, DefaultEdgeWithPremiseNumber> graph;

    private IArgumentRepository argumentRepository;

    public JGraphTAdapter()
    {
        this.graph = new SimpleDirectedGraph<String, DefaultEdgeWithPremiseNumber>(DefaultEdgeWithPremiseNumber.class);
        this.argumentRepository = new ArgumentRepository();
    }

    @Override
    public Map<String, Double> computeAndSavePageRank() {
        PageRank<String, DefaultEdgeWithPremiseNumber> pageRanker = new PageRank<>(graph, dampingFactor);

        Map<String,Double> pageRankScores = pageRanker.getScores();

        for(Map.Entry<String,Double> entry : pageRankScores.entrySet()){
            this.argumentRepository.updatePageRank(entry.getKey(), entry.getValue());
        }
        return pageRankScores;
    }

    @Override
    public String addVertex(String vertex) {
        boolean added = graph.addVertex(vertex);

        return added ? vertex : null;
    }

    @Override
    public String[] addEdge(String sourceVertex, String targetVertex, String premiseId) {
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
    public Iterable<String[]> getOutgoingEdges(String vertex) {
        LinkedList<String[]> edges = new LinkedList<String[]>();
        for(DefaultEdgeWithPremiseNumber edge : this.graph.outgoingEdgesOf(vertex)){
            String[] edgeAsArray = new String[3];
            edgeAsArray[0] = graph.getEdgeSource(edge);
            edgeAsArray[1] = graph.getEdgeTarget(edge);
            edgeAsArray[2] = edge.getPremiseNumber();
            edges.add(edgeAsArray);
        }

        return edges;
    }
}
