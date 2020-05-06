package de.peyrer.model;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonString;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Argument {

    public String id;

    public String conclusion;

    public String[] premises;

    public double relevance;

    public double pageRank;

    public Argument(String id, String conclusion, String[] premises){
        this.id = id;
        this.conclusion = conclusion;
        this.premises = premises;
    }

    public Argument(BsonDocument bson)
    {
        this.id = bson.getString("id").getValue();
        this.conclusion = bson.getString("conclusion").getValue();
        this.premises = bson.getArray("premises").getValues()
                .stream()
                .map(s -> s.asDocument().getString("text").getValue())
                .collect(Collectors.toList())
                .toArray(new String[0]);

        this.relevance = bson.isDouble("relevance") ? bson.getDouble("relevance").getValue() : 0.0;
        this.pageRank = bson.isDouble("pageRank") ? bson.getDouble("pageRank").getValue() : 0.0;
    }

    public BsonDocument toBson(){
        BsonDocument bson = new BsonDocument();

        bson.append("id", new BsonString(id));
        bson.append("conclusion", new BsonString(conclusion));
        bson.append("premises", new BsonArray(
                Arrays.stream(premises)
                        .map(s -> (new BsonDocument()).append("text", new BsonString(s)))
                        .collect(Collectors.toList())
        ));
        bson.append("relevance", new BsonDouble(relevance));
        bson.append("pageRank", new BsonDouble(pageRank));

        return bson;
    }
}
