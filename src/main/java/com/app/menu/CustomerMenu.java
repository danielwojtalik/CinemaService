package com.app.menu;

import com.app.exceptions.MyException;
import com.app.model.Customer;
import com.app.service.cinema_service.CustomerService;
import com.app.service.utils.UserDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

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
                    case 1 -> option1();
                    case 2 -> option2();
                    case 3 -> option3();
                    case 4 -> option4();
                    case 5 -> continueLoop = false;
                }
            } catch (MyException e) {
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

    private void option1() {
        int id = retrieveCustomerIdFromUser("Please write Customer ID to delete",
                result -> result > 0);
        customerService.deleteById(id);
    }

    private int retrieveCustomerIdFromUser(String message, Predicate<Integer> callback) {
        return UserDataService.getIntWithValidator(message, callback);
    }

    private void option2() {
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
        Integer loyaltyCard = UserDataService.getIntWithValidator("Write new loyalty card of customer", lc -> lc >= 0);

        return Customer.builder()
                .id(id)
                .name(name)
                .surname(surname)
                .age(age)
                .email(emailAddress)
                .loyaltyCardId(loyaltyCard)
                .build();
    }

    private void option3() {
        List<Customer> customers = customerService.findAll();
        customers.forEach(System.out::println);
    }

    private void option4() {
        List<Customer> customers = customerService.getSearchResult();
        customers.forEach(System.out::println);
    }
}
