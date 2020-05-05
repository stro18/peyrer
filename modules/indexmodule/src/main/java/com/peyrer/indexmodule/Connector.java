package com.peyrer.indexmodule;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Arrays;

public class Connector {

    private MongoDatabase database;

    public Connector() {
        MongoCredential credential = MongoCredential.createCredential("root", "admin", "example".toCharArray());

        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToClusterSettings(builder ->
                        builder.hosts(Arrays.asList(new ServerAddress("localhost", 27017))))
                .build();

        MongoClient mongoClient = MongoClients.create(settings);

        this.database = mongoClient.getDatabase("searchengine");
    }

    public MongoCollection<Document> getCollection(String collection){
        return database.getCollection("argument");
    }


}
