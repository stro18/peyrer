package de.peyrer.graph;

import de.peyrer.indexmodule.InvalidSettingValueException;
import de.peyrer.model.Argument;
import de.peyrer.repository.ArgumentRepository;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GraphBuilderForThreads {

    private AbstractDirectedGraph graph;

    private ArgumentRepository repository;

    private Matcher matcher;

    private static final String AND = "AND";
    private static final String PHRASE = "PHRASE";

    public enum GraphType {
        JGRAPHT
    }

    public GraphBuilderForThreads(GraphBuilder.GraphType graphType) throws InvalidSettingValueException {
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

    public IDirectedGraph build(String premiseIndex) throws IOException {
        if(graph instanceof JGraphTAdapter){
            return buildJGraphT(premiseIndex);
        }
        return null;
    }

    private IDirectedGraph buildJGraphT(String premiseIndex) throws IOException {
        Iterable<Argument> arguments = repository.readAll();

        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);

        int counter = 0;
        for(Argument argument : arguments){
            executorService.submit(new MatchThread(graph, argument, premiseIndex));

            counter++;
            if (counter % 1000 == 0) {
                System.out.println("Progress of graph building: " + counter + " arguments were processed at " + java.time.ZonedDateTime.now());
                System.out.println("Pool size: " + executorService.getPoolSize());
                System.out.println("Task queue size: " + executorService.getQueue().size());
            }
        }

        executorService.shutdown();
        System.out.println("Shutdown at " + java.time.ZonedDateTime.now());
        try {
            executorService.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Terminated at " + java.time.ZonedDateTime.now());

        System.out.println("Number of edges: " + graph.getNumberOfEdges());

        return graph;
    }
}
