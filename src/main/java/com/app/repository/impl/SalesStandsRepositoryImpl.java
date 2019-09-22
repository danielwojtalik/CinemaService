package com.app.repository.impl;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Customer;
import com.app.model.Movie;
import com.app.model.SalesStand;
import com.app.repository.SalesStandsRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class SalesStandsRepositoryImpl implements SalesStandsRepository {
    @Override
    public List<SalesStand> findByStartDateTime() {
        return null;
    }

    @Override
    public void add(SalesStand salesStand) {
        if (salesStand == null) {
            throw new MyException("SALES STANDS IS NULL", ExceptionCode.REPOSITORY);
        }

        jdbi.useTransaction(handle -> handle.createUpdate("insert into "));
    }

    @Override
    public void update(SalesStand salesStand) {

    }

    @Override
    public Optional<SalesStand> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public List<SalesStand> findAll() {
        return jdbi.withHandle(handle -> handle.createQuery("select * from sales_stands")
                .mapToBean(SalesStand.class)
                .list());
    }

    @Override
    public void deleteByID(Integer id) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void addCustomerWithTicket(Customer customer, Movie movie, LocalTime startTime) {
        if (customer == null) {
            throw new MyException("CUSTOMER IS NULL", ExceptionCode.REPOSITORY);
        }
        if (movie == null) {
            throw new MyException("MOVIE IS NULL", ExceptionCode.REPOSITORY);
        }
        if (startTime == null) {
            throw new MyException("START TIME IS NULL", ExceptionCode.REPOSITORY);
        }

        LocalDateTime startTimeWithDate = LocalDateTime.now().with(startTime);
        jdbi.useTransaction(handle -> handle.createUpdate("insert into sales_stands (customer_id, movie_id, start_date_time) " +
                "values (:customer_id, :movie_id, :start_date_time)")
                .bind("customer_id", customer.getId())
                .bind("movie_id", movie.getId())
                .bind("start_date_time", (startTimeWithDate))
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
    public List<Movie> findMovieByCustomerId(Customer customer) {
        if (customer == null) {
            throw new MyException("CUSTOMER IS NULL", ExceptionCode.SALES_STAND_SERVICE);
        }
        return jdbi.withHandle(handle -> handle.createQuery("select m.* " +
                "from sales_stands s join movies m on m.id = s.movie_id where s.customer_id = :id")
                .bind("id", customer.getId())
                .mapToBean(Movie.class)
                .list()
        );

    }
}
