package de.peyrer.relevance;

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
	public Map<String,Double> computeAndSaveRelevance(){
		double relevance;
		double pageRankChild;
		double relevanceSum;

		Map<String,Double> relevanceMap = new HashMap<>();
		Map<String,Double> uniqueChildren = null;
		
		for(Map.Entry<String, Double> node : pageRank.entrySet()) {
			Iterable<String[]> children = graph.getOutgoingEdges(node.getKey());
			int numberOfPremises = repo.getNumberofPremises(node.getKey());
			//If argument has no premises
			if(children == null) {
				relevance = Double.MIN_VALUE;
				relevanceMap.put(node.getKey(), relevance);
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
					pageRankChild = 0.0;
					pageRankChild = uniqueNodes.getValue();	//grab page rank for each child
					relevanceSum += pageRankChild;
				}
				relevanceSum += (numberOfPremises - uniqueChildren.size()) * (1 - AbstractDirectedGraph.dampingFactor);
				relevanceMap.put(node.getKey(), relevanceSum);
			}
		}
		for(Map.Entry<String, Double> rvMap : relevanceMap.entrySet()){
			repo.updateRelevance(rvMap.getKey(), rvMap.getValue());
		}
		return relevanceMap;
	}
}