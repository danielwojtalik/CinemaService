package com.app.repository;

import com.app.connection.DbConnection;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {

     Jdbi jdbi = DbConnection.getInstance().getJdbi();

    void add(T t);

    void update(T t);

    Optional<T> findById(Integer id);

    List<T> findAll();

    void deleteByID(Integer id);

    void deleteAll();
}
