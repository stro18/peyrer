package de.peyrer.retrievalmodule;

import java.io.IOException;
import java.nio.file.Paths;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import de.peyrer.querybuilder.IQueryBuilder;

public class RetrievalModule implements IRetrievalModule{

	private String indexPath = "./index";
	private IndexSearcher indexSearcher;
	private IQueryBuilder queryBuilder;

	public RetrievalModule(IQueryBuilder queryBuilder, String indexPath) throws IOException {
		this.indexPath = indexPath;
		this.indexSearcher = createIndexSearcher(indexPath);
		this.queryBuilder = queryBuilder;
	}

	public void setIndexPath(String indexPath) throws IOException {
		this.indexPath = indexPath;
		this.indexSearcher = createIndexSearcher(this.indexPath);
	}

	public String getArgument(int doc) throws IOException {
		return this.indexSearcher.doc(doc).get("argumentId");
	}

	public TopDocs getResults(String query, int amt) throws ParseException, IOException {
		return this.indexSearcher.search(this.queryBuilder.getQuery(query), amt);
	}
	
	private static IndexSearcher createIndexSearcher(String indexPath) throws IOException {
		IndexReader indexReader;
		indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		return indexSearcher;
	}

}
