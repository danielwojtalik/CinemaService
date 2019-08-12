package com.app.repository.impl;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Customer;
import com.app.model.Movie;
import com.app.model.SalesStands;
import com.app.repository.SalesStandsRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class SalesStandsRepositoryImpl implements SalesStandsRepository {
    @Override
    public List<SalesStands> findByStartDateTime() {
        return null;
    }

    @Override
    public void add(SalesStands salesStands) {
        if (salesStands == null) {
            throw new MyException("SALES STANDS IS NULL", ExceptionCode.REPOSITORY);
        }

        jdbi.useTransaction(handle -> handle.createUpdate("insert into "));
    }

    @Override
    public void update(SalesStands salesStands) {

    }

    @Override
    public Optional<SalesStands> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public List<SalesStands> findAll() {
        return null;
    }

    @Override
    public void deleteByID(Integer id) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void addCustomerWithTicket(Customer customer, Movie movie, Date startTime) {
        if (customer == null) {
            throw new MyException("CUSTOMER IS NULL", ExceptionCode.REPOSITORY);
        }
        if (movie == null) {
            throw new MyException("MOVIE IS NULL", ExceptionCode.REPOSITORY);
        }

        jdbi.useTransaction(handle -> handle.createUpdate("insert into sales_stands (customer_id, movie_id, start_date_time) " +
                "values (:customer_id, :movie_id, :start_date_time)")
                .bind("customer_id", customer.getId())
                .bind("movie_id", movie.getId())
                .bind("start_date_time", startTime)
                .execute()
        );
    }

    @Override
    public int getTicketQuantityBoughtByCustomer(Customer customer) {
        if (customer == null) {
            throw new MyException("CUSTOMER IS NULL", ExceptionCode.REPOSITORY);
        }

        return jdbi.withHandle(handle -> handle.createQuery("select * from sales_stands where customer_id = :customer_id")
                .bind("customer_id", customer.getId())
                .mapToBean(Customer.class)
                .list()
                .size()
        );
    }

    @Override
    public List<Movie> retrieveAllMoviesBoughtByCustomer(Customer customer) {
        return jdbi.withHandle(handle -> handle.createQuery("select m.title, m.genre, m.duration, m.release_date " +
                "from sales_stands s join movies m on m.id = s.movie_id where s.customer_id = :id")
                .bind("id", customer.getId())
                .mapToBean(Movie.class)
                .list()
        );
    }
}
