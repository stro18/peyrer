package de.peyrer.graph;

import org.jgrapht.graph.DefaultEdge;

class DefaultEdgeWithPremiseNumber extends DefaultEdge {

    private String premiseId;

    DefaultEdgeWithPremiseNumber(String premiseId){
        super();

        this.premiseId = premiseId;
    }

    String getPremiseNumber(){
        return this.premiseId;
    }
}
