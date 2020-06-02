package de.peyrer.retriever;

import java.io.IOException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;

import de.peyrer.model.Argument;
import de.peyrer.repository.ArgumentRepository;

class ResultPageIterator extends AbstractResultIterator {
	
	private TopDocs page;
	private int idx;
	private ArgumentRepository argRepo = new ArgumentRepository();
	
	public ResultPageIterator(TopDocs page, IndexSearcher searcher) {
		super(searcher);
		this.page = page;
		this.idx = 0;
	}
	
	@Override
	public boolean hasNext() {
		if(page == null) return false;
		if(this.idx < page.scoreDocs.length) return true;
		else return false;
	}

	@Override
	public Argument next() {
		String argId;
    	try {
			argId = this.searcher.doc(this.page.scoreDocs[this.idx].doc).get("argumentId");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
    	idx++;
        return this.argRepo.readById(argId);
	}
	
	public void setPage(TopDocs page) {
		this.page = page;
		this.idx = 0;
	}
}
