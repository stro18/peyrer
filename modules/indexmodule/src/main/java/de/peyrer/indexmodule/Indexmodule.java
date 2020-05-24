package de.peyrer.indexmodule;

import de.peyrer.graph.GraphBuilder;
import de.peyrer.graph.IDirectedGraph;
import de.peyrer.indexer.IIndexer;
import de.peyrer.relevance.IRelevanceComputer;

import java.io.IOException;
import java.util.Map;

public class Indexmodule implements IIndexmodule {

    private GraphBuilder graphBuilder;

    private IIndexer premiseIndexer;

    private IIndexer conclusionIndexer;

    private IIndexer relevanceIndexer;

    private IRelevanceComputer relevanceComputer;

    @Override
    public void indexWithRelevance() {
        try {
            premiseIndexer.index();
            conclusionIndexer.index();
        } catch (IOException e) {
            e.printStackTrace();
        }

        IDirectedGraph graph = graphBuilder.build();

        Map<String,Double> pageRank = graph.computeAndSavePageRank();

        relevanceComputer.setGraph(graph);
        relevanceComputer.setPageRank(pageRank);
        Map<String,Double> relevance = relevanceComputer.computeAndSaveRelevance();

        graphBuilder.saveToDatabase(graph, pageRank, relevance);

        try {
            relevanceIndexer.index();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
