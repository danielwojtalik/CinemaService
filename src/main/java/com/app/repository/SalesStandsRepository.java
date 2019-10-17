package com.app.repository;

import com.app.model.Customer;
import com.app.model.Movie;
import com.app.model.SalesStand;
import com.app.service.TicketConfiguration;

import java.time.LocalDate;
import java.util.List;

public interface SalesStandsRepository extends CrudRepository<SalesStand> {

    List<SalesStand> findByStartDateTime();

    void addSaleStand(TicketConfiguration ticketConfiguration);

    int getTicketQuantityBoughtByCustomer(Customer customer);

    List<Movie> findAllMoviesForCustomer(Customer customer);

    List<Movie> findMoviesInConcreteTimeRange(LocalDate startTime, LocalDate finishTime);
}
