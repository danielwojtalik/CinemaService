package com.app.service.cinema_service;


import com.app.model.Customer;
import com.app.model.Movie;
import com.app.model.MovieType;
import com.app.model.SalesStand;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j
@RequiredArgsConstructor
public class StatisticsService {

    private final SalesStandsService salesStandsService;
    private final CustomerService customerService;
    private final MovieService movieService;

    public final Map<Movie, List<Customer>> retrieveMovieWithCustomers() {
        List<SalesStand> salesStands = salesStandsService.retrieveAllSalesStands();

        Map<Movie, List<Customer>> movieWithCustomers = new HashMap<>();

        for (SalesStand salesStand : salesStands) {

            Movie movie = movieService.findMovieById(salesStand.getMovieId());
            Customer customer = customerService.findCustomerById(salesStand.getCustomerId());
            List<Customer> customers = new ArrayList<>();
            if (!movieWithCustomers.containsKey(movie)) {
                customers.add(customer);
                movieWithCustomers.put(movie, customers);
            } else {
                customers = movieWithCustomers.get(movie);
                customers.add(customer);
                movieWithCustomers.put(movie, customers);
            }
        }
        return movieWithCustomers;
    }

    public Map<Customer, List<Movie>> retrieveCustomerWithMovies() {
        final Map<Customer, List<Movie>> customerWithMovies = new HashMap<>();
        final List<Customer> allCustomers = customerService.findAll();

        allCustomers.forEach(customer -> {
            List<Movie> moviesBoughtByCustomer = salesStandsService.retrieveAllMoviesBoughtByCustomer(customer);
            customerWithMovies.put(customer, moviesBoughtByCustomer);
        });
        return customerWithMovies;
    }

    public Map<MovieType, Customer> retrieveBestCustomerForCategory() {
        final EnumSet<MovieType> moviesType = EnumSet.allOf(MovieType.class);
        final Map<Customer, List<Movie>> customerWithMovie = retrieveCustomerWithMovies();
        final Map<MovieType, Customer> bestCustomerForMovieType = new HashMap<>();

        for (MovieType movieType : moviesType) {
            Customer bestCustomer = customerWithMovie.entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue()
                                    .stream()
                                    .filter(movie -> movie.getGenre() == movieType)
                                    .collect(Collectors.toList())
                    ))
                    .entrySet()
                    .stream()
                    .sorted((e1, e2) -> (e2.getValue()).stream().map(Movie::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add)
                            .compareTo(e1.getValue().stream().map(Movie::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add)))
                    .filter(entry -> !entry.getValue().isEmpty())
                    .map(Map.Entry::getKey)
                    .findFirst().orElseGet(() -> Customer.builder()
                            .id(0)
                            .name("NONE")
                            .surname("NONE")
                            .build());
            bestCustomerForMovieType.put(movieType, bestCustomer);
        }
        return bestCustomerForMovieType;
    }

    public Map<MovieType, Long> retrieveAllTicketSalesInEachCategoryInTimeRange(LocalDate startTime, LocalDate finishTime) {
        List<Movie> allMovies = salesStandsService.findAllMoviesInTimeRange(startTime, finishTime);
        return allMovies.stream()
                .map(Movie::getGenre)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public Map<Customer, Map<Movie, Long>> retrieveNumberOfEachMoviesForClient() {
        Map<Customer, List<Movie>> customerWithMovies = retrieveCustomerWithMovies();
        return customerWithMovies
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue()
                                .stream()
                                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                        )
                );
    }

    public BigDecimal retrieveTotalTicketPriceSoldInCinema() {
        return salesStandsService.findAllSaleStands().stream()
                .map(SalesStand::getPriceWithDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
