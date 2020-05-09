package de.peyrer.graph;

import org.junit.Assert;
import org.junit.Test;

public class GraphBuilderTest {

    @Test
    public void testBuildJGraphT(){
        GraphBuilder builder = new GraphBuilder(GraphBuilder.GraphType.JGRAPHT, GraphBuilder.MatcherType.AND);

        IDirectedGraph graph = builder.build();

        int counter = 0;
        for(String[] edge : graph.getEdges()){
            counter++;

            if(counter > 1000){
                break;
            }
        }

        Assert.assertTrue(counter > 1000);
    }
}
