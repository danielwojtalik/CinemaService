package repository.impl;


import lombok.extern.log4j.Log4j;
import model.Boundary;
import model.Movie;
import repository.AbstractCrudRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Log4j
public class MovieRepository extends AbstractCrudRepository<Movie, Integer> {

    public List<Movie> findByTitle(String title) {
        return jdbi.withHandle(handle -> handle.createQuery("select * from movies where title = :title")
                .bind("title", title)
                .mapToBean(Movie.class)
                .list());
    }

    public List<Movie> findByGenre(String genre) {
        return jdbi.withHandle(handle -> handle.createQuery("select * from movies where genre = :genre")
                .bind("genre", genre)
                .mapToBean(Movie.class)
                .list());
    }

    public List<Movie> findByPrice(BigDecimal price, Boundary boundary) {
        String sql = "select * from movies where price %s :price";
        return jdbi.withHandle(handle -> handle.createQuery(String.format(sql, boundary == Boundary.LESS_THAN ? "<" : ">"))
                .bind("price", price)
                .mapToBean(Movie.class)
                .list());
    }

    public List<Movie> findByDuration(int duration, Boundary boundary) {
        String sql = "select * from movies where duration %s :duration";
        return jdbi.withHandle(handle -> handle.createQuery(String.format(sql, boundary == Boundary.LESS_THAN ? "<" : ">"))
                .bind("duration", duration)
                .mapToBean(Movie.class)
                .list());
    }

    public List<Movie> findByReleaseDate(LocalDate releaseDate, Boundary boundary) {
        String sql = "select * from movies where release_date %s :release_date";
        return jdbi.withHandle(handle -> handle.createQuery(String.format(sql,boundary == Boundary.LESS_THAN ? "<" : ">"))
                .bind("release_date", releaseDate)
                .mapToBean(Movie.class)
                .list());
    }

}
