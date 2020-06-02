package de.peyrer.graph;

import de.peyrer.graph.AndMatcher;
import de.peyrer.indexer.ConclusionIndexer;
import de.peyrer.indexer.PremiseIndexer;
import de.peyrer.indexer.RelevanceIndexer;
import de.peyrer.model.Argument;
import de.peyrer.repository.IArgumentRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class AndMatcherTest {

    /*
    @Test
    public void MatchingTest(){
        PremiseIndexer indexer = null;
        ConclusionIndexer conclusionIndexer = null;
        AndMatcher andMatcher = null;
        try {
            indexer = new PremiseIndexer("index");
            conclusionIndexer = new ConclusionIndexer("conclusionIndex");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // make the arguments for mocked database
        Argument argument = new Argument("1", "Abortion is murder", new String[]{"Every woman has the right to decide for herself"});
        Argument argument2 = new Argument("2", "Abortion is bad", new String[]{"Abortion is murder"});

        // mock the argument repository for the premiseIndexer
        Iterator<Argument> iterator = (Iterator<Argument>) Mockito.mock(Iterator.class);
        Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(iterator.next()).thenReturn(argument).thenReturn(argument2);

        Iterable<Argument> iterable = (Iterable<Argument>) Mockito.mock(Iterable.class);
        Mockito.when(iterable.iterator()).thenReturn(iterator);

        indexer.argumentRepository = Mockito.mock(IArgumentRepository.class);
        Mockito.when(indexer.argumentRepository.readAll()).thenReturn(iterable);

        // mock the argument repository for the conclusionIndexer
        Iterator<Argument> iterator_Conclusions = (Iterator<Argument>) Mockito.mock(Iterator.class);
        Mockito.when(iterator_Conclusions.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(iterator_Conclusions.next()).thenReturn(argument).thenReturn(argument2);

        Iterable<Argument> iterable_Conclusions = (Iterable<Argument>) Mockito.mock(Iterable.class);
        Mockito.when(iterable_Conclusions.iterator()).thenReturn(iterator_Conclusions);

        conclusionIndexer.argumentRepository = Mockito.mock(IArgumentRepository.class);
        Mockito.when(conclusionIndexer.argumentRepository.readAll()).thenReturn(iterable_Conclusions);

        //index the mocked database and construct matcher
        try {
            indexer.index();
            conclusionIndexer.index();
            andMatcher = new AndMatcher();
            andMatcher.setStringToMatch("Abortion is murder");
            andMatcher.setArgumentId("1");
            andMatcher.setDirectoryName(indexer.getIndexPath());
            andMatcher.setDirectoryName_Conclusions(conclusionIndexer.getIndexPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterable<Map<String,String>> indexIterable = Objects.requireNonNull(andMatcher).match();
        Iterator<Map<String,String>> indexIterator = indexIterable.iterator();
        Map<String,String> firstMap = indexIterator.next();
        Assert.assertEquals(firstMap.get("argumentId"),"2");
    }
    */
}

