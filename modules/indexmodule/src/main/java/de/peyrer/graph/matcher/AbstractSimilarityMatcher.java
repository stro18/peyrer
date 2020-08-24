package de.peyrer.graph.matcher;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class AbstractSimilarityMatcher implements ISimilarityMatcher {

    protected Iterable<Map<String,String>> searchPremiseIndex(IndexSearcher searcher, Query query, int limit, int threshold) throws IOException {
        ScoreDoc[] hits = searcher.search(query, limit).scoreDocs;

        LinkedList<Map<String,String>> result = new LinkedList<>();
        Map<String,String> first = new HashMap<>();
        result.add(first);

        return result;
    }

    protected Iterable<Map<String,String>> searchConclusionIndex(IndexSearcher searcher, Query query, int limit, int threshold) throws IOException {
        return new LinkedList<>();
    }
}
