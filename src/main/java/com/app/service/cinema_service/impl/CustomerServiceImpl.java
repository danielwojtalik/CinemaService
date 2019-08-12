package com.app.service.cinema_service.impl;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Customer;
import com.app.repository.impl.CustomerRepositoryImpl;
import com.app.service.UserDataService;
import com.app.service.cinema_service.CustomerService;
import com.app.service.cinema_service.ItemService;
import com.app.service.menu_services.SearchCriteria;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    private static final String CRITERIA_OPTIONS = "\n---CRITERIA OPTIONS---\n" +
            "1. Search by name\n" +
            "2. Search by surname\n" +
            "3. Search by name";

    private UserDataService dataService = UserDataService.getInstance();
    private CustomerRepositoryImpl customerRepository = new CustomerRepositoryImpl();

    public CustomerServiceImpl() {
    }

    @Override
    public void deleteById() {
        int id;
        id = dataService.getIntWithValidator("ID of user to delete", result -> result > 0);
        customerRepository.deleteByID(id);
    }

    @Override
    public void showAll() {
        customerRepository.findAll().forEach(System.out::println);
    }


    @Override
    public void showFiltered() {
        List<Customer> customers = getSearchResult();
        System.out.println("All customers pass the criteria:");
        customers.forEach(System.out::println);
    }

    @Override
    public SearchCriteria getSearchCriterion() {
        int option = dataService.getIntWithValidator(CRITERIA_OPTIONS, op -> op > 0 && op < 5);
        SearchCriteria searchCriteria = null;
        switch (option) {
            case 1 -> searchCriteria = SearchCriteria.BY_NAME;
            case 2 -> searchCriteria = SearchCriteria.BY_SURNAME;
            case 3 -> searchCriteria = SearchCriteria.BY_AGE;
        }
        return searchCriteria;
    }

    @Override
    public List<Customer> getSearchResult() {
        SearchCriteria searchCriteria = getSearchCriterion();
        if (searchCriteria == SearchCriteria.BY_NAME) {
            return findByName();
        } else if (searchCriteria == SearchCriteria.BY_SURNAME) {
            return findBySurname();
        } else if (searchCriteria == SearchCriteria.BY_AGE) {
            return findByAge();
        } else {
            return null;
        }
    }

    public List<Customer> findByName() {
        String name = dataService.getString("Write the name of search users");
        return customerRepository.findByName(name);
    }

    private List<Customer> findBySurname() {
        String surname = dataService.getString("Write the surname of search users");
        return customerRepository.findBySurname(surname);
    }

    private List<Customer> findByAge() {
        int age = dataService.getIntWithValidator("Write the age of customer", a -> a > 10);
        return customerRepository.findByAge(age);
    }

    // TODO: 11.07.2019 Sprawdz czy istnieje id karty lojalnosciowej takie na jakie chcesz zmienic i jezeli nie to pozostaw null - loyalty card repository required
    public void update() {
        Integer id = dataService.getIntWithValidator("Write the id of customer which you want to update", idd -> idd > 0);
        String name = dataService.getString("Write the new name of customer");
        String surname = dataService.getString("Write the new surname of customer");
        Integer age = dataService.getIntWithValidator("Write the new age of customer ", a -> a > 10);
        String emailAddress = dataService.getStringWithValidator("Write the new email address of customer",
                dataService::isMailAddress);
        Integer loyaltyCard = dataService.getIntWithValidator("Write new loyalty card of customer", lc -> lc > 0);
        Customer c1 = Customer.builder()
                .id(id)
                .name(name)
                .surname(surname)
                .age(age)
                .email(emailAddress)
                .loyaltyCardId(loyaltyCard)
                .build();
        try {
            customerRepository.update(c1);
        } catch (UnableToExecuteStatementException e) {
            /*c1.setLoyaltyCardId(null);
            customerRepository.update(c1);*/
            throw new MyException("CAN NOT UPDATE USER", ExceptionCode.CUSTOMER_SERVICE);
        }
    }
}
