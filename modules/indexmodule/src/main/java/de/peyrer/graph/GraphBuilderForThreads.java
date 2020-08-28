package de.peyrer.graph;

import de.peyrer.indexmodule.InvalidSettingValueException;
import de.peyrer.model.Argument;
import de.peyrer.repository.ArgumentRepository;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GraphBuilderForThreads implements IGraphBuilder {

    private final AbstractDirectedGraph graph;

    private final ArgumentRepository repository;

    public GraphBuilderForThreads(IGraphBuilder.GraphType graphType) throws InvalidSettingValueException {
        this.repository = new ArgumentRepository();

        switch(graphType){
            case JGRAPHT:
                this.graph = new JGraphTAdapter();
                break;
            case JGRAPHT_WEIGHTED:
                this.graph = new JGraphTAdapterWeighted();
                break;
            default:
                throw new InvalidSettingValueException("The setting GRAPH_TYPE=" + graphType + " is not allowed!");
        }
    }

    @Override
    public IDirectedGraph build(String premiseIndex, String conclusionIndex) throws InvalidSettingValueException, InterruptedException {
        if(graph instanceof AbstractJGraphTAdapter){
            return buildJGraphT(premiseIndex, conclusionIndex);
        }
        return null;
    }

    public IDirectedGraph buildWithPremiseIndex(String premiseIndex) throws InterruptedException, InvalidSettingValueException {
        if(graph instanceof AbstractJGraphTAdapter){
            return buildJGraphT(premiseIndex, "");
        }
        return null;
    }

    public IDirectedGraph buildWithConclusionIndex(String conclusionIndex) throws InterruptedException, InvalidSettingValueException {
        if(graph instanceof AbstractJGraphTAdapter){
            return buildJGraphT("", conclusionIndex);
        }
        return null;
    }

    private IDirectedGraph buildJGraphT(String premiseIndex, String conclusionIndex) throws InterruptedException, InvalidSettingValueException {
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
            threadPoolExecutor.submit(new MatchThread(graph, argument, premiseIndex, conclusionIndex));
        }

        threadPoolExecutor.shutdown();
        threadPoolExecutor.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);

        scheduledExecutor.shutdownNow();

        Map<String,String> analysis = graph.analyse();

        for (Map.Entry<String, String> entry : analysis.entrySet()) {
            System.out.println(entry.getKey() + entry.getValue());
        }

        return graph;
    }
}
