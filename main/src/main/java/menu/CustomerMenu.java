package menu;

import exceptions.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import model.Customer;
import cinemaservice.CustomerService;
import utils.UserDataService;

import java.util.List;
import java.util.function.Predicate;

@Log4j
@RequiredArgsConstructor
public class CustomerMenu {
    private final CustomerService customerService;

    public void manageCustomers() {
        boolean continueLoop = true;
        while (continueLoop) {
            try {
                int option = chooseOption();
                switch (option) {
                    case 1 -> deleteCustomerById();
                    case 2 -> updateDataOfCustomer();
                    case 3 -> showAllCustomers();
                    case 4 -> searchCustomerBy();
                    case 5 -> continueLoop = false;
                }
            } catch (CustomException e) {
                System.err.println(e.getExceptionInfo().getDescription());
            }
        }
    }

    private int chooseOption() {
        System.out.println("\n--------------------------------------");
        System.out.println("1. Delete customer by id");
        System.out.println("2. Update data of customer");
        System.out.println("3. Show all customers");
        System.out.println("4. Search customers by...");
        System.out.println("5. Back to MAIN MENU");

        return UserDataService.getIntWithValidator("Choose option:", op -> op > 0 && op < 6);
    }

    private void deleteCustomerById() {
        int id = retrieveCustomerIdFromUser("Please write Customer ID to delete",
                result -> result > 0);
        customerService.deleteById(id);
    }

    private int retrieveCustomerIdFromUser(String message, Predicate<Integer> callback) {
        return UserDataService.getIntWithValidator(message, callback);
    }

    private void updateDataOfCustomer() {
        Customer newCustomer = createCustomerTuUpdate();
        customerService.update(newCustomer);
    }

    private Customer createCustomerTuUpdate() {
        Integer id = UserDataService.getIntWithValidator("Write the id of customer which you want to update", idd -> idd > 0);
        String name = UserDataService.getString("Write the new name of customer");
        String surname = UserDataService.getString("Write the new surname of customer");
        Integer age = UserDataService.getIntWithValidator("Write the new age of customer ", a -> a > 10);
        String emailAddress = UserDataService.getStringWithValidator("Write the new email address of customer",
                UserDataService::isMailAddress);
        Integer loyaltyCard = UserDataService.getIntWithValidator("Write new loyalty card of customer (if customer should not have loyaltyCard write 0)", lc -> lc >= 0);
        loyaltyCard = loyaltyCard != 0 ? loyaltyCard : null;

        return Customer.builder()
                .id(id)
                .name(name)
                .surname(surname)
                .age(age)
                .email(emailAddress)
                .loyaltyCardId(loyaltyCard)
                .build();
    }

    private void showAllCustomers() {
        List<Customer> customers = customerService.findAll();
        customers.forEach(System.out::println);
    }

    private void searchCustomerBy() {
        List<Customer> customers = customerService.getSearchResult();
        customers.forEach(System.out::println);
    }
}
