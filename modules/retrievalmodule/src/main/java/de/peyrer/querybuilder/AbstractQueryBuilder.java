package de.peyrer.querybuilder;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;

import de.peyrer.analyzermodule.AnalyzerModule;
import org.apache.lucene.search.QueryVisitor;
import org.apache.lucene.search.TermQuery;

import java.io.IOException;

public abstract class AbstractQueryBuilder implements IQueryBuilder {

	@Override
	public abstract Query getQuery(String queryString) throws ParseException, IOException;

	protected Query getOriginalQuery(String queryString) throws ParseException, IOException {
		AnalyzerModule analyzerModule = new AnalyzerModule();
		Analyzer analyzer = analyzerModule.getAnalyzer();
		QueryParser parser = new QueryParser("conclusion", analyzer);
		return parser.parse(analyzerModule.analyze("conclusion", queryString));
	}
}
