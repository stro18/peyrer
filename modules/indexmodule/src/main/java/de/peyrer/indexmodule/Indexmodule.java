package de.peyrer.indexmodule;

import de.peyrer.graph.GraphBuilder;
import de.peyrer.graph.IDirectedGraph;
import de.peyrer.indexer.ConclusionIndexer;
import de.peyrer.indexer.IIndexer;
import de.peyrer.indexer.PremiseIndexer;
import de.peyrer.indexer.RelevanceIndexer;
import de.peyrer.relevance.IRelevanceComputer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Indexmodule implements IIndexmodule {

    private GraphBuilder graphBuilder;

    private IIndexer premiseIndexer;

    private IIndexer conclusionIndexer;

    private IIndexer relevanceIndexer;

    private IRelevanceComputer relevanceComputer;

    @Override
    public void indexWithRelevance() throws IOException {
        this.graphBuilder = new GraphBuilder(GraphBuilder.GraphType.JGRAPHT, GraphBuilder.MatcherType.AND);

        this.premiseIndexer = new PremiseIndexer("temp", "premiseindex");
        this.conclusionIndexer = new ConclusionIndexer("temp", "conclusionindex");
        this.relevanceIndexer = new RelevanceIndexer("index");

        try {
            premiseIndexer.index();
            conclusionIndexer.index();
        } catch (IOException e) {
            e.printStackTrace();
        }

        IDirectedGraph graph = graphBuilder.build();

        Map<String,Double> pageRank = graph.computeAndSavePageRank();

        //relevanceComputer.setGraph(graph);
        //relevanceComputer.setPageRank(pageRank);
        //Map<String,Double> relevance = relevanceComputer.computeRelevance();

        try {
            relevanceIndexer.index();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getIndexPath(){
        Path path = Paths.get(System.getProperty("user.dir"), "index");
        if(Files.exists(path)){
            return path.toString();
        }else{
            return null;
        }
    }

    @Override
    public List<String> getStopwords() {
        return Arrays.asList(
                "a", "an", "and", "are", "as", "at", "be", "but", "by",
                "for", "if", "in", "into", "is", "it", "of", "on", "or", "such",
                "that", "the", "their", "then", "there", "these",
                "they", "this", "to", "was", "will", "with"
        );
    }
}
