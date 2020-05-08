package de.peyrer.relevance;

import de.peyrer.graph.IDirectedGraph;

public interface IRelevanceComputer {

    public IDirectedGraph setGraph(IDirectedGraph graph);

    public IDirectedGraph computeRelevance();
}
