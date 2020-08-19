package de.peyrer.retriever;

import java.io.IOException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import de.peyrer.model.Argument;

public class ResultIterator extends AbstractResultIterator {
    
    private Query query;
    private int pageSize = 10;
    private ScoreDoc after = null;
    private ResultPageIterator resultPageIterator;
    public ResultIterator(IndexSearcher indexSearcher, Query query) {
    	super(indexSearcher);
    	this.query = query;
		this.resultPageIterator = new ResultPageIterator(this.nextPage(), this.searcher);
    }
    
    public ResultIterator(IndexSearcher indexSearcher, Query query, int pageSize) {
		super(indexSearcher);
		this.query = query;
		this.pageSize = pageSize;
		this.resultPageIterator = new ResultPageIterator(this.nextPage(), this.searcher);
	}

    private TopDocs nextPage() {
		try {
			if(after != null) return this.searcher.searchAfter(this.after, this.query, this.pageSize);
			else return this.searcher.search(this.query, this.pageSize);
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}

    @Override
	public boolean hasNext() {
		if(this.resultPageIterator.hasNext()) return this.resultPageIterator.hasNext();
		else {
			this.resultPageIterator.setPage(this.nextPage());
			return this.resultPageIterator.hasNext();
		}
    }

    @Override
    public Argument next() {
		if(this.resultPageIterator.hasNext()) return this.resultPageIterator.next();
		else {
			this.resultPageIterator.setPage(this.nextPage());
			return this.resultPageIterator.next();
		}
    }
}