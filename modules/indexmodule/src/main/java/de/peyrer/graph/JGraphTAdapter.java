package de.peyrer.graph;

import de.peyrer.repository.ArgumentRepository;
import de.peyrer.repository.IArgumentRepository;
import org.jgrapht.alg.scoring.PageRank;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.concurrent.AsSynchronizedGraph;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

public class JGraphTAdapter extends AbstractJGraphTAdapter {
    public JGraphTAdapter()
    {
        super();

        DefaultDirectedGraph<String, IEdgeWithPremiseNumber> innerGraph
                = new DefaultDirectedGraph<>(DefaultEdgeWithPremiseNumber.class);
        this.graph = new AsSynchronizedGraph<>(innerGraph);

        this.pageRanker = new PageRank<>(graph, dampingFactor, maxIterations, toleranceDefault);
    }

    @Override
    public String[] addEdge(String sourceVertex, String targetVertex, String premiseId, double weight) {
        boolean success = graph.addEdge(sourceVertex, targetVertex, new DefaultEdgeWithPremiseNumber(premiseId));

        return success ? null : new String[]{sourceVertex, targetVertex, premiseId};
    }
}
