package de.peyrer.graph;

import de.peyrer.indexmodule.InvalidSettingValueException;

import java.io.IOException;

public interface IGraphBuilder {

    enum GraphType {
        JGRAPHT
    }

    IDirectedGraph build(String premiseIndex, String conclusionIndex) throws IOException, InterruptedException, InvalidSettingValueException;

    IDirectedGraph buildWithPremiseIndex(String premiseIndex) throws IOException, InterruptedException, InvalidSettingValueException;

    IDirectedGraph buildWithConclusionIndex(String conclusionIndex) throws IOException, InterruptedException, InvalidSettingValueException;


}
