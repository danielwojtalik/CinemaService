package com.app.service.cinema_service;

import com.app.model.SalesStands;
import com.app.repository.CustomerRepository;
import com.app.repository.LoyaltyCardRepository;
import com.app.repository.MovieRepository;
import com.app.repository.SalesStandsRepository;
import com.app.repository.impl.CustomerRepositoryImpl;
import com.app.repository.impl.LoyaltyCardRepositoryImpl;
import com.app.repository.impl.MovieRepositoryImpl;
import com.app.repository.impl.SalesStandsRepositoryImpl;
import com.app.service.UserDataService;

import java.math.BigDecimal;

public interface SalesStandsService extends ItemService<SalesStands> {

    CustomerRepository customerRepository = new CustomerRepositoryImpl();
    MovieRepository movieRepository = new MovieRepositoryImpl();
    SalesStandsRepository salesStandsRepository = new SalesStandsRepositoryImpl();
    LoyaltyCardRepository loyaltyCardRepository = new LoyaltyCardRepositoryImpl();
    UserDataService dataService = UserDataService.getInstance();
    int boughtTicketsForDiscount = 5;
    BigDecimal discount = new BigDecimal("0.1");
}
