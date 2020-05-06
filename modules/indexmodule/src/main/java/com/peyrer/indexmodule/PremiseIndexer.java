package com.peyrer.indexmodule;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.Arrays;


public class PremiseIndexer {

    public MongoCollection<Document> coll;

    public PremiseIndexer(){
        Connector connector = new Connector();
        this.coll = connector.getCollection("argument");
    }

}
