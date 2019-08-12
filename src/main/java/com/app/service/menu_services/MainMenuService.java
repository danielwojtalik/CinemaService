package com.app.service.menu_services;

import com.app.exceptions.MyException;
import com.app.model.Customer;
import com.app.repository.impl.CustomerRepositoryImpl;
import com.app.service.UserDataService;
import com.app.service.cinema_service.impl.MovieServiceImpl;
import com.app.service.cinema_service.impl.SalesStandsServiceImpl;
import com.app.service.cinema_service.impl.TransactionHistoryServiceImpl;

public class MainMenuService {

    UserDataService dataService = UserDataService.getInstance();

    private static String NAME = "Write the name of customer:";
    private static String SURNAME = "Write the surname of customer:";
    private static String AGE = "Write the age of the customer";
    private static String EMAIL = "Write the email address of the customer:";

    private final String MAIN_MENU_CONTENT = "\n---MAIN MENU---\n" +
            "1. Add customer\n" +
            "2. Add new movie\n" +
            "3. Manage customers\n" +
            "4. Manage movies\n" +
            "5. Buy tickets\n" +
            "6. Transactions history\n" +
            "7. Statistics\n" +
            "9. Exit program";


    public Customer createCustomer() {
        Customer build = Customer.builder()
                .name(dataService.getString(NAME))
                .surname(dataService.getString(SURNAME))
                .age(dataService.getIntWithValidator(AGE, value -> value > 0))
                .email(dataService.getStringWithValidator(EMAIL, dataService::isMailAddress))
                .build();
        return build;
    }

    public void selectOption() {
        while (true) {
            try {
                int option;
                option = dataService.getIntWithValidator(MAIN_MENU_CONTENT, op -> op > 0 && op < 10);
                switch (option) {
                    case 1 -> new CustomerRepositoryImpl().add(createCustomer());
                    case 2 -> new MovieServiceImpl().addMovie();
                    case 3 -> new CustomerMenuService().manageCustomers();
                    case 4 -> new MovieMenuService().manageMovies();
                    case 5 -> new SalesStandsServiceImpl().sellTicket();
                    case 6 -> new TransactionHistoryServiceImpl().showResults();
                    case 9 -> {
                        dataService.close();
                        return;
                    }
                }
            } catch (MyException e) {
                System.err.println(e.getExceptionInfo().getDescription());
                System.err.println(e.getExceptionInfo().getExceptionCode());
            }

        }
    }
}
