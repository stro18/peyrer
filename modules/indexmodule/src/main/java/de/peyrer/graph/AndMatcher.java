package de.peyrer.graph;

import de.peyrer.indexer.ConclusionIndexer;
import de.peyrer.indexmodule.Indexmodule;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

class AndMatcher extends AbstractMatcher {

    @Override
    public Iterable<Map<String,String>> match() throws IOException {
        LinkedList<Map<String,String>> result = new LinkedList<>();

        //directory for premise index
        Directory directory = null;
        DirectoryReader iReader = null;
        //directory for conclusion index
        Directory directory_Conclusions = null;
        DirectoryReader iReader_Conclusions = null;

        try {
            directory = FSDirectory.open(Paths.get(directoryName));
            iReader = DirectoryReader.open(directory);
            //search the conclusions
            directory_Conclusions = FSDirectory.open(Paths.get(directoryName_Conclusions));
            iReader_Conclusions = DirectoryReader.open(directory_Conclusions);
        } catch (IOException e) {
            e.printStackTrace();
        }

        IndexSearcher searcher = new IndexSearcher(iReader);
        IndexSearcher searcher_Conclusions = new IndexSearcher(iReader_Conclusions);
        Analyzer analyzer = (new Indexmodule()).getAnalyzer();

        QueryParser parser = new QueryParser("premiseText", analyzer);
        Query query = parser.createBooleanQuery("premiseText", stringToMatch, BooleanClause.Occur.MUST);

        ScoreDoc[] hits = null;
        ScoreDoc[] conclusions = null;

        int matchNumber = searcher.count(query);
        if(matchNumber == 0) {
            return result;
        }
        hits = searcher.search(query, matchNumber).scoreDocs;

        for (int i = 0; i < hits.length; i++) {
            Document doc = searcher.doc(hits[i].doc);
            //make a query to find if premise is equals conclusion
            QueryParser parser_Conclusions = new QueryParser("conclusionText",analyzer);
            Query query_conclusion;
            try {
                query_conclusion = parser_Conclusions.createBooleanQuery("conclusionText", doc.get("premiseText").toString(), BooleanClause.Occur.MUST);
            }catch(BooleanQuery.TooManyClauses e){
                continue;
            }

            int matchNumberConclusion = searcher_Conclusions.count(query_conclusion);
            if(matchNumberConclusion == 0) {
                continue;
            }
            conclusions = searcher_Conclusions.search(query_conclusion, matchNumberConclusion).scoreDocs;

            //if a premise can be found in the conclusionIndex then add it to the current map if it equals the value of the setArgumentId
            for (int j = 0; j < conclusions.length; j++) {
                Document concs = searcher_Conclusions.doc(conclusions[j].doc);
                if (concs.get("argumentId").equals(argumentId)) {
                    Map<String,String> match = new HashMap<>();
                    match.put("argumentId", doc.get("argumentId"));
                    match.put("premiseId", doc.get("premiseId"));
                    if(!result.contains(match)){
                        result.add(match);
                    }
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
