package de.peyrer.connection;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.peyrer.model.Argument;
import org.bson.BsonDocument;
import org.bson.Document;

import java.util.Arrays;

public class MongoConnector {

    private MongoDatabase database;

    public MongoConnector() {
        MongoCredential credential = MongoCredential.createCredential("root", "admin", "example".toCharArray());

        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToClusterSettings(builder ->
                        builder.hosts(Arrays.asList(new ServerAddress("localhost", 27017))))
                .build();

        MongoClient mongoClient = MongoClients.create(settings);

        this.database = mongoClient.getDatabase("peyrer");
    }

    public MongoCollection<BsonDocument> getCollection(String collection){
        return database.getCollection(collection, BsonDocument.class);
    }
}
