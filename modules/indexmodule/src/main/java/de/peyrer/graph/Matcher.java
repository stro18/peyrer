package de.peyrer.graph;

public interface Matcher {

    // Returns array of ids of matching arguments
    String[] match();

    String setStringToMatch(String stringToMatch);
}
