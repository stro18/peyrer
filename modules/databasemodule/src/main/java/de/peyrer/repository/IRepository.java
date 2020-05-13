package de.peyrer.repository;

import java.util.Iterator;

public interface IRepository<T> {

    Iterable<T> readAll();

    T readById(String id);
    T create(T entity);
    T replace(T entity,T entity2);
    T delete(T entity);
    T updatePageRank (T entity, double value);
    T updateRelevance(T entity, double value);
}
