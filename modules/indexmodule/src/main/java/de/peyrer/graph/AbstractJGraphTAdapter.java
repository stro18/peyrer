package de.peyrer.graph;

import de.peyrer.repository.ArgumentRepository;
import de.peyrer.repository.IArgumentRepository;
import org.jgrapht.alg.scoring.PageRank;
import org.jgrapht.graph.concurrent.AsSynchronizedGraph;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

public abstract class AbstractJGraphTAdapter extends AbstractDirectedGraph {

    protected final IArgumentRepository argumentRepository;

    protected AsSynchronizedGraph<String, IEdgeWithPremiseNumber> graph;

    protected PageRank<String, IEdgeWithPremiseNumber> pageRanker;

    protected AbstractJGraphTAdapter() {
        super();

        this.argumentRepository = new ArgumentRepository();
    }

    @Override
    public Map<String, Double> computeAndSavePageRank()
    {
        Map<String,Double> pageRankScores = pageRanker.getScores();

        Double[] pageRankValues = pageRankScores.values().toArray(new Double[0]);
        Arrays.sort(pageRankValues);
        Double current = 0d;
        int count = 0;
        for(Double pageRank : pageRankValues){
            if(pageRank.equals(current)){
                count++;
            }else{
                System.out.println("Pagerank " + current + " occurs " + count + " times.");
                current = pageRank;
                count = 1;
            }
        }

        if (System.getenv().get("DEBUG") != null && System.getenv().get("DEBUG").equals("1")) {
            System.out.println("Saving of pageRank started at : " + java.time.ZonedDateTime.now());
            for (Map.Entry<String, Double> entry : pageRankScores.entrySet()) {
                this.argumentRepository.updatePageRank(entry.getKey(), entry.getValue());
            }
            System.out.println("Saving of pageRank ended at : " + java.time.ZonedDateTime.now());
        }

        return pageRankScores;
    }

    @Override
    public String addVertex(String vertex) {
        boolean added = graph.addVertex(vertex);

        return added ? vertex : null;
    }

    @Override
    public int getNumberOfEdges() {
        int count = 0;
        for(IEdgeWithPremiseNumber edge : this.graph.edgeSet()){
            count++;
        }
        return count;
    }

    @Override
    public Iterable<String[]> getOutgoingEdges(String vertex) {
        LinkedList<String[]> edges = new LinkedList<String[]>();
        for(IEdgeWithPremiseNumber edge : this.graph.outgoingEdgesOf(vertex)){
            String[] edgeAsArray = new String[3];
            edgeAsArray[0] = graph.getEdgeSource(edge);
            edgeAsArray[1] = graph.getEdgeTarget(edge);
            edgeAsArray[2] = edge.getPremiseNumber();
            edges.add(edgeAsArray);
        }

        return edges;
    }

    public Iterable<String[]> getIncomingEdges(String vertex) {
        LinkedList<String[]> edges = new LinkedList<String[]>();
        for(IEdgeWithPremiseNumber edge : this.graph.incomingEdgesOf(vertex)){
            String[] edgeAsArray = new String[3];
            edgeAsArray[0] = graph.getEdgeSource(edge);
            edgeAsArray[1] = graph.getEdgeTarget(edge);
            edgeAsArray[2] = edge.getPremiseNumber();
            edges.add(edgeAsArray);
        }

        return edges;
    }
}
