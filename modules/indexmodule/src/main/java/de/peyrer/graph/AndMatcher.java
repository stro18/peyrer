package de.peyrer.graph;

public class AndMatcher implements Matcher {

    private String stringToMatch;

    @Override
    public String[] match() {
        if(!stringToMatch.equals("Mine Ban Treaty (Ottawa Treaty)")) {
            return new String[]{"S96f2396e-A2f68e3d2"};
        }else {
            return new String[0];
        }
    }

    @Override
    public String setStringToMatch(String stringToMatch) {
        this.stringToMatch = stringToMatch;
        return this.stringToMatch;
    }
}
