package de.peyrer.graph;

import de.peyrer.repository.ArgumentRepository;
import de.peyrer.repository.IArgumentRepository;
import org.jgrapht.alg.scoring.PageRank;
import org.jgrapht.graph.concurrent.AsSynchronizedGraph;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class AbstractJGraphTAdapter extends AbstractDirectedGraph {

    protected final IArgumentRepository argumentRepository;

    protected AsSynchronizedGraph<String, IEdgeWithPremiseNumber> graph;

    protected PageRank<String, IEdgeWithPremiseNumber> pageRanker;

    public static String number_of_edges = "Number of edges: ";

    public static String average_number_of_edges = "Average number of edges: ";

    public static String arguments_with_incoming_degree_zero = "Number of arguments with incoming degree zero: ";

    public static String arguments_with_incoming_degree_one = "Number of arguments with incoming degree one: ";

    public static String arguments_with_incoming_degree_at_least_two = "Number of arguments with at least incoming degree two: ";

    public static String median_degree = "Median incoming degree: ";

    public static String highest_degree = "Highest incoming degree: ";

    public static String argument_with_highest_degree = "Argument with highest incoming degree: ";

    protected AbstractJGraphTAdapter() {
        super();

        this.argumentRepository = new ArgumentRepository();
    }

    @Override
    public Map<String, Double> computeAndSavePageRank()
    {
        Map<String,Double> pageRankScores = pageRanker.getScores();

        if (System.getenv().get("DEBUG") != null && System.getenv().get("DEBUG").equals("1")) {
            this.logPageRank(pageRankScores);
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

    public void logPageRank(Map<String,Double> pageRankScores)
    {
        Double[] pageRankValues = pageRankScores.values().toArray(new Double[0]);
        Arrays.sort(pageRankValues);
        Double current = 0d;
        int count = 0;
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(String.format("/out/pagerank-distribution_%s.xml",
                    java.time.ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        } catch (FileNotFoundException e) {
            System.err.println("Cannot open output file for PageRank distribution logging.");
        }
        writer.println("<PageRanks>");
        for(Double pageRank : pageRankValues){
            if(pageRank.equals(current)){
                count++;
            }else{
                writer.println("<PageRank rank='" + current + "'>\n\t<Amount num='" + count + "' />\n</PageRank>\n");
                System.out.println("Pagerank " + current + " occurs " + count + " times.");
                current = pageRank;
                count = 1;
            }
        }
        writer.println("</PageRanks>");
        writer.close();
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

    @Override
    public Map<String,String> analyse()
    {
        int numberOfEdges = 0;
        double averageNumberOfEdges = 0;
        int argumentsWithNullEdges = 0;
        int argumentsWithOneEdge = 0;
        int argumentsWithAtleastTwoEdges = 0;
        int medianDegree = 0;
        int highestDegree = 0;
        String argumentWithHighestDegree = "";

        List<Integer> argumentsMedianDegree = new LinkedList<>();
        int numberOfArguments = 0;

        for (String vertex : graph.vertexSet()) {
            numberOfArguments++;

            int incomingEdges = graph.inDegreeOf(vertex);

            numberOfEdges += incomingEdges;

            argumentsMedianDegree.add(incomingEdges);

            switch (incomingEdges) {
                case 0:
                    argumentsWithNullEdges++;
                    break;
                case 1:
                    argumentsWithOneEdge++;
                    break;
                default:
                    argumentsWithAtleastTwoEdges++;
            }

            if (incomingEdges > highestDegree) {
                highestDegree = incomingEdges;
                argumentWithHighestDegree = vertex;
            }
        }

        averageNumberOfEdges = (double) numberOfEdges / numberOfArguments;

        Integer[] medianArray = argumentsMedianDegree.toArray(new Integer[0]);
        Arrays.sort(medianArray);
        medianDegree = medianArray[medianArray.length/2];

        Map<String,String> result = new HashMap<>();

        result.put(number_of_edges, String.valueOf(numberOfEdges));
        result.put(average_number_of_edges, String.valueOf(averageNumberOfEdges));
        result.put(median_degree, String.valueOf(medianDegree));
        result.put(arguments_with_incoming_degree_zero, String.valueOf(argumentsWithNullEdges));
        result.put(arguments_with_incoming_degree_one, String.valueOf(argumentsWithOneEdge));
        result.put(arguments_with_incoming_degree_at_least_two, String.valueOf(argumentsWithAtleastTwoEdges));
        result.put(highest_degree, String.valueOf(highestDegree));
        result.put(argument_with_highest_degree, argumentWithHighestDegree);

        return result;
    }
}
