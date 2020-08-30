package de.peyrer.relevance;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.peyrer.graph.AbstractDirectedGraph;
import de.peyrer.graph.IDirectedGraph;
import de.peyrer.repository.ArgumentRepository;

public class SumComputer extends AbstractRelevanceComputer
{
	public IDirectedGraph graph;
	
	public Map<String, Double> pageRank;

	@Override
	public IDirectedGraph setGraph(IDirectedGraph graph) {
		this.graph = graph;
		return graph;
	}

	@Override
    public Map<String,Double> setPageRank(Map<String,Double> pageRank) {
    	this.pageRank = pageRank;
    	return pageRank;
    }

	@Override
	public Map<String,Double> computeAndSaveRelevance()
	{
		Double[] pageRankValues = pageRank.values().toArray(new Double[0]);
		Arrays.sort(pageRankValues);
		double pageRankMedian = pageRankValues[pageRankValues.length/2];

		Map<String,Double> relevanceMap = new HashMap<>();
		Map<String,Integer> numberOfPremises = argumentRepository.getNumberOfPremises();

		for(Map.Entry<String, Double> node : pageRank.entrySet()) {
			double relevanceSum = 0;
			Iterable<String[]> children = graph.getOutgoingEdges(node.getKey());

			//If argument has no premises
			if (numberOfPremises.get(node.getKey()) == 0) {
				relevanceSum = Double.MIN_VALUE;
			}
			else {
				relevanceSum = 0.0;		//iterate over children -> put uniques in new map
				Map<String,Double> uniqueChildren = new HashMap<>();
				for(String[] child : children) {
					if(!uniqueChildren.containsKey(child[2])) {
						uniqueChildren.put(child[2], pageRank.get(child[1]));
					}
				}
				for(Map.Entry<String, Double> uniqueNodes : uniqueChildren.entrySet()) {
					relevanceSum += uniqueNodes.getValue();	//grab page rank for each child
				}

				relevanceSum += (numberOfPremises.get(node.getKey()) - uniqueChildren.size()) * pageRankMedian;
			}
			relevanceMap.put(node.getKey(), relevanceSum);
		}

		this.normalizeRelevance(relevanceMap, relevanceMap.size());

		this.logRelevanceData(relevanceMap);

		if (System.getenv().get("DEBUG") != null && System.getenv().get("DEBUG").equals("1")) {
			this.saveRelevance(relevanceMap);
		}

		return relevanceMap;
	}
}