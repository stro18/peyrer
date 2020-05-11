package de.peyrer.indexer;

import java.io.IOException;

public interface IIndexer {

    public void index() throws IOException;

    public String getIndexPath() throws IOException;
}
