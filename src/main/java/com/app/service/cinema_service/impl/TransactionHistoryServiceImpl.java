package com.app.service.cinema_service.impl;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Customer;
import com.app.model.Movie;
import com.app.model.MovieType;
import com.app.repository.CustomerRepository;
import com.app.repository.SalesStandsRepository;
import com.app.repository.impl.CustomerRepositoryImpl;
import com.app.repository.impl.SalesStandsRepositoryImpl;
import com.app.service.cinema_service.TransactionHistoryService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TransactionHistoryServiceImpl implements TransactionHistoryService {

    private List<Movie> movies;
    private List<Predicate<Movie>> predicates;

    public TransactionHistoryServiceImpl() {
        SalesStandsRepository standsRepository = new SalesStandsRepositoryImpl();
        CustomerRepository customerRepository = new CustomerRepositoryImpl();
        Customer customer = customerRepository.findCustomerByPersonalDateFromUser().orElseThrow(
                () -> new MyException("CUSTOMER IS NOT FOUND", ExceptionCode.TRANSACTION_HISTORY_SERVICE));
        this.movies = standsRepository.retrieveAllMoviesBoughtByCustomer(customer);
        this.predicates = createPredicates();
    }

    @Override
    public List<Predicate<Movie>> createPredicates() {
        List<Predicate<Movie>> predicates = new ArrayList<>();
        // filter by genre
        int genre = userService.getInt("Choose one genre which you are interested in." +
                "Any other value disable this filter:\n" +
                "1. DRAMA\n" +
                "2. COMEDY\n" +
                "3. SCI_FI\n" +
                "4. THRILLER");

        Predicate<Movie> genrePredicate = (m -> m.getGenre() == getMovieType(genre));
        predicates.add(genrePredicate);

        // filter by releaseDate
        Date fromReleaseDate = userService.getDate("Write date from...");
        Date toReleaseDate = userService.getDate("Write date to...");

        Predicate<Movie> releaseDatePredicate = (movie -> movie.getReleaseDate().after(fromReleaseDate) &&
                movie.getReleaseDate().before(toReleaseDate));
        predicates.add(releaseDatePredicate);

        // filter by duration
        int minDuration = userService.getIntWithValidator("Write the mininum duration in minutes:", d -> d > 0);
        int maxDuration = userService.getIntWithValidator("Write the maximum duration in minutes:", d -> d > minDuration);

        Predicate<Movie> durationPredicate = (movie -> movie.getDuration() > minDuration &&
                movie.getDuration() < maxDuration);
        predicates.add(durationPredicate);

        return predicates;
    }

    @Override
    public MovieType getMovieType(int option) {
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

    @Override
    public List<Movie> getFilteredMovieList() {




        return movies.stream()
                .filter(predicates.get(0))
                .filter(predicates.get(1))
                .filter(predicates.get(2))
                .collect(Collectors.toList());
    }

    public void showResults() {
        if (!movies.isEmpty()) {
            System.out.println("All movies which meet requirements of searching:");
            movies.forEach(System.out::println);
        } else {
            System.out.println("Unfortunately, in the history is any movie which meets your requirements");
        }
    }
}
