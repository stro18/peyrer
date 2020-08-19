module retrievalmodule {
	requires lucene.core;
	requires databasemodule;
	requires lucene.queryparser;
	requires lucene.expressions;
	requires lucene.queries;
	
	exports de.peyrer.retrievalmodule;
}