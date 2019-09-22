package com.app.service.cinema_service;


import com.app.model.Customer;
import com.app.model.Movie;
import com.app.model.SalesStand;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j
@RequiredArgsConstructor
public class StatisticsService {

    private final SalesStandsService salesStandsService;

    public final Map<Movie, List<Customer>> retrieveMoviesWithCustomers() {
        List<SalesStand> salesStands = salesStandsService.retrieveAllSalesStands();

        Map<Movie, List<Customer>> movieWithCustomers = new HashMap<>();

        for (SalesStand salesStand : salesStands) {

            Movie movie = salesStandsService.findMovie(salesStand.getMovieId());
            Customer customer = salesStandsService.findCustomer(salesStand.getCustomerId());
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

}
