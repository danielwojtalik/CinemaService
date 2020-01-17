package repository.impl;


import model.Customer;
import repository.generic.AbstractCrudRepository;

import java.util.List;
import java.util.Optional;

public class CustomerRepository extends AbstractCrudRepository<Customer, Integer> {

    public List<Customer> findByName(String name) {
        return jdbi.withHandle(handle -> handle.createQuery("select * from customers where name = :name")
                .bind("name", name)
                .mapToBean(Customer.class)
                .list());
    }


    public List<Customer> findBySurname(String surname) {
        return jdbi.withHandle(handle -> handle.createQuery("select * from customers where surname = :surname")
                .bind("surname", surname)
                .mapToBean(Customer.class)
                .list());
    }

    public List<Customer> findByAge(int age) {
        return jdbi.withHandle(handle -> handle.createQuery("select * from customers where age = :age")
                .bind("age", age)
                .mapToBean(Customer.class)
                .list());
    }

    public Optional<Customer> findByNameSurnameEmail(String name, String surname, String email) {
        return jdbi.withHandle(handle -> handle.createQuery("select * from customers where name = :name and " +
                "surname = :surname and email = :email")
                .bind("name", name)
                .bind("surname", surname)
                .bind("email", email)
        .mapToBean(Customer.class).findFirst());
    }
}
