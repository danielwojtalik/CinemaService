package com.app.service.menu_services;

import com.app.exceptions.MyException;
import com.app.service.cinema_service.impl.CustomerServiceImpl;
import com.app.service.UserDataService;

public class CustomerMenuService {

    UserDataService dataService = UserDataService.getInstance();

    private final String CUSTOMER_MENU_CONTENT = "\n---CUSTOMER MENU---\n" +
            "1. Delete customer by id\n" +
            "2. Update data of customer\n" +
            "3. Show all customers\n" +
            "4. Search customers by...\n" +
            "5. Back to MAIN MENU";


    public void manageCustomers() {
        boolean continueLoop = true;
        while (continueLoop) {
            try {
                int option;
                option = dataService.getIntWithValidator(CUSTOMER_MENU_CONTENT, op -> op > 0 && op < 6);
                switch (option) {
                    case 1 -> new CustomerServiceImpl().deleteById();
                    case 2 -> new CustomerServiceImpl().update();
                    case 3 -> new CustomerServiceImpl().showAll();
                    case 4 -> new CustomerServiceImpl().showFiltered();
                    case 5 -> {
                        continueLoop = false;
                        return;
                    }
                }
            } catch (MyException e) {
                System.err.println(e.getExceptionInfo().getDescription());
            }

        }
    }
}
