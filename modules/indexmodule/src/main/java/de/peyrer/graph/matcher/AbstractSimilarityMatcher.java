package de.peyrer.graph.matcher;

import de.peyrer.graph.AbstractMatcher;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class AbstractSimilarityMatcher extends AbstractMatcher implements ISimilarityMatcher
{
    protected static boolean logged = false;


    protected Iterable<Map<String,String>> searchPremiseIndex(IndexSearcher searcher, Query query, int limit, double threshold) throws IOException {
        LinkedList<Map<String,String>> result = new LinkedList<>();

        ScoreDoc[] hits = searcher.search(query, limit).scoreDocs;

        for (ScoreDoc hit : hits) {
            if (threshold != 0 && hit.score < threshold) {
                break;
            }

            Document doc = searcher.doc(hit.doc);

            if (!logged) {
                logged = true;
                Explanation explanation = searcher.explain(query, hit.doc);

                this.logExplanation(explanation, 1);
            }

            Map<String, String> found = new HashMap<>();
            found.put("argumentId", doc.get("argumentId"));
            found.put("score", String.valueOf(hit.score));
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

    protected void logExplanation(Explanation explanation, int step)
    {
        if (step == 1) {
            System.out.println("Formula for calculating the matching score (before normalization):");
        }

        for (int i = 0; i < step; i++) {
            System.out.print("  ");
        }

        System.out.println(explanation.getDescription());

        for (Explanation details : explanation.getDetails()) {
            this.logExplanation(details, step + 1);
        }
    }

    protected Iterable<Map<String,String>> searchConclusionIndex(IndexSearcher searcher, Query query, int limit, int threshold) throws IOException {
        return new LinkedList<>();
    }
}
