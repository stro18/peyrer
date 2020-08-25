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
import java.util.Map;

public class TfIdfWeightedMatcher extends AbstractSimilarityMatcher
{
    @Override
    public Iterable<Map<String, String>> match() throws IOException, ParseException {
        Directory directory = FSDirectory.open(Paths.get(directoryName));
        DirectoryReader iReader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(iReader);
        searcher.setSimilarity(new ClassicSimilarity());

        AnalyzerModule analyzerModule = new AnalyzerModule();
        Analyzer analyzer = analyzerModule.getAnalyzer();
        QueryParser parser = new QueryParser("conclusionText", analyzer);

        Query query = parser.createBooleanQuery("premiseText", stringToMatch, BooleanClause.Occur.SHOULD);

        int limit = 10;

        Iterable<Map<String,String>> matches = this.searchPremiseIndex(searcher, query, limit, 0.0);

        return this.normalizeScore(matches, 10 * limit);
    }

    private Iterable<Map<String, String>> normalizeScore(Iterable<Map<String, String>> matches, int base)
    {
        double sum = 0;
        for (Map<String,String> match : matches){
            sum += Double.parseDouble(match.get("score"));
        }

        double normalize = sum/base;

        for (Map<String,String> match : matches){
            String newScore = String.valueOf(Double.parseDouble(match.get("score")) / normalize);

            match.replace("score", newScore);
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
