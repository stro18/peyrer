package de.peyrer.retriever;

import java.util.Iterator;

import org.apache.lucene.search.IndexSearcher;

import de.peyrer.model.Argument;

abstract class AbstractResultIterator implements Iterator<Argument> {
	protected IndexSearcher searcher;
    
    public AbstractResultIterator(IndexSearcher searcher) {
    	this.searcher = searcher;
    }

	@Override
	public abstract boolean hasNext();

	@Override
	public abstract Argument next();
}
