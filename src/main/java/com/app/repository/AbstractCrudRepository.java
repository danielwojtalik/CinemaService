package com.app.repository;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.repository.connection.DbConnection;
import com.google.common.base.CaseFormat;
import org.atteo.evo.inflector.English;
import org.jdbi.v3.core.Jdbi;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractCrudRepository<T, ID> implements CrudRepository<T, ID> {

    protected final Jdbi jdbi = DbConnection.getInstance().getJdbi();
    @SuppressWarnings("unchecked")
    private final Class<T> entityType = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    @SuppressWarnings("unchecked")
    private final Class<ID> idType = (Class<ID>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    private static String NULL = "null";

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
            throw new MyException("INVALID ID PARAMETER", ExceptionCode.REPOSITORY);
        }

        return jdbi.withHandle(handle -> handle
                .createQuery("select * from " + getTableName() + "where id =:id")
                .bind("id", id)
                .mapToBean(entityType)
                .findFirst()
        );
    }

    @Override
    public void add(T t) {
        final String sql = "insert into " + getTableName() + getParamsForAdd(t);
        System.out.println(sql);
        jdbi.withHandle(handle -> handle
                .createUpdate(sql)
                .execute());
    }

    @Override
    public void update(T t) {
        if (t == null) {
            throw new MyException("UPDATE EXCEPTION", ExceptionCode.REPOSITORY);
        }
        final String sql = "update " + getTableName() + " set " + getParamsForUpdate(t) + " where " + getIdForUpdate(t);
        jdbi.useHandle(handle -> handle.createUpdate(sql).execute());
    }

    @Override
    public void deleteAll() {
        jdbi.useHandle(handle -> handle
                .createUpdate("delete from " + getTableName())
                .execute()
        );
    }

    @Override
    public void deleteByID(ID id) {
        if (id == null) {
            throw new MyException("ID TO DELETE IS NULL", ExceptionCode.REPOSITORY);
        }

        jdbi.useHandle(handle -> handle
                .createUpdate("delete from " + getTableName() + " where id = :id")
                .bind("id", id)
                .execute()
        );
    }

    private String getTableName() {
        String tableName = entityType.getSimpleName();
        return English.plural(tableName);
    }

    private String getParamsForAdd(T t) {

        String params = "(" + Arrays
                .stream(entityType.getDeclaredFields())
                .filter(field -> !field.getName().equals("id"))
                .map(field -> {
                    try {
                        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
                    } catch (Exception e) {
                        throw new MyException("PARAMS EXCEPTION", ExceptionCode.REPOSITORY);
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
                        if (field.getType().equals(String.class)) {
                            return "'" + field.get(t).toString() + "'";
                        }
                        return field.get(t).toString();
                    } catch (Exception e) {
                        throw new MyException("ADDED VALUES EXCEPTIONS", ExceptionCode.REPOSITORY);
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
                        throw new MyException("PARAMS FOR UPDATE EXCEPTION IN FILTER", ExceptionCode.REPOSITORY);
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
                        throw new MyException("PARAMS FOR UPDATE EXCEPTION IN MAP OPERATION", ExceptionCode.REPOSITORY);
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
            throw new MyException("GET ID FOR UPDATE EXCEPTION", ExceptionCode.REPOSITORY);

        }
    }
}
