package repository.generic;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {

    List<T> findAll();

    Optional<T> findById(ID id);

    Optional<T> add(T t);

    Optional<T> update(T t);

    boolean deleteAll();

    Optional<T> deleteByID(ID id);
}
