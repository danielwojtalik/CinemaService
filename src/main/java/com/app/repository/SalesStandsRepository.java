package com.app.repository;

import com.app.model.Customer;
import com.app.model.Movie;
import com.app.model.SalesStand;

import java.time.LocalTime;
import java.util.List;

public interface SalesStandsRepository extends CrudRepository<SalesStand> {

    List<SalesStand> findByStartDateTime();

    void addCustomerWithTicket(Customer customer, Movie movie, LocalTime startTime);

    int getTicketQuantityBoughtByCustomer(Customer customer);

    List<Movie> findMovieByCustomerId(Customer customer);
}
