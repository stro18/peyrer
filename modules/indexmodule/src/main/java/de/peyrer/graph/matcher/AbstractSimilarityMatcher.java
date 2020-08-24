package de.peyrer.graph.matcher;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class AbstractSimilarityMatcher implements ISimilarityMatcher
{
    protected Iterable<Map<String,String>> searchPremiseIndex(IndexSearcher searcher, Query query, int limit, int threshold) throws IOException {
        LinkedList<Map<String,String>> result = new LinkedList<>();

        ScoreDoc[] hits = searcher.search(query, limit).scoreDocs;

        for (int i = 0; i < hits.length; i++) {
            if (threshold != 0 && hits[i].score < threshold){
                break;
            }

            Document doc = searcher.doc(hits[i].doc);
            Map<String,String> found = new HashMap<>();
            found.put("argumentId", doc.get("argumentId"));
            found.put("score", String.valueOf(hits[i].score));
            result.add(found);
        }

        return result;
    }

    protected Iterable<Map<String,String>> searchConclusionIndex(IndexSearcher searcher, Query query, int limit, int threshold) throws IOException {
        return new LinkedList<>();
    }
}
