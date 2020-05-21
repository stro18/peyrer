package de.peyrer.querybuilder;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;

public abstract class AbstractQueryBuilder implements IQueryBuilder {
	
	@Override
	public abstract Query getQuery(String queryString) throws ParseException;
	
	protected Query getOriginalQuery(String query) throws ParseException {
		Analyzer analyzer = new StandardAnalyzer();
		QueryParser parser = new QueryParser("conclusion", analyzer);
		return parser.parse(query);
	}
}
