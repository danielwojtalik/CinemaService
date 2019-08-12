package com.app.service.menu_services;

import com.app.exceptions.MyException;
import com.app.service.cinema_service.impl.MovieServiceImpl;
import com.app.service.UserDataService;

public class MovieMenuService {

    private UserDataService dataService = UserDataService.getInstance();

    private static String PRICE = "Write new price for the movie";

    private final String MOVIE_MENU_CONTENT = "---MOVIE MENU---\n" +
            "1. Delete movie by id\n" +
            "2. Update price of movie\n" +
            "3. Show all movie\n" +
            "4. Search movie by...\n" +
            "5. Back to MAIN MENU\n";


    public void manageMovies() {
        boolean continueLoop = true;
        while (continueLoop) {
            try {
                int option;
                option = dataService.getIntWithValidator(MOVIE_MENU_CONTENT, op -> op > 0 && op < 6);
                switch (option) {
                    case 1 -> new MovieServiceImpl().deleteById();
                    case 2 -> new MovieServiceImpl().updatePrice();
                    case 3 -> new MovieServiceImpl().showAll();
                    case 4 -> new MovieServiceImpl().showFiltered();
                    case 5 -> {
                        continueLoop = false;
                        return;
                    }
                }
            } catch (MyException e) {
                System.err.println(e.getExceptionInfo().getDescription());
                System.err.println(e.getExceptionInfo().getExceptionDate());
            }
        }
    }


}



