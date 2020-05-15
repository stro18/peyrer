package de.peyrer.graph;

import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.Map;

public class JGraphTAdapterTest {

    @Test
    public void testComputePageRank()
    {
        GraphBuilder builder = new GraphBuilder(GraphBuilder.GraphType.JGRAPHT, GraphBuilder.MatcherType.AND);

        IDirectedGraph graph = builder.build();

        Map<String, Double> scores = graph.computeAndSavePageRank();

        for(Map.Entry<String,Double> entry : scores.entrySet()){
            Assert.assertTrue(entry.getValue() > 0);
        }

        Assert.assertTrue(scores.size() > 1);
    }

    @Test
    public void testGetOutgoingEdges()
    {
        JGraphTAdapter jGraphTAdapter = new JGraphTAdapter();

        jGraphTAdapter.addVertex("1");
        jGraphTAdapter.addVertex("2");
        jGraphTAdapter.addVertex("3");
        jGraphTAdapter.addEdge("1", "2", "1");
        jGraphTAdapter.addEdge("3", "1", "1");

        Iterable<String[]> resultIterable = jGraphTAdapter.getOutgoingEdges("1");

        Assert.assertTrue(resultIterable.iterator().hasNext());

        for(String[] result : resultIterable){
            Assert.assertArrayEquals(result, new String[]{"1", "2", "1"});
        }
    }
}
