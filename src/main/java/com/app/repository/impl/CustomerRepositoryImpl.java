package com.app.repository.impl;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Customer;
import com.app.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

import static com.app.service.cinema_service.SalesStandsService.customerRepository;
import static com.app.service.cinema_service.SalesStandsService.dataService;

public class CustomerRepositoryImpl implements CustomerRepository {

    @Override
    public void add(Customer customer) {
        int numbersOfInsertedRows = jdbi.withHandle(handle -> handle
                .createUpdate("insert into customers (name, surname, age, email, loyalty_card_id) " +
                        "values (:name, :surname, :age, :email, :loyalty_card_id)")
                .bind("name", customer.getName())
                .bind("surname", customer.getSurname())
                .bind("age", customer.getAge())
                .bind("email", customer.getEmail())
                .bind("loyalty_card_id", customer.getLoyaltyCardId())
                .execute()
        );
        System.out.println(numbersOfInsertedRows);
    }

    @Override
    public void update(Customer customer) {
        Customer customerFromDb = findById(customer.getId()).orElseThrow(() -> new MyException("NO CUSTOMER WITH ID", ExceptionCode.REPOSITORY));

        int numbersOfUpdatedRows = jdbi.withHandle(handle -> handle
                .createUpdate("update customers set name = :name, surname = :surname, age = :age, email = :email," +
                        " loyalty_card_id = :loyalty_card_id where id =:id")
                .bind("id", customer.getId())
                .bind("name", customer.getName() != null ? customer.getName() : customerFromDb.getName())
                .bind("surname", customer.getSurname() != null ? customer.getSurname() : customerFromDb.getSurname())
                .bind("age", customer.getAge() != null ? customer.getAge() : customerFromDb.getAge())
                .bind("email", customer.getEmail() != null ? customer.getEmail() : customerFromDb.getEmail())
                .bind("loyalty_card_id", customer.getLoyaltyCardId() != null ? customer.getLoyaltyCardId() : customerFromDb.getLoyaltyCardId())
                .execute());
        System.out.println(numbersOfUpdatedRows);
    }


    @Override
    public Optional<Customer> findById(Integer id) {

        return jdbi.withHandle(handle -> handle.createQuery("select * from customers where id = :id")
                .bind("id", id)
                .mapToBean(Customer.class)
                .findFirst()
        );
    }

    @Override
    public List<Customer> findAll() {
        return jdbi.withHandle(handle -> handle.createQuery("select * from customers")
                .mapToBean(Customer.class)
                .list()
        );
    }

    @Override
    public void deleteByID(Integer id) {
        jdbi.useTransaction(handle -> handle.createUpdate("delete from customers where id = :id")
                .bind("id", id)
                .execute());
    }

    @Override
    public void deleteAll() {
        jdbi.useTransaction(handle -> handle.createUpdate("delete * from customers")
                .execute()
        );
    }

    @Override
    public List<Customer> findByName(String name) {
        return jdbi.withHandle(handle -> handle.createQuery("select * from customers where name = :name")
                .bind("name", name)
                .mapToBean(Customer.class)
                .list());
    }

    @Override
    public List<Customer> findBySurname(String surname) {
        return jdbi.withHandle(handle -> handle.createQuery("select * from customers where surname = :surname")
                .bind("surname", surname)
                .mapToBean(Customer.class)
                .list());
    }

    @Override
    public List<Customer> findByAge(int age) {
        return jdbi.withHandle(handle -> handle.createQuery("select * from customers where age = :age")
                .bind("age", age)
                .mapToBean(Customer.class)
                .list());
    }

    @Override
    public Optional<Customer> findByNameSurnameEmail(String name, String surname, String email) {
        if (name == null) {
            throw new MyException("CUSTOMER NAME IS NULL", ExceptionCode.REPOSITORY);
        }
        if (surname == null) {
            throw new MyException("CUSTOMER SURNAME IS NULL", ExceptionCode.REPOSITORY);
        }
        if (email == null) {
            throw new MyException("CUSTOMER EMAIL IS NULL", ExceptionCode.REPOSITORY);
        }

        return jdbi.withHandle(handle -> handle.createQuery("select * from customers where name = :name and " +
                "surname = :surname and email = :email")
                .bind("name", name)
                .bind("surname", surname)
                .bind("email", email)
        .mapToBean(Customer.class).findFirst());
    }

    @Override
    public Optional<Customer> findCustomerByPersonalDateFromUser() {
        Optional<Customer> customer;
        String name = dataService.getString("Write the name of customer");
        String surname = dataService.getString("Write the surname of customer");
        String email = dataService.getString("Write the email address of customer");
        return customerRepository.findByNameSurnameEmail(name, surname, email);
    }

}
