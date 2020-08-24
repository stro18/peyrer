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
		Map<String,Integer> numberOfPremises = repo.getNumberOfPremises();

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
				relevanceMap.put(node.getKey(), relevanceSum);
			}
		}

		if (System.getenv().get("DEBUG") != null && System.getenv().get("DEBUG").equals("1")) {
			this.saveRelevance(relevanceMap);
		}

		return relevanceMap;
	}

	private void saveRelevance(Map<String,Double> relevanceMap)
	{
		System.out.println("Saving of relevance started at : " + java.time.ZonedDateTime.now());
		int count = 0;
		for (Map.Entry<String, Double> rvMap : relevanceMap.entrySet()) {
			repo.updateRelevance(rvMap.getKey(), rvMap.getValue());

			count++;
			if (count % 1000 == 0) {
				System.out.println("Progress: Relevance of " + count + " arguments saved at: " + java.time.ZonedDateTime.now());
			}
		}
		System.out.println("Saving of relevance ended at : " + java.time.ZonedDateTime.now());
	}
}