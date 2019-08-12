package com.app.main;

import com.app.service.menu_services.MainMenuService;

public class Main {

    public static void main(String[] args) {


//        CustomerRepositoryImpl customerRepository = new CustomerRepositoryImpl();
//        Customer c1 = Customer.builder()
//                .name("Jacek")
//                .surname("Pinkiewicz")
//                .age(14)
//                .email("jacek.pinkiewicz@gmail.com")
//                .build();
//
//        Customer c2 = Customer.builder()
//                .name("Agnieszka")
//                .surname("Kolodziejczyk")
//                .age(55)
//                .email("agnieszka@op.pl")
//                .build();
//        customerRepository.add(c1);
//        customerRepository.add(c2);

        new MainMenuService().selectOption();

//        customerRepository.deleteByID(3);
//        customerRepository.deleteAll();

//        List<Customer> customers = customerRepository.findAll();
//        customers.forEach(System.out::println);

    }
}
