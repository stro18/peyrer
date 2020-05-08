package de.peyrer.indexmodule;

import de.peyrer.graph.GraphBuilder;
import de.peyrer.graph.IDirectedGraph;
import de.peyrer.indexer.IIndexer;
import de.peyrer.relevance.IRelevanceComputer;

public class Indexmodule implements IIndexmodule {

    private GraphBuilder graphBuilder;

    private IIndexer indexer;

    private IRelevanceComputer relevanceComputer;

    @Override
    public void indexWithRelevance() {
        indexer.indexPrem();

        IDirectedGraph graph = graphBuilder.build();

        relevanceComputer.setGraph(graph);
        IDirectedGraph graphWithRelevance = relevanceComputer.computeRelevance();

        graphBuilder.saveToDatabase(graphWithRelevance);

        indexer.indexConc();
    }
}
