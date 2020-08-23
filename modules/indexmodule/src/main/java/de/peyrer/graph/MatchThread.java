package de.peyrer.graph;

import de.peyrer.indexmodule.InvalidSettingValueException;
import de.peyrer.model.Argument;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;

public class MatchThread implements Callable<Integer> {

    private final AbstractDirectedGraph graph;

    private final Argument argument;

    private final String premiseIndexPath;

    private final Matcher matcher;

    private static final String AND = "AND";
    private static final String PHRASE = "PHRASE";
    private static final String PHRASE_THREAD = "PHRASE_THREAD";

    MatchThread(AbstractDirectedGraph graph, Argument argument, String premiseIndexPath) throws InvalidSettingValueException {
        this.graph = graph;
        this.argument = argument;
        this.premiseIndexPath = premiseIndexPath;

        String matcherType = System.getenv().get("MATCHING");
        switch(matcherType){
            case AND:
                this.matcher = new AndMatcher();
                break;
            case PHRASE:
            case PHRASE_THREAD:
                this.matcher = new PhraseMatcher();
                break;
            default:
                throw new InvalidSettingValueException("The setting MATCHING=" + matcherType +  " is not allowed!");
        }
    }

    @Override
    public Integer call() {
        matcher.setArgumentId(argument.id);
        matcher.setStringToMatch(argument.conclusion);
        matcher.setDirectoryName(premiseIndexPath);

        Iterable<Map<String,String>> matches;
        try {
            matches = matcher.match();
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }

        graph.addVertex(argument.id);

        for(Map<String,String> match : matches){
            graph.addVertex(match.get("argumentId"));
            graph.addEdge(match.get("argumentId"), argument.id, match.get("premiseId"));
        }

        return 0;
    }
}
