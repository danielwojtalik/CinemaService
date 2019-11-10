package com.app.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {

//     Jdbi jdbi = DbConnection.getInstance().getJdbi();

    List<T> findAll();

    Optional<T> findById(ID id);

    void add(T t);

    void update(T t);

    void deleteAll();

    void deleteByID(ID id);
}
