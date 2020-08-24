package de.peyrer.graph.matcher;

import de.peyrer.analyzermodule.AnalyzerModule;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

public class TfIdfMatcher extends AbstractSimilarityMatcher {

    @Override
    public Iterable<Map<String, String>> match() throws IOException, ParseException {
        Directory directory = FSDirectory.open(Paths.get(directoryName));
        DirectoryReader iReader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(iReader);
        searcher.setSimilarity(new ClassicSimilarity());

        Analyzer analyzer = (new AnalyzerModule()).getAnalyzer();
        QueryParser parser = new QueryParser("conclusionText", analyzer);

        Query query = parser.parse(stringToMatch);

        Iterable<Map<String,String>> matches = this.searchPremiseIndex(searcher, query, 100, 0.8);

        for (Map<String,String> match : matches){
            match.remove("score");
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
        this.argumentId = argumentId;
        return argumentId;
    }

    @Override
    public String setDirectoryName_Conclusions(String directoryName_conclusions) {
        return null;
    }

    @Override
    public String setDirectoryName(String directoryName) {
        return null;
    }
}
