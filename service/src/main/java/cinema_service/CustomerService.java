package cinema_service;

import exceptions.ExceptionCode;
import exceptions.MyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import model.Customer;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import repository.impl.CustomerRepository;
import criteria.SearchCriteria;
import utils.UserDataService;

import java.util.List;

@Log4j
@RequiredArgsConstructor
public class CustomerService {

    private static final String CRITERIA_OPTIONS = "\n---CRITERIA OPTIONS---\n" +
            "1. Search by name\n" +
            "2. Search by surname\n" +
            "3. Search by name";

    private final CustomerRepository customerRepository;

    public void addCustomer(Customer customer) {
        if (customer == null) {
            throw new MyException("CUSTOMER IS NULL", ExceptionCode.CUSTOMER_SERVICE);
        }

        customerRepository.add(customer);
    }

    public Customer deleteById(int id) {
        if (id < 1) {
            throw new MyException("id is less than 0", ExceptionCode.CUSTOMER_SERVICE);
        }

         return customerRepository.deleteByID(id)
                 .orElseThrow(() -> new MyException("customer with id " + id + " does not exists", ExceptionCode.CUSTOMER_SERVICE));
    }

    public Customer findCustomerById(int id) {
        if (id < 1) {
            throw new MyException("id is less than 0", ExceptionCode.CUSTOMER_SERVICE);
        }

        return customerRepository.findById(id)
                .orElseThrow(() -> new MyException("customer with id " + id + " does not exists", ExceptionCode.CUSTOMER_SERVICE));
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public SearchCriteria getSearchCriterion() {
        int option = UserDataService.getIntWithValidator(CRITERIA_OPTIONS, op -> op > 0 && op < 5);
        SearchCriteria searchCriteria = null;
        switch (option) {
            case 1 -> searchCriteria = SearchCriteria.BY_NAME;
            case 2 -> searchCriteria = SearchCriteria.BY_SURNAME;
            case 3 -> searchCriteria = SearchCriteria.BY_AGE;
        }

        return searchCriteria;
    }

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
        String name = UserDataService.getString("Write the name of search users");
        return customerRepository.findByName(name);
    }

    private List<Customer> findBySurname() {
        String surname = UserDataService.getString("Write the surname of search users");
        return customerRepository.findBySurname(surname);
    }

    private List<Customer> findByAge() {
        int age = UserDataService.getIntWithValidator("Write the age of customer", a -> a > 10);
        return customerRepository.findByAge(age);
    }

    // TODO: 02.10.2020 Poznac logike tego bo jest cos ewidentnie nie tak
    public void update(Customer newCustomer) {
        try {
            if (newCustomer.getLoyaltyCardId() == 0) {
                newCustomer.setLoyaltyCardId(null);
            }
            customerRepository.update(newCustomer);
        } catch (UnableToExecuteStatementException e) {
            newCustomer.setLoyaltyCardId(null);
            customerRepository.update(newCustomer);
            throw new MyException("can not update user", ExceptionCode.CUSTOMER_SERVICE);
        }
    }

    public Customer findByNameSurnameEmail(String name, String surname, String email) {
        if (name == null) {
            throw new MyException("customer name is null", ExceptionCode.CUSTOMER_SERVICE);
        }
        if (surname == null) {
            throw new MyException("customer surname is null", ExceptionCode.CUSTOMER_SERVICE);
        }
        if (email == null) {
            throw new MyException("customer email is null", ExceptionCode.CUSTOMER_SERVICE);
        }

        return customerRepository.findByNameSurnameEmail(name, surname, email).orElseThrow(
                () -> new MyException("customer with name " + name + ", surname " + surname + " email " + email + " does not exists", ExceptionCode.CUSTOMER_SERVICE));
    }
}


