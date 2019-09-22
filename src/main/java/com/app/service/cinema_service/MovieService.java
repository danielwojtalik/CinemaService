package com.app.service.cinema_service;

import com.app.converters.MovieConverter;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.main.ApplicationConstants;
import com.app.model.Customer;
import com.app.model.Movie;
import com.app.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final SalesStandsService salesStandsService;

    public void addMovie(Movie movie) {
        movieRepository.add(movie);
        log.info("Movie have been added successfully");
    }

    public void deleteById(int id) {
        movieRepository.deleteByID(id);
    }

    public void updatePrice(int id, BigDecimal price) {
        Movie movieFromDb = movieRepository.findById(id).orElseThrow(
                () -> new MyException(String.format("THERE IS NO MOVIE WITH ID - %d", id), ExceptionCode.MOVIE_SERVICE));

        movieFromDb.setPrice(price.compareTo(BigDecimal.ZERO) <= 0 ? movieFromDb.getPrice() : price);
        movieRepository.update(movieFromDb);
    }

    public List<Movie> findByTitle(String title) {
        return movieRepository.findByTitle(title);
    }

    public List<Movie> findByGenre(String genre) {
        return movieRepository.findByGenre(genre);

    }

    public List<Movie> findByPrice(BigDecimal price, Boundary boundary) {
        return movieRepository.findByPrice(price, boundary);
    }

    public List<Movie> findByDuration(Integer duration, Boundary boundary) {
        return movieRepository.findByDuration(duration, boundary);
    }

    public List<Movie> findByReleaseDate(LocalDate date, Boundary boundary) {
        return movieRepository.findByReleaseDate(date, boundary);
    }

    public List<Movie> returnAllMovies() {
        return movieRepository.findAll();
    }

    public Movie retrieveMovieFromTitle(String title) {
        if (title == null) {
            throw new MyException("MOVIE TITLE IS NULL", ExceptionCode.MOVIE_SERVICE);
        }
        String fullPathToFile = new StringBuilder(ApplicationConstants.PATH_TO_RESOURCES_FOLDER)
                .append(title)
                .append(".json").toString();
        MovieConverter movieConverter = new MovieConverter(fullPathToFile);
        return movieConverter.fromJson().orElseThrow(() -> new MyException("CAN NOT FIND MOVIE IN RESOURCES", ExceptionCode.MAIN_MENU));
    }

    public List<Movie> retrieveAllMoviesForCustomerWithFilters(List<Predicate<Movie>> predicates, Customer customer) {
        List<Movie> moviesSeenByCustomer = salesStandsService.retrieveAllMovieBoughtByCustomer(customer);
        for (Predicate<Movie> pred : predicates) {
            moviesSeenByCustomer = moviesSeenByCustomer
                    .stream()
                    .filter(pred)
                    .collect(Collectors.toList());
        }
        return moviesSeenByCustomer;
    }

    public enum Boundary {
        LESS_THAN,
        MORE_THAN
    }
}
