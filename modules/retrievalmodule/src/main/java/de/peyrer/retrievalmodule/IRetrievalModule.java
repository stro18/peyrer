package de.peyrer.retrievalmodule;

import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;

public interface IRetrievalModule {
    public Results getResults(String query, int amt) throws ParseException, IOException;
    public void setIndexPath(String indexPath) throws IOException;
}
