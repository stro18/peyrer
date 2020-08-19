package de.peyrer.querybuilder;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;

public interface IQueryBuilder {
    public Query getQuery(String query) throws ParseException;
}
