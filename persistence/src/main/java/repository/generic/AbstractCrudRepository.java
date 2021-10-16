package repository.generic;

import com.google.common.base.CaseFormat;
import exceptions.ExceptionCode;
import exceptions.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.atteo.evo.inflector.English;
import org.jdbi.v3.core.Jdbi;
import repository.connection.DbConnection;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractCrudRepository<T, ID> implements CrudRepository<T, ID> {

    private static final String NULL = "null";

    protected final Jdbi jdbi = DbConnection.getInstance().getJdbi();
    @SuppressWarnings("unchecked")
    private final Class<T> entityType = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    @SuppressWarnings("unchecked")
    private final Class<ID> idType = (Class<ID>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];

    @Override
    public List<T> findAll() {
        return jdbi.withHandle(handle -> handle
                .createQuery("select * from " + getTableName())
                .mapToBean(entityType)
                .list()
        );
    }

    @Override
    public Optional<T> findById(ID id) {

        if (id == null) {
            throw new CustomException("INVALID ID PARAMETER", ExceptionCode.REPOSITORY);
        }

        return jdbi.withHandle(handle -> handle
                .createQuery("select * from " + getTableName() + " where id =:id")
                .bind("id", id)
                .mapToBean(entityType)
                .findFirst()
        );
    }

    @Override
    public Optional<T> add(T t) {
        final String sql = "insert into " + getTableName() + getParamsForAdd(t);
        jdbi.withHandle(handle -> handle
                .createUpdate(sql)
                .execute());

        return Optional.ofNullable(t);
    }

    @Override
    public Optional<T> update(T t) {
        final String sql = "update " + getTableName() + " set " + getParamsForUpdate(t) + " where " + getIdForUpdate(t);
        jdbi.useHandle(handle -> handle.createUpdate(sql).execute());
        return Optional.ofNullable(t);
    }

    @Override
    public boolean deleteAll() {
        jdbi.useHandle(handle -> handle
                .createUpdate("delete from " + getTableName())
                .execute()
        );

        return findAll().size() == 0;
    }

    @Override
    public Optional<T> deleteByID(ID id) {

        Optional<T> entity = findById(id);

        if (entity.isPresent()) {
            jdbi.useHandle(handle -> handle
                    .createUpdate("delete from " + getTableName() + " where id = :id")
                    .bind("id", id)
                    .execute()
            );
        }
        return entity;
    }

    private String getTableName() {
        String tableName = entityType.getSimpleName();
        tableName = English.plural(tableName);
        tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, tableName);
        return tableName;
    }

    private String getParamsForAdd(T t) {

        String params = "(" + Arrays
                .stream(entityType.getDeclaredFields())
                .filter(field -> !field.getName().equals("id"))
                .map(field -> {
                    try {
                        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
                    } catch (Exception e) {
                        throw new CustomException("PARAMS EXCEPTION", ExceptionCode.REPOSITORY);
                    }
                })
                .collect(Collectors.joining(", ")) + " ) ";

        String paramsValues = "(" + Arrays
                .stream(entityType.getDeclaredFields())
                .filter(field -> !field.getName().equals("id"))
                .map(field -> {
                    try {
                        field.setAccessible(true);
                        if (field.get(t) == null) {
                            return NULL;
                        }
                        if (!Number.class.isAssignableFrom(field.getType())) {
                            return "'" + field.get(t).toString() + "'";
                        }
                        return field.get(t).toString();
                    } catch (Exception e) {
                        throw new CustomException("ADDED VALUES EXCEPTIONS", ExceptionCode.REPOSITORY);
                    }
                })
                .collect(Collectors.joining(", ")) + " ) ";
        return params + "VALUES " + paramsValues + ";";
    }

    private String getParamsForUpdate(T t) {
        return Arrays
                .stream(entityType.getDeclaredFields())
                .filter(field -> {
                    try {
                        field.setAccessible(true);
                        return !field.getName().equals("id") && (field.get(t) != null || field.getName().equals("loyaltyCardId"));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        throw new CustomException("PARAMS FOR UPDATE EXCEPTION IN FILTER", ExceptionCode.REPOSITORY);
                    }
                })
                .map(field -> {
                    try {
                        field.setAccessible(true);
                        if (field.getType().equals(String.class)) {
                            return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName())
                                    + " = '" + field.get(t) + "'";
                        }
                        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName())
                                + " = " + field.get(t);
                    } catch (Exception e) {
                        throw new CustomException("PARAMS FOR UPDATE EXCEPTION IN MAP OPERATION", ExceptionCode.REPOSITORY);
                    }
                })
                .collect(Collectors.joining(", "));
    }

    private String getIdForUpdate(T t) {
        try {
            Field field = entityType.getDeclaredField("id");
            field.setAccessible(true);
            return "id = " + field.get(t);
        } catch (Exception e) {
            throw new CustomException("GET ID FOR UPDATE EXCEPTION", ExceptionCode.REPOSITORY);
        }
    }
}
