package de.peyrer.indexer;

import java.io.IOException;

public interface IIndexer {

    public void indexPrem() throws IOException;

    public void indexConc();
}
