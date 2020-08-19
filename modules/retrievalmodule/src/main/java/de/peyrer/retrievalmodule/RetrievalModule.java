package de.peyrer.retrievalmodule;

import java.io.IOException;
import java.nio.file.Paths;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

import de.peyrer.model.Argument;
import de.peyrer.querybuilder.IQueryBuilder;
import de.peyrer.retriever.Retriever;

public class RetrievalModule implements IRetrievalModule{

	private String indexPath = "./index";
	private Retriever retriever;
	private IQueryBuilder queryBuilder;

	public RetrievalModule(IQueryBuilder queryBuilder) {
		this.retriever = createRetriever(this.indexPath);
		this.queryBuilder = queryBuilder;
	}

	public void setIndexPath(String indexPath) {
		this.indexPath = indexPath;
		this.retriever = createRetriever(this.indexPath);
	}

	public Iterable<Argument> getArguments(String query) {
		try {
			return this.retriever.retrieve(this.queryBuilder.getQuery(query));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private static Retriever createRetriever(String indexPath) {
		IndexReader indexReader;
		try {
			indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		return new Retriever(indexSearcher);
	}

}
