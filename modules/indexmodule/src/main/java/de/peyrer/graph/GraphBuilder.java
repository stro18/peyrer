package de.peyrer.graph;

import de.peyrer.model.Argument;
import de.peyrer.repository.ArgumentRepository;

import java.util.Map;

public class GraphBuilder {

    private AbstractDirectedGraph graph;

    private ArgumentRepository repository;

    private Matcher matcher;

    public enum GraphType {
        JGRAPHT
    }

    public enum MatcherType {
        AND
    }

    public GraphBuilder(GraphType graphType, MatcherType matcherType){
        this.repository = new ArgumentRepository();

        switch(matcherType){
            case AND:
                this.matcher = new AndMatcher();
        }

        switch(graphType){
            case JGRAPHT:
                this.graph = new JGraphTAdapter();
        }
    }

    public IDirectedGraph build(){
        if(graph instanceof JGraphTAdapter){
            return buildJGraphT();
        }
        return null;
    };

    public void saveToDatabase(IDirectedGraph graph, Map<String,Double> pageRank, Map<String,Double> relevance){
        return;
    };

    private IDirectedGraph buildJGraphT(){
        Iterable<Argument> arguments = repository.readAll();

        for(Argument argument : arguments){
            graph.addVertex(argument.id);

            matcher.setStringToMatch(argument.conclusion);
            Iterable<Map<String,String>> matches = matcher.match();

            for(Map<String,String> match : matches){
                graph.addVertex(match.get("argumentId"));
                graph.addEdge(match.get("argumentId"), argument.id, match.get("premiseId"));
            }
        }

        return graph;
    }
}
