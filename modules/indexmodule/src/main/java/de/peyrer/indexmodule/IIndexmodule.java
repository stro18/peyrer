package de.peyrer.indexmodule;

import java.io.IOException;
import java.util.List;

public interface IIndexmodule {

    public void indexWithRelevance() throws IOException, InvalidSettingValueException;

    public String getIndexPath();
}
