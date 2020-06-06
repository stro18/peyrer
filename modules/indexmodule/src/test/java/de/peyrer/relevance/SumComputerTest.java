package de.peyrer.relevance;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.peyrer.model.Argument;
import de.peyrer.repository.ArgumentRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import de.peyrer.graph.*;
import org.mockito.MockitoAnnotations;

public class SumComputerTest {

	@Mock(name = "argumentRepository")
	ArgumentRepository argumentRepository;

	@InjectMocks
	SumComputer sumComputer;

	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testRelevance() {
		Argument argument = new Argument("1", "Abortion is good", new String[]{"Every woman has the right to decide for herself"});
	    Argument argument2 = new Argument("2", "Abortion is bad", new String[]{"Health risks for woman", "Abortion is murder"});
		Argument argument3 = new Argument("3", "Health risks for woman", new String[]{"0.1% of the woman have permanent damage to health"});
		Argument argument4 = new Argument("4", "Abortion is murder", new String[]{"Embryos live"});
		Argument argument5 = new Argument("5", "Every woman has the right to decide for herself", new String[]{"Some womans may be to young to be mothers"});

		JGraphTAdapter jGraphTAdapter = new JGraphTAdapter();
		jGraphTAdapter.addVertex("1");
		jGraphTAdapter.addVertex("2");
		jGraphTAdapter.addVertex("3");
		jGraphTAdapter.addVertex("4");
		jGraphTAdapter.addVertex("5");
		jGraphTAdapter.addEdge("1", "5", "1");
		jGraphTAdapter.addEdge("2", "3", "1");
		jGraphTAdapter.addEdge("2", "4", "2");

		Map<String,Double> pageRank = new HashMap<String,Double>();
		pageRank.put("1", 0.9);
		pageRank.put("2", 0.5);
		pageRank.put("3", 0.4);
		pageRank.put("4", 0.3);
		pageRank.put("5", 0.5);

		Mockito.when(argumentRepository.updateRelevance(Mockito.anyString(), Mockito.anyDouble()))
				.thenAnswer(i -> i.getArgument(1));

		Map<String,Integer> numberOfPremises = new HashMap<>();
		numberOfPremises.put("1", 1);
		numberOfPremises.put("2", 2);
		numberOfPremises.put("3", 1);
		numberOfPremises.put("4", 1);
		numberOfPremises.put("5", 1);
		Mockito.when(argumentRepository.getNumberOfPremises()).thenReturn(numberOfPremises);

		sumComputer.setGraph(jGraphTAdapter);
		sumComputer.setPageRank(pageRank);

		Map<String,Double> relevance = sumComputer.computeAndSaveRelevance();

		Assert.assertEquals(0.5, relevance.get("1"), 0.0);
		Assert.assertEquals(0.7, relevance.get("2"), 0.0);
	}
}

