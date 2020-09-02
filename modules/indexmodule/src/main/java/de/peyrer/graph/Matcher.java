package de.peyrer.graph;

import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.Map;

public interface Matcher {

    // Returns array of ids of matching arguments
    Iterable<Map<String,String>> match() throws IOException, ParseException;

    String setStringToMatch(String stringToMatch);

    String setArgumentId(String id);

    String setDirectoryName_Conclusions(String directoryName_conclusions);

    String setDirectoryName(String directoryName);
}
