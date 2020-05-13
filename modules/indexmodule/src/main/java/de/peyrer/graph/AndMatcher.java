package de.peyrer.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

class AndMatcher implements Matcher {

    private String stringToMatch;

    @Override
    public Iterable<Map<String,String>> match() {
        LinkedList<Map<String,String>> result = new LinkedList<>();
        if(!stringToMatch.equals("Mine Ban Treaty (Ottawa Treaty)")) {
            Map<String,String> match = new HashMap<>();
            match.put("argumentId", "S96f2396e-A2f68e3d2");
            match.put("premiseId", "1");
            result.add(match);
        }
        return result;
    }

    @Override
    public String setStringToMatch(String stringToMatch) {
        this.stringToMatch = stringToMatch;
        return this.stringToMatch;
    }
}
