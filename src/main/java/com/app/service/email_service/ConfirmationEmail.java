package com.app.service.email_service;

import com.app.model.Customer;
import com.app.model.Movie;

import java.text.SimpleDateFormat;
import java.time.LocalTime;

public class ConfirmationEmail extends EmailService {

    private final static String MESSAGE_CONTENT = "Hello, %s %s!\nYou have already successfully bought ticket for movie: %s.\n" +
            "The movie starts at: %s.\nThe ticket price is equal: %s zł.";
    private Movie movie;
    private LocalTime startTime;


    public ConfirmationEmail(Customer customer, Movie movie, LocalTime startTime) {
        super(customer);
        this.movie = movie;
        this.startTime = startTime;
    }

    @Override
    protected String initializeEmailContent() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm - dd/MM");
        return String.format(MESSAGE_CONTENT, customer.getName(), customer.getSurname(), movie.getTitle(),
                simpleDateFormat.format(startTime), movie.getPrice());
    }

    @Override
    protected String initializeEmailTitle() {
        return "Confirmation";
    }
}
