package de.peyrer.graph;

public class DefaultWeightedEdgeWithPremiseNumber implements IEdgeWithPremiseNumber
{
    private String premiseId;

    DefaultWeightedEdgeWithPremiseNumber(String premiseId){
        super();

        this.premiseId = premiseId;
    }

    public String getPremiseNumber(){
        return this.premiseId;
    }
}
