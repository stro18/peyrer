package de.peyrer.indexer;

import de.peyrer.indexmodule.InvalidSettingValueException;

import java.io.IOException;

public interface IIndexer {

    public void index() throws IOException, InvalidSettingValueException;

    public String getIndexPath() throws IOException;
}
