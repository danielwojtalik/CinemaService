package com.app.repository;

import com.app.model.Movie;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface MovieRepository extends CrudRepository<Movie> {

    List<Movie> findByTitle(String title);

    List<Movie> findByGenre(String genre);

    List<Movie> findByPrice(BigDecimal price, boolean isPriceFrom);

    List<Movie> findByDuration(int duration, boolean isDurationFrom);

    List<Movie> findByReleaseDate(Date releaseDate, boolean isReleaseDateFrom);

}
