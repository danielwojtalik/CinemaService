package com.app.service.email_service;

import com.app.model.Customer;
import com.app.model.Movie;

import java.util.List;

public class TransactionHistoryEmail extends EmailService {

    private List<Movie> movies;

    public TransactionHistoryEmail(Customer customer, List<Movie> movies) {
        super(customer);
        this.movies = movies;
        this.emailContent = initializeEmailContent();
        this.emailTitle = initializeEmailTitle();

    }

    @Override
    protected String initializeEmailContent() {
        String movieList = movies.toString();
        return "The history of all your movies:\n " + movieList;
    }

    @Override
    protected String initializeEmailTitle() {
        return "Transaction History";
    }
}
