package de.peyrer.querybuilder;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;

import java.io.IOException;

public interface IQueryBuilder {
    public Query getQuery(String query) throws ParseException, IOException;
}
