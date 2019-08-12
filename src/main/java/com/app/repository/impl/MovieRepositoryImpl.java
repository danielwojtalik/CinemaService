package com.app.repository.impl;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Movie;
import com.app.repository.MovieRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MovieRepositoryImpl implements MovieRepository {

    @Override
    public void add(Movie movie) {
        if (movie == null) {
            throw new MyException("MOVIE IS NULL", ExceptionCode.REPOSITORY);
        }
        String movieTitle = movie.getTitle();
        if (findByTitle(movieTitle).size() != 0) {
            throw new MyException("MOVIE IS ALREADY EXISTS IN CINEMA PROGRAMME", ExceptionCode.REPOSITORY);
        }
        jdbi.withHandle(handle -> handle
                .createUpdate("insert into movies (title, genre, price, duration, release_date) " +
                        "values (:title, :genre, :price, :duration, :release_date)")
                .bind("title", movie.getTitle())
                .bind("genre", movie.getGenre())
                .bind("price", movie.getPrice())
                .bind("duration", movie.getDuration())
                .bind("release_date", movie.getReleaseDate())
                .execute()
        );
    }

    @Override
    public void update(Movie movie) {
        Movie movieFromDb = findById(movie.getId()).orElseThrow(() -> new MyException("NO MOVIE WITH ID", ExceptionCode.REPOSITORY));
        jdbi.useTransaction(handle -> handle.createUpdate("update movies set title = :title, " +
                "genre =:genre, price = :price, duration = :duration, release_date = :release_date where id = :id")
                .bind("id", movie.getId())
                .bind("title", movie.getGenre() != null ? movie.getTitle() : movieFromDb.getTitle())
                .bind("genre", movie.getGenre() != null ? movie.getGenre() : movieFromDb.getGenre())
                .bind("price", movie.getPrice() != null ? movie.getPrice() : movieFromDb.getPrice())
                .bind("duration", movie.getDuration() != null ? movie.getDuration() : movieFromDb.getDuration())
                .bind("release_date", movie.getReleaseDate() != null ? movie.getReleaseDate() : movieFromDb.getReleaseDate())
                .execute());
    }

    @Override
    public Optional<Movie> findById(Integer id) {
        return jdbi.withHandle(handle -> handle.createQuery("select * from movies where id = :id")
                .bind("id", id)
                .mapToBean(Movie.class)
                .findFirst());
    }

    @Override
    public List<Movie> findAll() {
        return jdbi.withHandle(handle -> handle.createQuery("select * from movies")
                .mapToBean(Movie.class)
                .list());
    }

    @Override
    public void deleteByID(Integer id) {
        jdbi.useTransaction(handle -> handle.createUpdate("delete from movies where id = :id")
                .bind("id", id)
                .execute());

    }

    @Override
    public void deleteAll() {
        jdbi.useTransaction(handle -> handle.createUpdate("delete * from movies")
                .execute());

    }

    @Override
    public List<Movie> findByTitle(String title) {
        return jdbi.withHandle(handle -> handle.createQuery("select * from movies where title = :title")
                .bind("title", title)
                .mapToBean(Movie.class)
                .list());
    }

    @Override
    public List<Movie> findByGenre(String genre) {
        return jdbi.withHandle(handle -> handle.createQuery("select * from movies where genre = :genre")
                .bind("genre", genre)
                .mapToBean(Movie.class)
                .list());
    }

    @Override
    public List<Movie> findByPrice(BigDecimal price, boolean isPriceFrom) {
        String sql = "select * from movies where price %s :price";
        return jdbi.withHandle(handle -> handle.createQuery(String.format(sql, isPriceFrom ? ">" : "<"))
                .bind("price", price)
                .mapToBean(Movie.class)
                .list());
    }

    @Override
    public List<Movie> findByDuration(int duration, boolean isDurationFrom) {
        String sql = "select * from movies where duration %s :duration";
        return jdbi.withHandle(handle -> handle.createQuery(String.format(sql, isDurationFrom ? ">" : "<"))
                .bind("duration", duration)
                .mapToBean(Movie.class)
                .list());
    }

    @Override
    public List<Movie> findByReleaseDate(Date releaseDate, boolean isReleaseDateFrom) {
        String sql = "select * from movies where release_date %s :release_date";
        return jdbi.withHandle(handle -> handle.createQuery(String.format(sql, isReleaseDateFrom ? ">" : "<"))
                .bind("release_date", releaseDate)
                .mapToBean(Movie.class)
                .list());
    }
}
