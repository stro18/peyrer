package de.peyrer.model;

import com.mongodb.client.FindIterable;
import org.bson.BsonDocument;

import java.util.Iterator;

public class ArgumentIterable implements Iterable<Argument> {

    private FindIterable<BsonDocument> bsonIterable;

    public ArgumentIterable(FindIterable<BsonDocument> bsonIterable){
        this.bsonIterable = bsonIterable;
    }

    @Override
    public Iterator<Argument> iterator() {
        return new ArgumentIterator(bsonIterable.iterator());
    }
}
