package de.peyrer.repository;

import com.mongodb.client.MongoCollection;
import de.peyrer.connection.MongoConnector;
import de.peyrer.model.Argument;
import org.bson.BsonDocument;
import org.bson.BsonObjectId;
import org.bson.BsonString;

public class ArgumentRepository implements IArgumentRepository {

    private MongoCollection<BsonDocument> collection;

    public ArgumentRepository(){
        this.collection = (new MongoConnector()).getCollection("args");
    }

    @Override
    public Iterable<Argument> readAll() {
        return null;
    }

    @Override
    public Argument readById(String id) {
        BsonDocument bsonTemplate = new BsonDocument("id", new BsonString(id));
        BsonDocument bsonResult = this.collection.find(bsonTemplate).first();

        return bsonResult != null ? new Argument(bsonResult) : null;
    }

    @Override
    public Argument create(Argument entity) {
         BsonObjectId bsonId = collection.insertOne(entity.toBson()).getInsertedId().asObjectId();
         return bsonId != null ? entity : null;
    }

    @Override
    public Argument update(Argument entity) {
        return null;
    }

    @Override
    public Argument delete(Argument entity) {
        return null;
    }
}
