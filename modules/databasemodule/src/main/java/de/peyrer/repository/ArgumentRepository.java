package de.peyrer.repository;

import com.mongodb.client.MongoCollection;
import de.peyrer.connection.MongoConnector;
import de.peyrer.model.Argument;
import org.bson.BsonDocument;
import org.bson.BsonValue;


public class ArgumentRepository implements IRepository<Argument> {

    private MongoCollection<BsonDocument> collection;

    public ArgumentRepository(){
        this.collection = (new MongoConnector()).getCollection("args");
    }


    @Override
    public Iterable<Argument> readAll() {
        return null;
    }

    @Override
    public Argument readById(String uuid) {
        /*BsonDocument document = new BsonDocument("id", new BsonValue());
        return this.collection.find(document).first();*/
        return null;
    }


    @Override
    public Argument create(Argument entity) {
         /*BsonDocument id = collection.insertOne(entity).getInsertedId();
         return entity.append("id", id);*/
         return null;
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
