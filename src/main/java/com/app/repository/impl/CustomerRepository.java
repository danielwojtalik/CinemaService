package com.app.repository.impl;

import com.app.model.Customer;
import com.app.repository.AbstractCrudRepository;

import java.util.List;
import java.util.Optional;

public class CustomerRepository extends AbstractCrudRepository<Customer, Integer> {

    // TODO: 08.09.2019 sprawdzic czy nie wystepuje identyczny customer w bazie danych - imie, nazwisko, email.
    //  jezeli wystepuje to moze byc problem pozniej przy wyszukiwaniu
//    @Override
//    public void add(Customer customer) {
//        int numbersOfInsertedRows = jdbi.withHandle(handle -> handle
//                .createUpdate("insert into customers (name, surname, age, email, loyalty_card_id) " +
//                        "values (:name, :surname, :age, :email, :loyalty_card_id)")
//                .bind("name", customer.getName())
//                .bind("surname", customer.getSurname())
//                .bind("age", customer.getAge())
//                .bind("email", customer.getEmail())
//                .bind("loyalty_card_id", customer.getLoyaltyCardId())
//                .execute()
//        );
//        System.out.println(numbersOfInsertedRows);
//    }

//    @Override
//    public void update(Customer customer) {
//        Customer customerFromDb = findById(customer.getId()).orElseThrow(() -> new MyException("NO CUSTOMER WITH ID", ExceptionCode.REPOSITORY));
//
//        int numbersOfUpdatedRows = jdbi.withHandle(handle -> handle
//                .createUpdate("update customers set name = :name, surname = :surname, age = :age, email = :email," +
//                        " loyalty_card_id = :loyalty_card_id where id =:id")
//                .bind("id", customer.getId())
//                .bind("name", customer.getName() != null ? customer.getName() : customerFromDb.getName())
//                .bind("surname", customer.getSurname() != null ? customer.getSurname() : customerFromDb.getSurname())
//                .bind("age", customer.getAge() != null ? customer.getAge() : customerFromDb.getAge())
//                .bind("email", customer.getEmail() != null ? customer.getEmail() : customerFromDb.getEmail())
//                .bind("loyalty_card_id", customer.getLoyaltyCardId() != null ? customer.getLoyaltyCardId() : customerFromDb.getLoyaltyCardId())
//                .execute());
//        System.out.println(numbersOfUpdatedRows);
//    }


//    @Override
//    public Optional<Customer> findById(Integer id) {
//        return jdbi.withHandle(handle -> handle.createQuery("select * from customers where id = :id")
//                .bind("id", id)
//                .mapToBean(Customer.class)
//                .findFirst()
//        );
//    }
//
//    @Override
//    public List<Customer> findAll() {
//        return jdbi.withHandle(handle -> handle.createQuery("select * from customers")
//                .mapToBean(Customer.class)
//                .list()
//        );
//    }

//    @Override
//    public void deleteByID(Integer id) {
//        jdbi.useTransaction(handle -> handle.createUpdate("delete from customers where id = :id")
//                .bind("id", id)
//                .execute());
//    }
//
//    @Override
//    public void deleteAll() {
//        jdbi.useTransaction(handle -> handle.createUpdate("delete * from customers")
//                .execute()
//        );
//    }

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
