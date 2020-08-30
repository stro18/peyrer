package de.peyrer.graph;

import de.peyrer.analyzermodule.AnalyzerModule;
import de.peyrer.graph.matcher.BM25Matcher;
import de.peyrer.graph.matcher.TfIdfMatcher;
import de.peyrer.graph.matcher.TfIdfWeightedMatcher;
import de.peyrer.indexmodule.InvalidSettingValueException;
import de.peyrer.model.Argument;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;

public class MatchThread implements Callable<Integer> {

    private final AbstractDirectedGraph graph;

    private final Argument argument;

    private final String premiseIndexPath;

    private final String conclusionIndexPath;

    private final Matcher matcher;

    private final AnalyzerModule analyzerModule;

    private boolean processPremises = false;

    private static final String AND = "AND";
    private static final String PHRASE = "PHRASE";
    private static final String PHRASE_PREMISE = "PHRASE_PREMISE";
    private static final String TFIDF = "TFIDF";
    private static final String TFIDF_WEIGHTED = "TFIDF_WEIGHTED";
    private static final String BM25 = "BM25";

    MatchThread(AbstractDirectedGraph graph, Argument argument, String premiseIndexPath, String conclusionIndexPath)
            throws InvalidSettingValueException {
        this.graph = graph;
        this.argument = argument;
        this.premiseIndexPath = premiseIndexPath;
        this.conclusionIndexPath = conclusionIndexPath;
        this.analyzerModule = new AnalyzerModule();

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
                this.processPremises = true;
                break;
            case TFIDF:
                this.matcher = new TfIdfMatcher();
                break;
            case TFIDF_WEIGHTED:
                this.matcher = new TfIdfWeightedMatcher();
                break;
            case BM25:
                this.matcher = new BM25Matcher();
                break;
            default:
                throw new InvalidSettingValueException("The setting MATCHING=" + matcherType +  " is not allowed!");
        }
    }

    @Override
    public Integer call() {
        if (processPremises) {
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
            try {
                premise = analyzerModule.analyze("conclusionText", premise);
            } catch (IOException e) {
                e.printStackTrace();
            }

            matcher.setStringToMatch(premise);

            Iterable<Map<String,String>> matches;
            try {
                matches = matcher.match();
                this.logLengthWithScore(premise, matches);
            } catch (IOException | ParseException e) {
                e.printStackTrace();
                return 1;
            }

            for(Map<String,String> match : matches){
                graph.addVertex(match.get("argumentId"));

                try {
                    if (weighted()) {
                        graph.addEdge(argument.id, match.get("argumentId"), String.valueOf(premiseId), Double.parseDouble(match.get("score")));
                    } else {
                        graph.addEdge(argument.id, match.get("argumentId"), String.valueOf(premiseId), 1);
                    }
                } catch (InvalidSettingValueException e) {
                    e.printStackTrace();
                    return 1;
                }
            }

            premiseId++;
        }

        return 0;
    }

    private Integer processConclusion()
    {
        String conclusion = null;
        try {
            conclusion = analyzerModule.analyze("premiseText", argument.conclusion);
        } catch (IOException e) {
            e.printStackTrace();
        }

        matcher.setStringToMatch(conclusion);
        matcher.setDirectoryName(premiseIndexPath);

        Iterable<Map<String,String>> matches;
        try {
            matches = matcher.match();
            this.logLengthWithScore(conclusion, matches);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return 1;
        }

        graph.addVertex(argument.id);

        for(Map<String,String> match : matches){
            graph.addVertex(match.get("argumentId"));

            try {
                if (weighted()) {
                    graph.addEdge(match.get("argumentId"), argument.id, match.get("premiseId"), Double.parseDouble(match.get("score")));
                } else {
                    graph.addEdge(match.get("argumentId"), argument.id, match.get("premiseId"), 1);
                }
            } catch (InvalidSettingValueException e) {
                e.printStackTrace();
                return 1;
            }
        }

        return 0;
    }

    private boolean weighted() throws InvalidSettingValueException {
        String matcherType = System.getenv().get("MATCHING");
        switch(matcherType){
            case AND:
            case PHRASE:
            case PHRASE_PREMISE:
            case TFIDF:
                return false;
            case TFIDF_WEIGHTED:
            case BM25:
                return true;
            default:
                throw new InvalidSettingValueException("The setting MATCHING=" + matcherType +  " is not allowed!");
        }
    }

    private void logLengthWithScore(String stringToMatch, Iterable<Map<String,String>> matches) throws IOException
    {
        int wordsCount = new StringTokenizer(stringToMatch).countTokens();

        if (wordsCount <= 4 || wordsCount >= 10) {
            int count = 0;
            double sum = 0;
            for (Map<String,String> match : matches) {
                count++;
                sum += match.get("score") == null ? 1 : Double.parseDouble(match.get("score"));
            }

            double average = Double.isNaN(sum/count) ? 0 : sum/count;

            if (wordsCount <= 4) {
                GraphBuilderForThreads.numberOfPhrasesWithLowLength++;
                GraphBuilderForThreads.averageScoreForPhrasesWithLowLength += average;
            } else {
                // wordsCount >= 10
                GraphBuilderForThreads.numberOfPhrasesWithHighLength++;
                GraphBuilderForThreads.averageScoreForPhrasesWithHighLength += average;
            }
        }
    }
}
