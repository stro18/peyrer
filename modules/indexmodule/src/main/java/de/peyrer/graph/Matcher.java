package de.peyrer.graph;

import java.util.Map;

public interface Matcher {

    // Returns array of ids of matching arguments
    Iterable<Map<String,String>> match();

    String setStringToMatch(String stringToMatch);
}
