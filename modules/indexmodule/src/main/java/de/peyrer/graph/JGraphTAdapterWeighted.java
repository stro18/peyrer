package de.peyrer.graph;

import org.jgrapht.alg.scoring.PageRank;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.concurrent.AsSynchronizedGraph;

public class JGraphTAdapterWeighted extends AbstractJGraphTAdapter {

    protected AsSynchronizedGraph<String, DefaultWeightedEdgeWithPremiseNumber> graph;

    protected PageRank<String, DefaultWeightedEdgeWithPremiseNumber> pageRanker;

    public JGraphTAdapterWeighted()
    {
        super();

        DefaultDirectedWeightedGraph<String, DefaultWeightedEdgeWithPremiseNumber> innerGraph
                = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdgeWithPremiseNumber.class);
        this.graph = new AsSynchronizedGraph<>(innerGraph);

        this.pageRanker = new PageRank<>(graph, dampingFactor, maxIterations, toleranceDefault);
    }

    @Override
    public String[] addEdge(String sourceVertex, String targetVertex, String premiseId, double weight) {
        DefaultWeightedEdgeWithPremiseNumber edgeWithPremiseNumber = new DefaultWeightedEdgeWithPremiseNumber(premiseId);
        boolean success = graph.addEdge(sourceVertex, targetVertex, edgeWithPremiseNumber);
        graph.setEdgeWeight(edgeWithPremiseNumber, weight);

        return success ? null : new String[]{sourceVertex, targetVertex, premiseId, String.valueOf(weight)};
    }
}
