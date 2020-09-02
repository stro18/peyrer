package de.peyrer.graph;

import org.jgrapht.graph.DefaultEdge;

class DefaultEdgeWithPremiseNumber extends DefaultEdge implements IEdgeWithPremiseNumber {

    private String premiseId;

    DefaultEdgeWithPremiseNumber(String premiseId){
        super();

        this.premiseId = premiseId;
    }

    public String getPremiseNumber(){
        return this.premiseId;
    }
}
