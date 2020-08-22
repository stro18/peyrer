package de.peyrer.graph;

import de.peyrer.indexer.RelevanceIndexer;
import de.peyrer.indexmodule.InvalidSettingValueException;
import de.peyrer.model.Argument;
import de.peyrer.repository.ArgumentRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;

public class GraphBuilderTest {
    @Mock(name = "argumentRepository")
    ArgumentRepository argumentRepository;

    @InjectMocks
    GraphBuilder builder;

    {
        try {
            builder = new GraphBuilder(GraphBuilder.GraphType.JGRAPHT);
        } catch (InvalidSettingValueException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    /*
    @Test
    public void testBuildJGraphT(){
        // Mocking of argumentRepository
        Argument argument = new Argument("1", "Abortion is good", new String[]{"Every woman has the right to decide for herself"});
        Argument argument2 = new Argument("2", "Abortion is bad", new String[]{"Abortion is murder"});

        Iterator<Argument> iterator = (Iterator<Argument>) Mockito.mock(Iterator.class);
        Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(iterator.next()).thenReturn(argument).thenReturn(argument2);

        Iterable<Argument> iterable = (Iterable<Argument>) Mockito.mock(Iterable.class);
        Mockito.when(iterable.iterator()).thenReturn(iterator);

        Mockito.when(argumentRepository.readAll()).thenReturn(iterable);

        IDirectedGraph graph = builder.build(
                Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "premiseindex").toString(),
                Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "conclusionindex").toString()
        );

        int counter = 0;
        for(String[] edge : graph.getEdges()){
            counter++;
        }

        // @Phillip: Bitte anpassen, sobald du matcher fertig hast
        Assert.assertEquals(2, counter);
    }*/
}
