package de.peyrer.relevance;

import java.util.Map;

import de.peyrer.graph.IDirectedGraph;

public class SumComputer implements IRelevanceComputer{
	
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
    public Map<String,Double> computeRelevance() {
		boolean wasComputed = false;
		double relevanceEnd;
		double relevanceChildren;
		
		Map<String,Double> relevanceMap = pageRank;
		relevanceMap.clear();	//hacky way of creating an empty map
		
	    	for(Map.Entry<String, Double> node : pageRank.entrySet()) {
	    		relevanceEnd = 0.0;
	    		Iterable<String[]> neighbors = graph.getEdges(node.getKey());
	    		if(neighbors == null) {
	    			relevanceEnd = pageRank.get(node.getKey());
	    			if(relevanceEnd == 0) {
	    				relevanceEnd = 0.15;
	    			}
	    			relevanceMap.put(node.getKey(), relevanceEnd);
	    			wasComputed = true;
	    		}
	    		else{
	    			for(String[] neighbor : neighbors) {
	    				relevanceChildren = 0.0;
	    				Iterable<String[]> children = graph.getEdges(neighbor[0]);
	    				if(children == null) {
	    					relevanceChildren = pageRank.get(neighbor[0]) + relevanceEnd;
	    					if(relevanceChildren == 0) {
	    						relevanceChildren = 0.15 + relevanceEnd;
	    					}
	    					relevanceMap.put(neighbor[0], relevanceChildren);
	    					wasComputed = true;
	    				}
	    				else {
	    					neighbors = children;
	    					relevanceMap.putAll(computeRelevanceRecursion(neighbors, relevanceChildren));
	    				}
	    			}
	    		}
	    	}
	    	return relevanceMap;
		}
	
	@Override
	public Map<String,Double> computeRelevanceRecursion(Iterable<String[]> neighbors, double relevanceEnd) {
		Map<String,Double> relevanceMap = pageRank;
		boolean wasComputed;
		double relevanceChildren;
		relevanceMap.clear();	//hacky way of creating an empty map
		if(wasComputed = true) {
			//do nothing
		}
		else {
			for(String[] neighbor : neighbors) {
				relevanceChildren = 0.0;
				Iterable<String[]> children = graph.getEdges(neighbor[0]);
				if(children == null) {
					relevanceChildren = pageRank.get(neighbor[0]) + relevanceEnd;
					if(relevanceChildren == 0) {
							relevanceChildren = 0.15 + relevanceEnd;
					}
					relevanceMap.put(neighbor[0], relevanceChildren);
					wasComputed = true;
				}
				else {
					neighbors = children;
					computeRelevanceRecursion(neighbors, relevanceChildren);
				}
			}
		}
		return relevanceMap;
	}
}
//TODO Stop possible infinite loops caused by circles in graph