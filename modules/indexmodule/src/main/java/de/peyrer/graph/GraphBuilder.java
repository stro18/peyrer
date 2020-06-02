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

    public IDirectedGraph build(String premiseIndex, String conclusionIndex){
        if(graph instanceof JGraphTAdapter){
            return buildJGraphT(premiseIndex, conclusionIndex);
        }
        return null;
    };

    public void saveToDatabase(IDirectedGraph graph, Map<String,Double> pageRank, Map<String,Double> relevance){
        return;
    };

    private IDirectedGraph buildJGraphT(String premiseIndex, String conclusionIndex){
        Iterable<Argument> arguments = repository.readAll();

        for(Argument argument : arguments){
            graph.addVertex(argument.id);

            matcher.setStringToMatch(argument.conclusion);
            matcher.setArgumentId(argument.id);
            matcher.setDirectoryName(premiseIndex);
            matcher.setDirectoryName_Conclusions(conclusionIndex);
            Iterable<Map<String,String>> matches = matcher.match();

            for(Map<String,String> match : matches){
                graph.addVertex(match.get("argumentId"));
                graph.addEdge(match.get("argumentId"), argument.id, match.get("premiseId"));
            }
        }

        System.out.println("Number of edges: " + graph.getNumberOfEdges());

        return graph;
    }
}
