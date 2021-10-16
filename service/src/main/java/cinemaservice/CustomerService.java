package cinemaservice;

import criteria.SearchFactory;
import exceptions.ExceptionCode;
import exceptions.CustomException;
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
    private final SearchFactory searchFactory = new SearchFactory();

    public Customer findCustomerById(int id) {
        if (id < 1) {
            throw new CustomException("id is less than 1", ExceptionCode.CUSTOMER_SERVICE);
        }

        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomException("customer with id " + id + " does not exists", ExceptionCode.CUSTOMER_SERVICE));
    }

    public List<Customer> findByName(String name) {
        if (name == null) {
            throw new CustomException("Customer name cannot be equal to null", ExceptionCode.CUSTOMER_SERVICE);
        }

        return customerRepository.findByName(name);
    }

    public List<Customer> findBySurname(String surname) {
        if (surname == null) {
            throw new CustomException("Customer surname cannot be equal to null", ExceptionCode.CUSTOMER_SERVICE);
        }

        return customerRepository.findBySurname(surname);
    }

    public List<Customer> findByAge(int age) {
        if (age < 0 || age > 130) {
            throw new CustomException("Invalid value of user age", ExceptionCode.CUSTOMER_SERVICE);
        }

        return customerRepository.findByAge(age);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer findByNameSurnameEmail(String name, String surname, String email) {
        if (name == null) {
            throw new CustomException("customer name is null", ExceptionCode.CUSTOMER_SERVICE);
        }
        if (surname == null) {
            throw new CustomException("customer surname is null", ExceptionCode.CUSTOMER_SERVICE);
        }
        if (email == null) {
            throw new CustomException("customer email is null", ExceptionCode.CUSTOMER_SERVICE);
        }

        return customerRepository.findByNameSurnameEmail(name, surname, email).orElseThrow(
                () -> new CustomException("customer with name " + name + ", surname " + surname + " email " + email + " does not exists",
                        ExceptionCode.CUSTOMER_SERVICE));
    }

    public void addCustomer(Customer customer) {
        if (customer == null) {
            throw new CustomException("CUSTOMER IS NULL", ExceptionCode.CUSTOMER_SERVICE);
        }

        customerRepository.add(customer);
    }

    public Customer update(Customer updatedCustomer) {
        this.findCustomerById(updatedCustomer.getId());
            return customerRepository.update(updatedCustomer)
                    .orElseThrow(() -> new CustomException("can not update user with id " + updatedCustomer.getId(), ExceptionCode.CUSTOMER_SERVICE));
    }

    public Customer deleteById(int id) {
        if (id < 1) {
            throw new CustomException("id is less than 0", ExceptionCode.CUSTOMER_SERVICE);
        }

        return customerRepository.deleteByID(id)
                .orElseThrow(() -> new CustomException("customer with id " + id + " does not exists", ExceptionCode.CUSTOMER_SERVICE));
    }

    private static final String CRITERIA_OPTIONS = "\n---CRITERIA OPTIONS---\n" +
            "1. Search by name\n" +
            "2. Search by surname\n" +
            "3. Search by age";

    private final CustomerRepository customerRepository;

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
            String name = UserDataService.getString("Write customer name");
            return searchFactory.getSearchAlgorithm(searchCriteria).find(name);
        } else if (searchCriteria == SearchCriteria.BY_SURNAME) {
            String surname = UserDataService.getString("Write Customer surname");
            return searchFactory.getSearchAlgorithm(searchCriteria).find(surname);
        }
        String age = UserDataService.getString("Write the age of customer");
        return searchFactory.getSearchAlgorithm(searchCriteria).find(age);


    }

    private List<Customer> findBySurname() {
        String surname = UserDataService.getString("Write the surname of search users");
        return customerRepository.findBySurname(surname);
    }

}


