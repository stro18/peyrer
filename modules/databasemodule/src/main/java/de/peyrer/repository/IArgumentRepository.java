package de.peyrer.repository;

import de.peyrer.model.Argument;

public interface IArgumentRepository extends IRepository<Argument> {
    public double updatePageRank(String id, double value);

    public double updateRelevance(String id, double value);

    public int getNumberofPremises(String id);
}
