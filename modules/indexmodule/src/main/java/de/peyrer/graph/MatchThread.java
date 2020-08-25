package de.peyrer.graph;

import de.peyrer.graph.matcher.TfIdfMatcher;
import de.peyrer.indexmodule.InvalidSettingValueException;
import de.peyrer.model.Argument;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;

public class MatchThread implements Callable<Integer> {

    private final AbstractDirectedGraph graph;

    private final Argument argument;

    private final String premiseIndexPath;

    private final String conclusionIndexPath;

    private final Matcher matcher;

    private static final String AND = "AND";
    private static final String PHRASE = "PHRASE";
    private static final String PHRASE_PREMISE = "PHRASE_PREMISE";
    private static final String TFIDF = "TFIDF";

    MatchThread(AbstractDirectedGraph graph, Argument argument, String premiseIndexPath, String conclusionIndexPath)
            throws InvalidSettingValueException {
        this.graph = graph;
        this.argument = argument;
        this.premiseIndexPath = premiseIndexPath;
        this.conclusionIndexPath = conclusionIndexPath;

        String matcherType = System.getenv().get("MATCHING");
        switch(matcherType){
            case AND:
                this.matcher = new AndMatcher();
                break;
            case PHRASE:
                this.matcher = new PhraseMatcher();
                break;
            case PHRASE_PREMISE:
                this.matcher = new PhraseMatcherForPremises();
                break;
            case TFIDF:
                this.matcher = new TfIdfMatcher();
                break;
            default:
                throw new InvalidSettingValueException("The setting MATCHING=" + matcherType +  " is not allowed!");
        }
    }

    @Override
    public Integer call() {
        if (this.matcher instanceof PhraseMatcherForPremises) {
            return this.processPremises();

        } else {
            return this.processConclusion();
        }
    }

    private Integer processPremises(){
        matcher.setDirectoryName_Conclusions(conclusionIndexPath);
        graph.addVertex(argument.id);

        int premiseId = 0;
        for (String premise : argument.premises) {
            matcher.setStringToMatch(premise);

            Iterable<Map<String,String>> matches;
            try {
                matches = matcher.match();
            } catch (IOException | ParseException e) {
                e.printStackTrace();
                return 1;
            }

            for(Map<String,String> match : matches){
                graph.addVertex(match.get("argumentId"));
                graph.addEdge(argument.id, match.get("argumentId"), String.valueOf(premiseId), 1);
            }

            premiseId++;
        }

        return 0;
    }

    private Integer processConclusion(){
        matcher.setStringToMatch(argument.conclusion);
        matcher.setDirectoryName(premiseIndexPath);

        Iterable<Map<String,String>> matches;
        try {
            matches = matcher.match();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return 1;
        }

        graph.addVertex(argument.id);

        for(Map<String,String> match : matches){
            graph.addVertex(match.get("argumentId"));
            graph.addEdge(match.get("argumentId"), argument.id, match.get("premiseId"), 1);
        }

        return 0;
    }
}
