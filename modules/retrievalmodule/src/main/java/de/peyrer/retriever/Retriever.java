package de.peyrer.retriever;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import de.peyrer.model.Argument;

public class Retriever {
	
	private IndexSearcher indexSearcher;
	
	public Retriever(IndexSearcher indexSearcher) {
		this.indexSearcher = indexSearcher;
	}

    public Iterable<Argument> retrieve(Query query) {
    	return new ResultIterable(new ResultIterator(this.indexSearcher, query));
    }
    
}