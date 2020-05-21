package de.peyrer.querybuilder;

import org.apache.lucene.expressions.Expression;
import org.apache.lucene.expressions.SimpleBindings;
import org.apache.lucene.expressions.js.JavascriptCompiler;
import org.apache.lucene.queries.function.FunctionScoreQuery;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;

public class DocValueFieldQueryBuilder extends AbstractQueryBuilder {

	private float coefficient = 1.0f;
	
	public DocValueFieldQueryBuilder(float coefficient) {
		this.coefficient = coefficient;
	}
	
	@Override
	public Query getQuery(String queryString) throws ParseException {
		Expression expr;
		try {
			expr = JavascriptCompiler.compile(coefficient + " * relevance");
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			throw new ParseException("Could not compile JavaScript expression: " + e.getMessage());
		}
		
		SimpleBindings bindings = new SimpleBindings();
		bindings.add(new SortField("relevance", SortField.Type.SCORE));
		
		return new FunctionScoreQuery(this.getOriginalQuery(queryString), expr.getDoubleValuesSource(bindings));
	}

}
