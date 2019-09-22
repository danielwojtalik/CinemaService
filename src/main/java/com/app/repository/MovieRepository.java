package com.app.repository;

import com.app.model.Movie;
import com.app.service.cinema_service.MovieService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface MovieRepository extends CrudRepository<Movie> {

    List<Movie> findByTitle(String title);

    List<Movie> findByGenre(String genre);

    List<Movie> findByPrice(BigDecimal price, MovieService.Boundary boundary);

    List<Movie> findByDuration(int duration, MovieService.Boundary boundary);

    List<Movie> findByReleaseDate(LocalDate releaseDate, MovieService.Boundary boundary);

}
