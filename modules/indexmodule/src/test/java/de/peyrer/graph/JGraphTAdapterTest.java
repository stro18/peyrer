package de.peyrer.graph;

import de.peyrer.repository.ArgumentRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Iterator;
import java.util.Map;

public class JGraphTAdapterTest {

    @Mock (name = "argumentRepository")
    ArgumentRepository argumentRepository;

    @InjectMocks
    JGraphTAdapter jGraphTAdapter;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        jGraphTAdapter.addVertex("1");
        jGraphTAdapter.addVertex("2");
        jGraphTAdapter.addVertex("3");
        jGraphTAdapter.addEdge("1", "2", "1");
        jGraphTAdapter.addEdge("3", "1", "1");
    }

    @Test
    public void testComputePageRank()
    {
        Mockito.when(argumentRepository.updatePageRank(Mockito.anyString(), Mockito.anyDouble()))
                .thenAnswer(i -> i.getArgument(1));

        Map<String, Double> scores = jGraphTAdapter.computeAndSavePageRank();

        for(Map.Entry<String,Double> entry : scores.entrySet()){
            Assert.assertTrue(entry.getValue() > 0);
        }

        Assert.assertTrue(scores.size() > 1);
    }

    @Test
    public void testGetOutgoingEdges()
    {
        Iterable<String[]> resultIterable = jGraphTAdapter.getOutgoingEdges("1");

        Assert.assertTrue(resultIterable.iterator().hasNext());

        for(String[] result : resultIterable){
            Assert.assertArrayEquals(result, new String[]{"1", "2", "1"});
        }
    }
}
