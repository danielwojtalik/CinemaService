package com.app.service.cinema_service;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
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
        String moviesFormattedList = formatMovieList(movies);
        String mainContent = new StringBuilder("Hello %s. \nYou have already " +
                "generate transaction history of yours movies. There is full list of filtered movies.\n")
                .append(moviesFormattedList).toString();
        return String.format(mainContent, customer.getName());
    }

    private String formatMovieList(List<Movie> movies) {
        if (movies == null) {
            throw new MyException("MOVIE LIST IS NULL", ExceptionCode.TRANSACTION_HISTORY_SERVICE);
        }
        StringBuilder formattedMovies = new StringBuilder();
        if (!movies.isEmpty()) {
            int idx = 1;
            for (Movie m : movies) {
                formattedMovies.append(idx + ". Title: " + m.getTitle() + ", Price: " + m.getPrice()).append("\n");
                idx++;
            }
        }
        return formattedMovies.toString();
    }

}
