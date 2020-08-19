package de.peyrer.retrievalmodule;

import de.peyrer.model.Argument;

public interface IRetrievalModule {
    public Iterable<Argument> getArguments(String query);
    public void setIndexPath(String indexPath);
}
