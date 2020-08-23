package de.peyrer.indexmodule;

import de.peyrer.graph.GraphBuilder;
import de.peyrer.graph.GraphBuilderForThreads;
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

    private IIndexer premiseIndexer;
    private IIndexer conclusionIndexer;

    private static final String[] premiseIndexPath = new String[]{"tempIndex", "premiseIndex"};
    private static final String[] conclusionIndexPath = new String[]{"tempIndex", "conclusionIndex"};

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
    public void indexWithRelevance() throws IOException, InvalidSettingValueException {
        Instant start = Instant.now();

        GraphBuilderForThreads graphBuilder = new GraphBuilderForThreads(GraphBuilder.GraphType.JGRAPHT);

        IRelevanceComputer relevanceComputer = new SumComputer();

        IIndexer relevanceIndexer = new RelevanceIndexer("index");

        this.prepareIndexForMatching();

        System.out.println("Building of graph started at : " + java.time.ZonedDateTime.now());

        IDirectedGraph graph = null;
        if (System.getenv().get("MATCHING") != null && System.getenv().get("MATCHING").equals("AND")) {
            graph = graphBuilder.build(
                    Paths.get(System.getProperty("user.dir"), premiseIndexPath).toString()
            );
        } else {
            graph = graphBuilder.build(Paths.get(System.getProperty("user.dir"), premiseIndexPath).toString());
        }

        System.out.println("Building of graph ended at : " + java.time.ZonedDateTime.now());

        System.out.println("Computing and saving of pageRank started at : " + java.time.ZonedDateTime.now());
        Map<String,Double> pageRank = graph.computeAndSavePageRank();
        System.out.println("Computing and saving of pageRank ended at : " + java.time.ZonedDateTime.now());

        System.out.println("Computing and saving of relevance started at : " + java.time.ZonedDateTime.now());
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

    private void prepareIndexForMatching() throws IOException {
        if (
                (System.getenv().get("NEW_MATCHING_INDEX") != null && System.getenv().get("NEW_MATCHING_INDEX").equals("true"))
                        || !this.indexExist(premiseIndexPath)
        ){
            System.out.println("Indexing of premises started at : " + java.time.ZonedDateTime.now());

            this.premiseIndexer = new PremiseIndexer(premiseIndexPath);
            premiseIndexer.index();

            System.out.println("Indexing of premises ended at : " + java.time.ZonedDateTime.now());
        }

        if (
                System.getenv().get("MATCHING") != null && System.getenv().get("MATCHING").equals("AND")
                && (
                        (System.getenv().get("NEW_MATCHING_INDEX") != null && System.getenv().get("NEW_MATCHING_INDEX").equals("true"))
                        || !this.indexExist(conclusionIndexPath)
                )
        ) {
            System.out.println("Indexing of conclusions started at : " + java.time.ZonedDateTime.now());

            this.conclusionIndexer = new ConclusionIndexer(conclusionIndexPath);
            conclusionIndexer.index();

            System.out.println("Indexing of conclusions ended at : " + java.time.ZonedDateTime.now());
        }
    }

    private boolean indexExist(String ... path){
        Path pathObject = Paths.get(System.getProperty("user.dir"), path);
        return Files.exists(pathObject);
    }
}
