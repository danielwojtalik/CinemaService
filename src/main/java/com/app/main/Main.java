package com.app.main;

import com.app.menu.MainMenu;
import com.app.repository.CustomerRepository;
import com.app.repository.LoyaltyCardRepository;
import com.app.repository.MovieRepository;
import com.app.repository.SalesStandsRepository;
import com.app.repository.impl.CustomerRepositoryImpl;
import com.app.repository.impl.LoyaltyCardRepositoryImpl;
import com.app.repository.impl.MovieRepositoryImpl;
import com.app.repository.impl.SalesStandsRepositoryImpl;
import com.app.service.cinema_service.*;
import org.apache.log4j.BasicConfigurator;

public class Main {

    public static void main(String[] args) {
        MainMenu mainMenu = initializeApp();
        mainMenu.runApplication();
    }

    private static MainMenu initializeApp() {
        BasicConfigurator.configure();
        MovieRepository movieRepository = new MovieRepositoryImpl();
        SalesStandsRepository salesStandsRepository = new SalesStandsRepositoryImpl();
        CustomerRepository customerRepository = new CustomerRepositoryImpl();
        LoyaltyCardRepository loyaltyCardRepository = new LoyaltyCardRepositoryImpl();
        CustomerService customerService = new CustomerService(customerRepository);
        SalesStandsService salesStandsService = new SalesStandsService(customerRepository, salesStandsRepository, loyaltyCardRepository, movieRepository);
        MovieService movieService = new MovieService(movieRepository, salesStandsService);
        LoyaltyCardService loyaltyCardService = new LoyaltyCardService(loyaltyCardRepository);
        TransactionHistoryService transactionHistoryService = new TransactionHistoryService(movieRepository);
        StatisticsService statisticsService = new StatisticsService(salesStandsService);
        return new MainMenu(customerService, movieService, salesStandsService, loyaltyCardService, transactionHistoryService, statisticsService);
    }

}
