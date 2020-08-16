module indexmodule {
    requires databasemodule;
    requires analyzermodule;
    requires org.jgrapht.core;
    requires lucene.core;
    requires lucene.queries;
    requires lucene.queryparser;

    exports de.peyrer.indexmodule;
}