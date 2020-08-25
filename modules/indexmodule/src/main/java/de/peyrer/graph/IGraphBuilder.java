package de.peyrer.graph;

import de.peyrer.indexmodule.InvalidSettingValueException;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;

public interface IGraphBuilder {

    enum GraphType {
        JGRAPHT,
        JGRAPHT_WEIGHTED
    }

    IDirectedGraph build(String premiseIndex, String conclusionIndex) throws IOException, InterruptedException, InvalidSettingValueException, ParseException;

    IDirectedGraph buildWithPremiseIndex(String premiseIndex) throws IOException, InterruptedException, InvalidSettingValueException, ParseException;

    IDirectedGraph buildWithConclusionIndex(String conclusionIndex) throws IOException, InterruptedException, InvalidSettingValueException;


}
