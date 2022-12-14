package de.peyrer.indexmodule;

import de.peyrer.graph.*;
import de.peyrer.indexer.*;
import de.peyrer.relevance.IRelevanceComputer;
import de.peyrer.relevance.SumComputer;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class Indexmodule implements IIndexmodule {

    private IIndexer premiseIndexer;
    private IIndexer conclusionIndexer;

    private static final String[] premiseIndexPath = new String[]{"tempIndex", "premiseIndex"};
    private static final String[] conclusionIndexPath = new String[]{"tempIndex", "conclusionIndex"};

    private static final String AND = "AND";
    private static final String PHRASE = "PHRASE";
    private static final String PHRASE_PREMISE = "PHRASE_PREMISE";
    private static final String TFIDF = "TFIDF";
    private static final String TFIDF_WEIGHTED = "TFIDF_WEIGHTED";
    private static final String BM25 = "BM25";

    @Override
    public String getIndexPath() throws IOException {
        if(this.indexExist("index")){
            Path path = Paths.get(System.getProperty("user.dir"), "index");
            return path.toString();
        }else{
            return null;
        }
    }

    @Override
    public void indexWithRelevance() throws IOException, InvalidSettingValueException, InterruptedException, ParseException {
        Instant start = Instant.now();

        IRelevanceComputer relevanceComputer = new SumComputer();

        AbstractRelevanceIndexer relevanceIndexer = new RelevanceIndexer("index");

        this.prepareIndexForMatching();

        System.out.println("Building of graph started at : " + java.time.ZonedDateTime.now());

        IDirectedGraph graph = this.buildGraph();

        System.out.println("Building of graph ended at : " + java.time.ZonedDateTime.now());

        System.out.println("Computing and saving of pageRank started at : " + java.time.ZonedDateTime.now());
        Map<String,Double> pageRank = graph.computeAndSavePageRank();
        System.out.println("Computing and saving of pageRank ended at : " + java.time.ZonedDateTime.now());

        System.out.println("Computing and saving of relevance started at : " + java.time.ZonedDateTime.now());
        relevanceComputer.setGraph(graph);
        relevanceComputer.setPageRank(pageRank);
        Map<String,Double> relevance = relevanceComputer.computeAndSaveRelevance();
        System.out.println("Computing and saving of relevance ended at : " + java.time.ZonedDateTime.now());

        System.out.println("Indexing of relevance started at : " + java.time.ZonedDateTime.now());
        try {
            relevanceIndexer.setRelevance(relevance);
            relevanceIndexer.index();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Indexing of relevance ended at : " + java.time.ZonedDateTime.now());

        Instant end = Instant.now();
        System.out.println("Total duration of index process: " + Duration.between(start, end));
    }

    private void prepareIndexForMatching() throws IOException, InvalidSettingValueException {
        boolean premiseIndexRequired;
        boolean conclusionIndexRequired;

        String matcherType = System.getenv().get("MATCHING");
        switch(matcherType){
            case AND:
                premiseIndexRequired = true;
                conclusionIndexRequired = true;
                break;
            case PHRASE:
            case TFIDF:
            case TFIDF_WEIGHTED:
            case BM25:
                premiseIndexRequired = true;
                conclusionIndexRequired = false;
                break;
            case PHRASE_PREMISE:
                premiseIndexRequired = false;
                conclusionIndexRequired = true;
                break;
            default:
                throw new InvalidSettingValueException("The setting MATCHING=" + matcherType +  " is not allowed!");
        }

        if (
                premiseIndexRequired
                && (
                        (System.getenv().get("NEW_MATCHING_INDEX") != null && System.getenv().get("NEW_MATCHING_INDEX").equals("true"))
                        || !this.indexExist(premiseIndexPath)
                )
        ){
            System.out.println("Indexing of premises started at : " + java.time.ZonedDateTime.now());

            this.premiseIndexer = new PremiseIndexer(premiseIndexPath);
            premiseIndexer.index();

            System.out.println("Indexing of premises ended at : " + java.time.ZonedDateTime.now());
        }

        if (
                conclusionIndexRequired
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

    private IGraphBuilder getGraphBuilder() throws InvalidSettingValueException {
        IGraphBuilder.GraphType type;
        String matching = System.getenv().get("MATCHING");
        switch (matching) {
            case AND:
            case PHRASE:
            case PHRASE_PREMISE:
            case TFIDF:
                type = IGraphBuilder.GraphType.JGRAPHT;
                break;
            case TFIDF_WEIGHTED:
            case BM25:
                type = IGraphBuilder.GraphType.JGRAPHT_WEIGHTED;
                break;
            default:
                throw new InvalidSettingValueException("The setting MATCHING=" + matching + " is not allowed!");
        }


        String threading = System.getenv().get("THREADING").toLowerCase();
        switch(threading){
            case "true":
                return new GraphBuilderForThreads(type);
            case "false":
                return new GraphBuilder(IGraphBuilder.GraphType.JGRAPHT);
            default:
                throw new InvalidSettingValueException("The setting MATCHING=" + threading +  " is not allowed!");
        }
    }

    private IDirectedGraph buildGraph() throws InvalidSettingValueException, IOException, InterruptedException, ParseException {
        IGraphBuilder graphBuilder = this.getGraphBuilder();

        String matcherType = System.getenv().get("MATCHING");
        switch(matcherType){
            case AND:
                return graphBuilder.build(
                        Paths.get(System.getProperty("user.dir"), premiseIndexPath).toString(),
                        Paths.get(System.getProperty("user.dir"), conclusionIndexPath).toString()
                );
            case PHRASE:
            case TFIDF:
            case TFIDF_WEIGHTED:
            case BM25:
                return graphBuilder.buildWithPremiseIndex(
                        Paths.get(System.getProperty("user.dir"), premiseIndexPath).toString()
                );
            case PHRASE_PREMISE:
                return graphBuilder.buildWithConclusionIndex(
                        Paths.get(System.getProperty("user.dir"), conclusionIndexPath).toString()
                );
            default:
                throw new InvalidSettingValueException("The setting MATCHING=" + matcherType +  " is not allowed!");
        }
    }

    private boolean indexExist(String ... path) throws IOException {
        Path pathObject = Paths.get(System.getProperty("user.dir"), path);
        if (Files.isDirectory(pathObject)) {
            try (DirectoryStream<Path> directory = Files.newDirectoryStream(pathObject)) {
                return directory.iterator().hasNext();
            }
        }

        return false;
    }
}
