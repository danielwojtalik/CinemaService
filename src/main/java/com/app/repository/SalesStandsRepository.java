package com.app.repository;

import com.app.model.Customer;
import com.app.model.Movie;
import com.app.model.SalesStands;

import java.util.Date;
import java.util.List;

public interface SalesStandsRepository extends CrudRepository<SalesStands> {

    List<SalesStands> findByStartDateTime();

    void addCustomerWithTicket(Customer customer, Movie movie, Date startTime);

    int getTicketQuantityBoughtByCustomer(Customer customer);

    List<Movie> retrieveAllMoviesBoughtByCustomer(Customer customer);
}
