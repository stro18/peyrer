package de.peyrer.graph;

import de.peyrer.indexmodule.InvalidSettingValueException;
import de.peyrer.model.Argument;
import de.peyrer.repository.ArgumentRepository;

import java.io.IOException;
import java.util.Map;

public class GraphBuilder {

    private AbstractDirectedGraph graph;

    private ArgumentRepository repository;

    private Matcher matcher;

    private static final String AND = "AND";
    private static final String PHRASE = "PHRASE";

    public enum GraphType {
        JGRAPHT
    }

    public GraphBuilder(GraphType graphType) throws InvalidSettingValueException {
        this.repository = new ArgumentRepository();

        String matcherType = System.getenv().get("MATCHING");
        switch(matcherType){
            case AND:
                this.matcher = new AndMatcher();
                break;
            case PHRASE:
                this.matcher = new PhraseMatcher();
                break;
            default:
                throw new InvalidSettingValueException("The setting MATCHING=" + matcherType +  "is not allowed!");
        }

        switch(graphType){
            case JGRAPHT:
                this.graph = new JGraphTAdapter();
        }
    }

    public IDirectedGraph build(String premiseIndex, String conclusionIndex) throws IOException {
        if(graph instanceof JGraphTAdapter){
            return buildJGraphT(premiseIndex, conclusionIndex);
        }
        return null;
    };

    private IDirectedGraph buildJGraphT(String premiseIndex, String conclusionIndex) throws IOException {
        Iterable<Argument> arguments = repository.readAll();
        matcher.setDirectoryName(premiseIndex);
        matcher.setDirectoryName_Conclusions(conclusionIndex);

        int counter = 0;
        for(Argument argument : arguments){
            graph.addVertex(argument.id);

            matcher.setStringToMatch(argument.conclusion);
            matcher.setArgumentId(argument.id);
            Iterable<Map<String,String>> matches = matcher.match();

            for(Map<String,String> match : matches){
                graph.addVertex(match.get("argumentId"));
                graph.addEdge(match.get("argumentId"), argument.id, match.get("premiseId"));
            }

            counter++;
            if (counter % 1000 == 0) {
                System.out.println("Progress of graph building: " + counter + " arguments were processed at " + java.time.ZonedDateTime.now());
            }
        }

        System.out.println("Number of edges: " + graph.getNumberOfEdges());

        return graph;
    }
}
