package com.app.repository;

import com.app.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer> {

    List<Customer> findByName(String name);

    List<Customer> findBySurname(String surname);

    List<Customer> findByAge(int age);

    Optional<Customer> findByNameSurnameEmail(String name, String surname, String email);
}
