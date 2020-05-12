package de.peyrer.relevance;

import de.peyrer.graph.IDirectedGraph;

import java.util.Map;

public interface IRelevanceComputer {

    public IDirectedGraph setGraph(IDirectedGraph graph);

    public Map<String,Double> setPageRank(Map<String,Double> pageRank);

    public Map<String,Double> computeRelevance();
}
