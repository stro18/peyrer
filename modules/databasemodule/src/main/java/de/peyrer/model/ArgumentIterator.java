package de.peyrer.model;

import org.bson.BsonDocument;

import java.util.Iterator;

class ArgumentIterator implements Iterator<Argument> {

    private Iterator<BsonDocument> bsonIterator;

    ArgumentIterator(Iterator<BsonDocument> bsonIterator){
        this.bsonIterator = bsonIterator;
    }

    @Override
    public boolean hasNext() {
        return bsonIterator.hasNext();
    }

    @Override
    public Argument next() {
        return new Argument(bsonIterator.next());
    }
}
