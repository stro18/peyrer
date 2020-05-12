package de.peyrer.repository;

import com.mongodb.client.MongoCollection;
import de.peyrer.connection.MongoConnector;
import de.peyrer.model.Argument;
import de.peyrer.model.ArgumentIterable;
import org.bson.BsonDocument;
import org.bson.BsonObjectId;
import org.bson.BsonString;

import java.util.Objects;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class ArgumentRepository implements IArgumentRepository {

    private MongoCollection<BsonDocument> collection;

    public ArgumentRepository(){
        this.collection = (new MongoConnector()).getCollection("args");
    }

    @Override
    public Iterable<Argument> readAll() {
        return new ArgumentIterable(collection.find());
    }

    @Override
    public Argument readById(String id) {
        BsonDocument bsonTemplate = new BsonDocument("id", new BsonString(id));
        BsonDocument bsonResult = this.collection.find(bsonTemplate).first();

        return bsonResult != null ? new Argument(bsonResult) : null;
    }

    @Override
    public Argument create(Argument entity) {
         if (collection.find(entity.toBson()).first() == null) {
            BsonObjectId bsonId = Objects.requireNonNull(collection.insertOne(entity.toBson()).getInsertedId()).asObjectId();
            return bsonId != null ? entity : null;
         }
         else{
             return null;
         }
    }

    @Override
    public Argument update(Argument entity, Argument entity2) {
        BsonDocument bsonEntity = entity.toBson();
        return collection.updateOne(Objects.requireNonNull(collection.find(bsonEntity).first()),combine(set("conclusion",entity2.toBson().get("conclusion")),set("premises",entity2.toBson().getArray("premises")))).wasAcknowledged() ? entity : null;
    }

    @Override
    public Argument delete(Argument entity) {
        return collection.deleteOne(entity.toBson()).wasAcknowledged() ? entity : null;
    }
}
