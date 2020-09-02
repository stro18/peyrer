package de.peyrer.indexmodule;

import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.List;

public interface IIndexmodule {

    public void indexWithRelevance() throws IOException, InvalidSettingValueException, InterruptedException, ParseException;

    public String getIndexPath();
}
