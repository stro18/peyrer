package de.peyrer.retrievalmodule;

import de.peyrer.model.Argument;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;

public interface IRetrievalModule {
    public String getArgument(int doc) throws IOException;
    public TopDocs getResults(String query, int amt) throws ParseException, IOException;
    public void setIndexPath(String indexPath) throws IOException;
}
