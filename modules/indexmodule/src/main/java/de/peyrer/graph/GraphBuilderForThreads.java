package de.peyrer.graph;

import de.peyrer.indexmodule.InvalidSettingValueException;
import de.peyrer.model.Argument;
import de.peyrer.repository.ArgumentRepository;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GraphBuilderForThreads implements IGraphBuilder {

    private AbstractDirectedGraph graph;

    private ArgumentRepository repository;

    private Matcher matcher;

    private static final String AND = "AND";
    private static final String PHRASE = "PHRASE";

    public GraphBuilderForThreads(IGraphBuilder.GraphType graphType) throws InvalidSettingValueException {
        this.repository = new ArgumentRepository();

        switch(graphType){
            case JGRAPHT:
                this.graph = new JGraphTAdapter();
        }
    }

    @Override
    public IDirectedGraph build(String premiseIndex, String conclusionIndex) {
        return null;
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

        ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(1);
        scheduledExecutor.scheduleAtFixedRate(() -> {
            System.out.println("Progress of graph building at " + java.time.ZonedDateTime.now() + " : "
                    + threadPoolExecutor.getCompletedTaskCount() + " arguments have already been processed, at least "
                    + threadPoolExecutor.getQueue().size() + " arguments remain.");
        }, 30,30, TimeUnit.SECONDS);

        for(Argument argument : arguments){
            threadPoolExecutor.submit(new MatchThread(graph, argument, premiseIndex));
        }

        threadPoolExecutor.shutdown();
        threadPoolExecutor.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);

        scheduledExecutor.shutdownNow();

        System.out.println("Number of edges: " + graph.getNumberOfEdges());

        return graph;
    }
}
