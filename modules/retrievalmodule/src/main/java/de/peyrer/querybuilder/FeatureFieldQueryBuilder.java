package de.peyrer.querybuilder;

import org.apache.lucene.document.FeatureField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.Query;

import java.io.IOException;

public class FeatureFieldQueryBuilder extends AbstractQueryBuilder {

	private float coefficient = 1.0f;
	
	public FeatureFieldQueryBuilder(float coefficient) {
		this.coefficient = coefficient;
	}
	
	@Override
	public Query getQuery(String queryString) throws ParseException, IOException {
		Query originalQuery = this.getOriginalQuery(queryString);
		Query featureQuery = this.createFeatureQuery();
		Query query = new BooleanQuery.Builder()
				.add(originalQuery, Occur.MUST)
				.add(new BoostQuery(featureQuery, this.coefficient), Occur.SHOULD)
				.build();
		return query;
	}

	private Query createFeatureQuery() {
		return FeatureField.newSaturationQuery("feature", "relevance");
	}
}
