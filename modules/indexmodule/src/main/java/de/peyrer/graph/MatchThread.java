package de.peyrer.graph;

import de.peyrer.analyzermodule.AnalyzerModule;
import de.peyrer.model.Argument;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Callable;

public class MatchThread implements Callable<Integer> {

    private final AbstractDirectedGraph graph;

    private final Argument argument;

    private final String premiseIndexPath;

    MatchThread(AbstractDirectedGraph graph, Argument argument, String premiseIndexPath){
        this.graph = graph;
        this.argument = argument;
        this.premiseIndexPath = premiseIndexPath;
    }

    @Override
    public Integer call() throws IOException {
        graph.addVertex(argument.id);

        Iterable<Map<String,String>> matches = this.match();

        for(Map<String,String> match : matches){
            graph.addVertex(match.get("argumentId"));
            graph.addEdge(match.get("argumentId"), argument.id, match.get("premiseId"));
        }

        return 0;
    }

    private Iterable<Map<String,String>> match() throws IOException {
        LinkedList<Map<String,String>> result = new LinkedList<>();
        Directory directory = FSDirectory.open(Paths.get(premiseIndexPath));
        DirectoryReader iReader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(iReader);
        Analyzer analyzer = (new AnalyzerModule()).getAnalyzer();
        QueryBuilder parser = new QueryParser("premiseText", analyzer);
        Query query = parser.createPhraseQuery("premiseText", new AnalyzerModule().analyze("premiseText", argument.conclusion));

        int matchNumber = searcher.count(query);
        if(matchNumber == 0) {
            return result;
        }
        ScoreDoc[] hits = searcher.search(query, matchNumber).scoreDocs;

        for (int i = 0; i < hits.length; i++) {
            Document doc = searcher.doc(hits[i].doc);
            if (new AnalyzerModule().analyze("premiseText", argument.conclusion).length() == new AnalyzerModule().analyze("premiseText",doc.get("premiseText")).length()){
                Map<String,String> match = new HashMap<>();
                match.put("argumentId", doc.get("argumentId"));
                match.put("premiseId", doc.get("premiseId"));
                if(!result.contains(match)){
                    result.add(match);
                }
            }
        }
        return result;
    }
}
