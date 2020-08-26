package de.peyrer.graph.matcher;

import de.peyrer.graph.AbstractMatcher;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class AbstractSimilarityMatcher extends AbstractMatcher implements ISimilarityMatcher
{
    protected Iterable<Map<String,String>> searchPremiseIndex(IndexSearcher searcher, Query query, int limit, double threshold) throws IOException {
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

    protected Iterable<Map<String, String>> normalizeScore(Iterable<Map<String, String>> matches, int base)
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

    protected Iterable<Map<String,String>> searchConclusionIndex(IndexSearcher searcher, Query query, int limit, int threshold) throws IOException {
        return new LinkedList<>();
    }
}
