package com.app.menu;

import com.app.exceptions.MyException;
import com.app.model.*;
import com.app.service.cinema_service.*;
import com.app.service.email_service.EmailService;
import com.app.service.utils.UserDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static com.app.main.ApplicationConstants.MOVIES_AMOUNT_FOR_FIRST_LOYALTY_CARD;

@Log4j
@RequiredArgsConstructor
public final class MainMenu {

    private static final String NAME = "Write the name of customer:";
    private static final String SURNAME = "Write the surname of customer:";
    private static final String AGE = "Write the age of the customer";
    private static final String EMAIL = "Write the email address of the customer:";

    private final CustomerService customerService;
    private final MovieService movieService;
    private final SalesStandsService salesStandsService;
    private final LoyaltyCardService loyaltyCardService;
    private final TransactionHistoryService transactionHistoryService;
    private final StatisticsService statisticsService;

    public void runApplication() {
        while (true) {
            try {
                int option = chooseOption();
                switch (option) {
                    case 1 -> option1();
                    case 2 -> option2();
                    case 3 -> option3();
                    case 4 -> option4();
                    case 5 -> option5();
                    case 6 -> option6();
                    case 7 -> option7();
                    case 8 -> {
                        option8();
                        return;
                    }
                }
            } catch (MyException e) {
                System.err.println(e.getExceptionInfo().getDescription());
                System.err.println(e.getExceptionInfo().getExceptionCode());
                System.err.println(e.getMessage());
            }

        }
    }

    private int chooseOption() {
        System.out.println("\n--------------------------------------");
        System.out.println("1. Add customer");
        System.out.println("2. Add new movie");
        System.out.println("3. Manage customers");
        System.out.println("4. Manage movies");
        System.out.println("5. Sell ticket");
        System.out.println("6. Transactions history");
        System.out.println("7. Statistics");
        System.out.println("8. Close application");
        return UserDataService.getIntWithValidator("Choose option:", op -> op > 0 && op < 10);
    }

    private void option1() {
        Customer customer = createCustomer();
        customerService.addCustomer(customer);
    }

    public Customer createCustomer() {
        return Customer.builder()
                .name(UserDataService.getString(NAME))
                .surname(UserDataService.getString(SURNAME))
                .age(UserDataService.getIntWithValidator(AGE, value -> value > 0))
                .email(UserDataService.getStringWithValidator(EMAIL, UserDataService::isMailAddress))
                .build();
    }

    private void option2() {
        Movie movie = retrieveMovie();
        movieService.addMovie(movie);
    }

    private Movie retrieveMovie() {
        String movieTitle = UserDataService.getString("Please write movie title");
        return movieService.retrieveMovieFromTitle(movieTitle);
    }

    private void option3() {
        CustomerMenu customerMenu = new CustomerMenu(customerService);
        customerMenu.manageCustomers();
    }

    private void option4() {
        MovieMenu movieMenu = new MovieMenu(movieService);
        movieMenu.manageMovies();
    }

    private void option5() {
        // find customer in db
        Customer customer = getCustomerFromDB();
        // choose movie by customer
        movieService.returnAllMovies().forEach(System.out::println);
        int kk = 0;
        int id = retrieveMovieIdFromUser("Please write movie id", mid -> mid > kk);
        Movie movie = movieService.findMovieById(id);

        // choose start time of the movie
        Map<Integer, LocalTime> availableTime = salesStandsService.getAvailableTime();
        LocalTime startTime = retrieveMovieStartTimeFromUser(availableTime);

        // sell ticket
        LoyaltyCard loyaltyCard = customer.getLoyaltyCardId() != null ? loyaltyCardService
                .findLoyaltyCardById(customer.getLoyaltyCardId()) : null;
        SalesStand salesStandToAdd = new SalesStand();
        salesStandToAdd.setCustomerId(customer.getId());
        salesStandToAdd.setMovieId(movie.getId());
        salesStandToAdd.setStartTime(startTime);
        salesStandToAdd.setPriceWithDiscount(loyaltyCard != null ? (loyaltyCard.getDiscount().add(BigDecimal.ONE))
                .multiply(movie.getPrice()) : movie.getPrice());
        salesStandsService.sellTicket(salesStandToAdd);

        // update movies amount bought by customer in loyalty card if exists
        if (customer.getLoyaltyCardId() != null) {
            loyaltyCardService.updateMoviesQuantity(customer);

        }
        // menage loyalty card
        int ticketsBoughtByCustomer = salesStandsService.getTotalTicketAmountBoughtByCustomer(customer);
        if (MOVIES_AMOUNT_FOR_FIRST_LOYALTY_CARD <= ticketsBoughtByCustomer && customer.getLoyaltyCardId() == null) {
            boolean isWilling = UserDataService.makeDecision("You have bought " + MOVIES_AMOUNT_FOR_FIRST_LOYALTY_CARD +
                    " times in our cinema. Do you like get loyalty card?");
            salesStandsService.offerFirstLoyaltyCard(customer, isWilling);
        } else if (customer.getLoyaltyCardId() != null && loyaltyCard != null) {
            int availableAmountOfTickets = loyaltyCard.getMoviesQuantity();
            int currentAmountOfTickets = loyaltyCard.getCurrentMoviesQuantity();
            LocalDate expiredCardDate = loyaltyCard.getExpirationDate();
            LocalDate today = LocalDate.now();
            if (currentAmountOfTickets >= availableAmountOfTickets || expiredCardDate.isBefore(today)) {
                boolean isWilling = UserDataService.makeDecision("Your loyalty card has expired or " +
                        "you have rich the movie limit from card. Do you like get new loyalty card?");
                salesStandsService.offerNewCardIfPossible(customer, isWilling);
            }
        }

        // send email
        String message = salesStandsService.prepareConfirmationMessage(salesStandToAdd);
        EmailService.sendAsHtml(customer.getEmail(), "Confirmation", message);
    }

    private Customer getCustomerFromDB() {
        String name = UserDataService.getString("Write the name of customer");
        String surname = UserDataService.getString("Write the surname of customer");
        String email = UserDataService.getString("Write the email address of customer");
        return customerService.findByNameSurnameEmail(name, surname, email);
    }

    public LocalTime retrieveMovieStartTimeFromUser(Map<Integer, LocalTime> availableHours) {
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        availableHours.forEach((k, v) -> System.out.println("option: (" + k + ") ---> " + timeFormat.format(v)));
        int choseOption = UserDataService.getIntWithValidator("Choose time for start movie",
                op -> op > 0 && op < availableHours.size() + 1);
        return availableHours.get(choseOption);
    }

    public int retrieveMovieIdFromUser(String message, Predicate<Integer> callback) {
        return UserDataService.getIntWithValidator(message, callback);
    }

    public void option6() {
        // find movies bought by customer
        Customer customer = getCustomerFromDB();
        // get customer preferences
        List<Predicate<Movie>> predicates = setupMoviesSearchConfiguration(customer);
        List<Movie> moviesAfterFilter = movieService.retrieveAllMoviesForCustomerWithFilters(predicates, customer);
        showMovieFromHistory(moviesAfterFilter);
        //send email
        String transactionHistory = transactionHistoryService.prepareContentOfTransactionHistory(moviesAfterFilter, customer);
        EmailService.sendAsHtml(customer.getEmail(), "Transaction history", transactionHistory);
    }

    private List<Predicate<Movie>> setupMoviesSearchConfiguration(Customer customer) {
        List<Predicate<Movie>> predicates = new ArrayList<>();
        if (UserDataService.makeDecision("Do you need genre filter?")) {
            int genre = UserDataService.getIntWithValidator("Choose one genre which you are interested in." +
                    "Any other value disable this filter:\n" +
                    "1. DRAMA\n" +
                    "2. COMEDY\n" +
                    "3. SCI_FI\n" +
                    "4. THRILLER", op -> op > 0 && op < 5);
            Predicate<Movie> genrePredicate = (m -> m.getGenre() == getMovieType(genre));
            predicates.add(genrePredicate);
        }
        if (UserDataService.makeDecision("Do you need release date filter?")) {
            LocalDate fromReleaseDate = UserDataService.getDate("Write date from...");
            LocalDate toReleaseDate = UserDataService.getDate("Write date to...");
            Predicate<Movie> releaseDatePredicate = (movie -> movie.getReleaseDate().isAfter(fromReleaseDate) &&
                    movie.getReleaseDate().isBefore(toReleaseDate));
            predicates.add(releaseDatePredicate);
        }
        if (UserDataService.makeDecision("Do you need duration filter?")) {
            int minDuration = UserDataService.getIntWithValidator("Write the mininum duration in minutes:", d -> d > 0);
            int maxDuration = UserDataService.getIntWithValidator("Write the maximum duration in minutes:", d -> d > minDuration);

            Predicate<Movie> durationPredicate = (movie -> movie.getDuration() > minDuration &&
                    movie.getDuration() < maxDuration);
            predicates.add(durationPredicate);
        }
        return predicates;
    }

    private void showMovieFromHistory(List<Movie> movies) {
        if (movies.isEmpty()) {
            System.out.println("Unfortunately, in the history is any movie which meets your requirements");
        } else {
            System.out.println("All movies which meet requirements of searching:");
            movies.forEach(System.out::println);
        }
    }

    private MovieType getMovieType(int option) {
        MovieType movieType;
        switch (option) {
            case 1 -> movieType = MovieType.DRAMA;
            case 2 -> movieType = MovieType.COMEDY;
            case 3 -> movieType = MovieType.SCI_FI;
            case 4 -> movieType = MovieType.THRILLER;
            default -> movieType = null;
        }
        return movieType;
    }

    private void option7() {
        new StatisticsMenu(statisticsService).manageStatistics();
    }

    private void option8() {
        log.info("The program has been finished by user");
        UserDataService.close();
    }
}
