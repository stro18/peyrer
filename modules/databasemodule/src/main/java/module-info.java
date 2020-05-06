module databasemodule {
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;

    exports de.peyrer.model;
    exports de.peyrer.repository;
}