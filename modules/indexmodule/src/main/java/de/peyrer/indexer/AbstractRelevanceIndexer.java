package de.peyrer.indexer;

import java.util.Map;

public abstract class AbstractRelevanceIndexer extends AbstractIndexer {

    protected Map<String,Double> relevance;

    public void setRelevance(Map<String,Double> relevance){
        this.relevance = relevance;
    }
}
