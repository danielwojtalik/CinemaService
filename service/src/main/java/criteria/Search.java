package criteria;

import model.Customer;

import java.util.List;

public interface Search {
    List<Customer> find(String value);
}
