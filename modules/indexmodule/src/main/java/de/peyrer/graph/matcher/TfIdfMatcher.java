package de.peyrer.graph.matcher;

import de.peyrer.analyzermodule.AnalyzerModule;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;

public class TfIdfMatcher extends AbstractSimilarityMatcher {

    @Override
    public Iterable<Map<String, String>> match() throws IOException, ParseException {
        Directory directory = FSDirectory.open(Paths.get(directoryName));
        DirectoryReader iReader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(iReader);
        searcher.setSimilarity(new ClassicSimilarity());

        AnalyzerModule analyzerModule = new AnalyzerModule();
        Analyzer analyzer = analyzerModule.getAnalyzer();
        QueryParser parser = new QueryParser("premiseText", analyzer);

        int wordsCount = new StringTokenizer(stringToMatch).countTokens();

        Iterable<Map<String, String>> matches;
        if (wordsCount == 0) {
            matches = new LinkedList<>();
        } else {
            Query query = parser.createBooleanQuery("premiseText", stringToMatch, BooleanClause.Occur.SHOULD);

            double threshold = Double.parseDouble(System.getenv().get("THRESHOLD_TFIDF"));
            int limit = Integer.parseInt(System.getenv().get("EDGES_LIMIT"));
            matches = this.searchPremiseIndex(searcher, query, limit, threshold);

            for (Map<String,String> match : matches){
                match.remove("score");
            }
        }

        return matches;
    }

    @Override
    public String setStringToMatch(String stringToMatch) {
        this.stringToMatch = stringToMatch;
        return stringToMatch;
    }

    @Override
    public String setArgumentId(String id) {
        this.argumentId = id;
        return argumentId;
    }

    @Override
    public String setDirectoryName_Conclusions(String directoryName_conclusions) {
        this.directoryName_Conclusions = directoryName_conclusions;
        return directoryName_conclusions;
    }

    @Override
    public String setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
        return directoryName;
    }
}
