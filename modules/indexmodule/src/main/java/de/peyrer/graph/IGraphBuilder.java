package de.peyrer.graph;

import de.peyrer.indexmodule.InvalidSettingValueException;

import java.io.IOException;

public interface IGraphBuilder {

    enum GraphType {
        JGRAPHT
    }

    IDirectedGraph build(String premiseIndex, String conclusionIndex) throws IOException, InterruptedException;

    IDirectedGraph build(String premiseIndex) throws IOException, InterruptedException, InvalidSettingValueException;
}
