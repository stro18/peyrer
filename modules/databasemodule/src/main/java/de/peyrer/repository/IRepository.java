package de.peyrer.repository;

public interface IRepository<T> {

    Iterable<T> readAll();

    T readById(String id);
    T create(T entity);
    T replace(T entity,T entity2);
    T delete(T entity);
}
