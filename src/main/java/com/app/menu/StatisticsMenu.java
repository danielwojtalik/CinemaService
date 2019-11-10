package com.app.menu;

import com.app.model.Customer;
import com.app.model.Movie;
import com.app.model.MovieType;
import com.app.service.cinema_service.StatisticsService;
import com.app.service.utils.UserDataService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class StatisticsMenu {

    private final StatisticsService statisticsService;

    private int chooseOption() {
        System.out.println("\n--------------------------------");
        System.out.println("1. Best client in each movie category");
        System.out.println("2. How many tickets sold in each category in time range");
        System.out.println("3. How many times client bought ticket for each movie");
        System.out.println("4. Total price for all tickets with discount");
        System.out.println("5. Back to Main Menu");
        return UserDataService.getIntWithValidator("Choose option", op -> op > 0 && op < 6);
    }

    public void manageStatistics() {
        while (true) {
            int option = chooseOption();
            switch (option) {
                case 1 -> option1();
                case 2 -> option2();
                case 3 -> option3();
                case 4 -> option4();
                case 5 -> {
                    return;
                }
            }
        }

    }

    private void option1() {
        System.out.println("CLIENT WHO SPENT MOST MONEY FOR PARTICULAR MOVIE TYPE (GENRE)...");
        Map<MovieType, Customer> bestCustomersInMovieTypes = statisticsService.retrieveBestCustomerForCategory();
        bestCustomersInMovieTypes
                .forEach((key, value) -> System.out.println(key.toString() + ":\n" + value.getName() + " "
                        + value.getSurname() + " with id  = " + value.getId()));
    }

    private void option2() {
        System.out.println("\nHOW MANY TICKETS HAS BEEN SOLD FOR EVERY MOVIE TYPE IN TIME SLOT...");
        LocalDate startDate = UserDataService.getDate("Write start date:");
        LocalDate finishDate = UserDataService.getDate("Write finish date:");

        Map<MovieType, Long> ticketSoldInCategory = statisticsService.
                retrieveAllTicketSalesInEachCategoryInTimeRange(startDate, finishDate);

        ticketSoldInCategory
                .forEach((key, value) -> System.out.println(key.toString() + ": " + value));
    }

    private void option3() {
        System.out.println("\nHOW MANY TIMES CUSTOMER BOUGHT TICKET FOR EACH MOVIE");
        Map<Customer, Map<Movie, Long>> customersWithAmountOfParticularMovie = statisticsService.retrieveNumberOfEachMoviesForClient();
        customersWithAmountOfParticularMovie.forEach((key, value) -> System.out.println("Customer " + key.getName()
                + " " + key.getSurname() + " with ID equal " + key.getId() + " has bought ticket for movies: \n"
                + value.entrySet().stream().map(entry -> entry.getKey().getTitle() + " " + entry.getValue() + " times")
                .collect(Collectors.joining("; "))));
    }

    private void option4() {
        System.out.println("\n TOTAL PRICE FOR ALL TICKETS IS..." + statisticsService.retrieveTotalTicketPriceSoldInCinema());
    }
}
