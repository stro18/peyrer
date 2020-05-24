package de.peyrer.relevance;

import java.util.Iterator;
import java.util.Map;

import de.peyrer.model.Argument;
import org.junit.Test;
import org.mockito.Mockito;
import de.peyrer.graph.*;

public class IRelevanceComputerTest {
	
	@Test
	public void testRelevance() {
		//creates a small graph here
		Argument argument = new Argument("1", "Abortion is good", new String[]{"Every woman has the right to decide for herself"});
	    Argument argument2 = new Argument("2", "Abortion is bad", new String[]{"Abortion is murder"});

	    Iterator<Argument> iterator = (Iterator<Argument>) Mockito.mock(Iterator.class);
	    Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
	    Mockito.when(iterator.next()).thenReturn(argument).thenReturn(argument2);

		Iterable<Argument> iterable = (Iterable<Argument>) Mockito.mock(Iterable.class);
	    Mockito.when(iterable.iterator()).thenReturn(iterator);
	    
	    for(Argument fakeArgument : iterable) {
	    	graph.addVertex(fakeArgument.id);
	    	matcher.setStringToMatch(fakeArgument.conclusion);
	    	
            Iterable<Map<String,String>> matches = matcher.match();

            for(Map<String,String> match : matches){
                graph.addVertex(match.get("argumentId"));
                graph.addEdge(match.get("argumentId"), argument.id, match.get("premiseId"));
            }
	    }
	    
		//creates small pageRank Map here
		
	}

}

