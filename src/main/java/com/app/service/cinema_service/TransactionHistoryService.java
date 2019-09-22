package com.app.service.cinema_service;

import com.app.model.Customer;
import com.app.model.Movie;
import com.app.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.util.List;

@Log4j
@RequiredArgsConstructor
public class TransactionHistoryService {
    private final MovieRepository movieRepository;


    public String prepareContentOfTransactionHistory(List<Movie> movies, Customer customer) {
        String mainContent = new StringBuilder("Hello %s. \nYou have already " +
                "generate transaction history of yours movies. There is full list of filtered movies.\n")
                .append(movies.toString()).toString();
        return String.format(mainContent, customer.getName());
    }
}
