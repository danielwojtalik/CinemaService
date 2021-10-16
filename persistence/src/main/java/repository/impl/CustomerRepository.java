package repository.impl;


import exceptions.CustomException;
import exceptions.ExceptionCode;
import model.Customer;
import repository.generic.AbstractCrudRepository;

import java.util.List;
import java.util.Optional;

public class CustomerRepository extends AbstractCrudRepository<Customer, Integer> {

    public List<Customer> findByName(String name) {
        if (name == null) {
            throw new CustomException("Customer name cannot be null", ExceptionCode.CUSTOMER_REPOSITORY);
        }
        return jdbi.withHandle(handle -> handle.createQuery("select * from customers where name = :name")
                .bind("name", name)
                .mapToBean(Customer.class)
                .list());
    }


    public List<Customer> findBySurname(String surname) {
        if (surname == null) {
            throw new CustomException("Customer surname cannot be null", ExceptionCode.CUSTOMER_REPOSITORY);
        }
        return jdbi.withHandle(handle -> handle.createQuery("select * from customers where surname = :surname")
                .bind("surname", surname)
                .mapToBean(Customer.class)
                .list());
    }

    public List<Customer> findByAge(int age) {
        if (age < 0 || age > 130){
            throw new CustomException("Customer age range is wrong", ExceptionCode.CUSTOMER_REPOSITORY);
        }
        return jdbi.withHandle(handle -> handle.createQuery("select * from customers where age = :age")
                .bind("age", age)
                .mapToBean(Customer.class)
                .list());
    }

    public Optional<Customer> findByNameSurnameEmail(String name, String surname, String email) {
        if (name == null || surname == null || email == null) {
            throw new CustomException("Customer name, surname or email is null", ExceptionCode.CUSTOMER_REPOSITORY);
        }
        return jdbi.withHandle(handle -> handle.createQuery("select * from customers where name = :name and " +
                "surname = :surname and email = :email")
                .bind("name", name)
                .bind("surname", surname)
                .bind("email", email)
        .mapToBean(Customer.class).findFirst());
    }
}
