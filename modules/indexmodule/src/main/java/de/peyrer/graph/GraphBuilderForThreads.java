package de.peyrer.graph;

import de.peyrer.indexmodule.InvalidSettingValueException;
import de.peyrer.model.Argument;
import de.peyrer.repository.ArgumentRepository;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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

    public IDirectedGraph build(String premiseIndex) throws InterruptedException {
        if(graph instanceof JGraphTAdapter){
            return buildJGraphT(premiseIndex);
        }
        return null;
    }

    private IDirectedGraph buildJGraphT(String premiseIndex) throws InterruptedException {
        Iterable<Argument> arguments = repository.readAll();

        int threadPoolSize = Integer.parseInt(System.getenv().get("THREAD_POOL_SIZE"));
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadPoolSize);

        for(Argument argument : arguments){
            threadPoolExecutor.submit(new MatchThread(graph, argument, premiseIndex));
        }

        threadPoolExecutor.shutdown();

        ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(5);
        scheduledExecutor.schedule(() -> {
            System.out.println("Progress of graph building at " + java.time.ZonedDateTime.now() + " : "
                    + threadPoolExecutor.getCompletedTaskCount() + " arguments have already been processed, "
                    + threadPoolExecutor.getQueue().size() + " arguments remain.");
        }, 30, TimeUnit.SECONDS);

        threadPoolExecutor.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);

        scheduledExecutor.shutdownNow();

        System.out.println("Number of edges: " + graph.getNumberOfEdges());

        return graph;
    }
}
