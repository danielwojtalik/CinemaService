package com.app.service.cinema_service;

import com.app.model.Movie;
import com.app.model.MovieType;
import com.app.service.UserDataService;

import java.util.List;
import java.util.function.Predicate;

public interface TransactionHistoryService {

    UserDataService userService = UserDataService.getInstance();

    List<Predicate<Movie>> createPredicates();

    List<Movie> getFilteredMovieList();

    MovieType getMovieType(int option);
}
