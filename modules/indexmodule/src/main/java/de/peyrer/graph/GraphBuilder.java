package de.peyrer.graph;

import de.peyrer.model.Argument;
import de.peyrer.repository.ArgumentRepository;

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

    public void saveToDatabase(IDirectedGraph graph){
        return;
    };

    private IDirectedGraph buildJGraphT(){
        Iterable<Argument> arguments = repository.readAll();

        for(Argument argument : arguments){
            graph.addVertex(argument.id);

            matcher.setStringToMatch(argument.conclusion);
            String[] matches = matcher.match();

            for(String match : matches){
                graph.addEdge(argument.id, match);
            }
        }

        return graph;
    }
}
