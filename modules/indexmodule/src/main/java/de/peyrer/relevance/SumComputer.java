package de.peyrer.relevance;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.peyrer.graph.AbstractDirectedGraph;
import de.peyrer.graph.IDirectedGraph;
import de.peyrer.repository.ArgumentRepository;

public class SumComputer implements IRelevanceComputer{
	
	public IDirectedGraph graph;
	
	public Map<String, Double> pageRank;
	
	private ArgumentRepository repo;
	
	public SumComputer(){
		this.repo = new ArgumentRepository();
	}

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
		Map<String,Double> uniqueChildren = null;
		
		for(Map.Entry<String, Double> node : pageRank.entrySet()) {
			double relevanceSum = 0;
			Iterable<String[]> children = graph.getOutgoingEdges(node.getKey());
			//If argument has no premises
			if(children == null) {
				relevanceSum = Double.MIN_VALUE;
				relevanceMap.put(node.getKey(), relevanceSum);
			}
			else {
				relevanceSum = 0.0;		//iterate over children -> put uniques in new map
				uniqueChildren = new HashMap<>();
				for(String[] child : children) {
					if(!uniqueChildren.containsKey(child[2])) {
						uniqueChildren.put(child[2], pageRank.get(child[1]));
					}
				}
				for(Map.Entry<String, Double> uniqueNodes : uniqueChildren.entrySet()) {
					relevanceSum += uniqueNodes.getValue();	//grab page rank for each child
				}
				int numberOfPremises = repo.getNumberofPremises(node.getKey());
				relevanceSum += (numberOfPremises - uniqueChildren.size()) * pageRankMedian;
				relevanceMap.put(node.getKey(), relevanceSum);
			}
		}
		for(Map.Entry<String, Double> rvMap : relevanceMap.entrySet()){
			repo.updateRelevance(rvMap.getKey(), rvMap.getValue());
		}
		return relevanceMap;
	}
}