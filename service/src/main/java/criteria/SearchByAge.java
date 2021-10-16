package criteria;

import cinemaservice.CustomerService;
import model.Customer;
import repository.impl.CustomerRepository;

import java.util.List;

public class SearchByAge implements Search{
    CustomerService customerService = new CustomerService(new CustomerRepository());

    @Override
    public List<Customer> find(String value) {
        int age = Integer.parseInt(value);
        return customerService.findByAge(age);
    }
}
