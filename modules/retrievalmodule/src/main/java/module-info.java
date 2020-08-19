module retrievalmodule {
	requires lucene.core;
	requires databasemodule;
	requires lucene.queryparser;
	requires lucene.expressions;
	requires lucene.queries;
	requires analyzermodule;

	exports de.peyrer.retrievalmodule;
}
