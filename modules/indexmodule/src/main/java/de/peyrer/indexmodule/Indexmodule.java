package de.peyrer.indexmodule;

import de.peyrer.graph.GraphBuilder;
import de.peyrer.graph.IDirectedGraph;
import de.peyrer.indexer.ConclusionIndexer;
import de.peyrer.indexer.IIndexer;
import de.peyrer.indexer.PremiseIndexer;
import de.peyrer.indexer.RelevanceIndexer;
import de.peyrer.relevance.IRelevanceComputer;
import de.peyrer.relevance.SumComputer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Indexmodule implements IIndexmodule {

    @Override
    public void indexWithRelevance() throws IOException {
        Instant start = Instant.now();

        GraphBuilder graphBuilder = new GraphBuilder(GraphBuilder.GraphType.JGRAPHT, GraphBuilder.MatcherType.AND);

        IIndexer premiseIndexer = new PremiseIndexer("temp", "premiseindex");
        IIndexer conclusionIndexer = new ConclusionIndexer("temp", "conclusionindex");
        IIndexer relevanceIndexer = new RelevanceIndexer("index");

        System.out.println("Indexing of premises and conclusions started at : " + java.time.ZonedDateTime.now());
        try {
            premiseIndexer.index();
            conclusionIndexer.index();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Indexing of premises and conclusions ended at : " + java.time.ZonedDateTime.now());

        System.out.println("Building of graph started at : " + java.time.ZonedDateTime.now());
        IDirectedGraph graph = graphBuilder.build(premiseIndexer.getIndexPath(), conclusionIndexer.getIndexPath());
        System.out.println("Building of graph ended at : " + java.time.ZonedDateTime.now());

        System.out.println("Computing and saving of pageRank started at : " + java.time.ZonedDateTime.now());
        Map<String,Double> pageRank = graph.computeAndSavePageRank();
        System.out.println("Computing and saving of pageRank ended at : " + java.time.ZonedDateTime.now());

        System.out.println("Computing and saving of relevance started at : " + java.time.ZonedDateTime.now());
        IRelevanceComputer relevanceComputer = new SumComputer();
        relevanceComputer.setGraph(graph);
        relevanceComputer.setPageRank(pageRank);
        relevanceComputer.computeAndSaveRelevance();
        System.out.println("Computing and saving of relevance ended at : " + java.time.ZonedDateTime.now());

        System.out.println("Indexing of relevance started at : " + java.time.ZonedDateTime.now());
        try {
            relevanceIndexer.index();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Indexing of relevance ended at : " + java.time.ZonedDateTime.now());

        Instant end = Instant.now();
        System.out.println("Total duration of index process: " + Duration.between(start, end));
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
}
