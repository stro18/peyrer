package de.peyrer.graph;

import de.peyrer.analyzermodule.AnalyzerModule;
import de.peyrer.indexmodule.Indexmodule;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class PhraseMatcher extends AbstractMatcher {

    private IndexSearcher searcher;

    private QueryParser parser;

    @Override
    public Iterable<Map<String,String>> match() throws IOException {

        LinkedList<Map<String,String>> result = new LinkedList<>();
        Directory directory = FSDirectory.open(Paths.get(directoryName));
        DirectoryReader iReader = DirectoryReader.open(directory);
        this.searcher = new IndexSearcher(iReader);
        Analyzer analyzer = (new AnalyzerModule()).getAnalyzer();
        this.parser = new QueryParser("premiseText", analyzer);
        Query query = this.parser.createPhraseQuery("premiseText", new AnalyzerModule().analyze("premiseText",stringToMatch));

        ScoreDoc[] hits = null;

        int matchNumber = this.searcher.count(query);
        if(matchNumber == 0) {
            return result;
        }
        hits = this.searcher.search(query, matchNumber).scoreDocs;

        for (int i = 0; i < hits.length; i++) {
            Document doc = this.searcher.doc(hits[i].doc);
            if (new AnalyzerModule().analyze("premiseText",stringToMatch).length() == new AnalyzerModule().analyze("premiseText",doc.get("premiseText")).length()){
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

    @Override
    public String setStringToMatch(String stringToMatch) {
        this.stringToMatch = stringToMatch;
        return this.stringToMatch;
    }

    @Override
    public String setArgumentId(String argumentId) {
        this.argumentId = argumentId;
        return this.argumentId;
    }

    @Override
    public String setDirectoryName_Conclusions(String directoryName_conclusions){
        this.directoryName_Conclusions = directoryName_conclusions;
        return this.directoryName_Conclusions;
    }

    @Override
    public String setDirectoryName(String directoryName){
        this.directoryName = directoryName;
        return this.directoryName;
    }
}
