package de.peyrer.repository;

import java.util.Iterator;

public interface IRepository<T> {

    Iterable<T> readAll();

    T readById(String id);
    T create(T entity);
    T update(T entity);
    T delete(T entity);
}
