module indexmodule {
    requires databasemodule;
    requires org.jgrapht.core;
    requires lucene.core;
    requires lucene.queries;
    requires lucene.queryparser;

    exports de.peyrer.indexmodule;
}