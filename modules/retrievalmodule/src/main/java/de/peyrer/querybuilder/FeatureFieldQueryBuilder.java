package de.peyrer.querybuilder;

import org.apache.lucene.document.FeatureField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.Query;

public class FeatureFieldQueryBuilder extends AbstractQueryBuilder {

	private float coefficient = 1.0f;
	
	public FeatureFieldQueryBuilder(float coefficient) {
		this.coefficient = coefficient;
	}
	
	@Override
	public Query getQuery(String queryString) throws ParseException {
		Query originalQuery = this.getOriginalQuery(queryString);
		BooleanQuery.Builder builder = new BooleanQuery.Builder()
				.add(originalQuery, Occur.MUST);
		if(!Boolean.parseBoolean(System.getenv("BASELINE"))) builder.add(this.createFeatureQuery(this.coefficient), Occur.SHOULD);
		Query query = builder.build();
		return query;
	}

	private Query createFeatureQuery(float weight) {
		return FeatureField.newLogQuery("feature", "relevance", weight, 1);
	}
}
