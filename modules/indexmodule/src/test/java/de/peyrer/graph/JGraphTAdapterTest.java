package de.peyrer.graph;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class JGraphTAdapterTest {

    @Test
    public void testComputePageRank()
    {
        GraphBuilder builder = new GraphBuilder(GraphBuilder.GraphType.JGRAPHT, GraphBuilder.MatcherType.AND);

        IDirectedGraph graph = builder.build();

        Map<String, Double> scores = graph.computePageRank();

        for(Map.Entry<String,Double> entry : scores.entrySet()){
            Assert.assertTrue(entry.getValue() > 0);
        }

        Assert.assertTrue(scores.size() > 1);
    }
}
