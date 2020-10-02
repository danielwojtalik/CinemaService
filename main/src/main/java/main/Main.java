package main;


import cinema_service.*;
import menu.Application;
import org.apache.log4j.BasicConfigurator;
import repository.impl.CustomerRepository;
import repository.impl.LoyaltyCardRepository;
import repository.impl.MovieRepository;
import repository.impl.SalesStandsRepository;

public class Main {

    public static void main(String[] args) {
        Application application = initializeApp();
        application.run();
    }

    private static Application initializeApp() {
        BasicConfigurator.configure();
        MovieRepository movieRepository = new MovieRepository();
        SalesStandsRepository salesStandsRepository = new SalesStandsRepository();
        CustomerRepository customerRepository = new CustomerRepository();
        LoyaltyCardRepository loyaltyCardRepository = new LoyaltyCardRepository();
        CustomerService customerService = new CustomerService(customerRepository);
        SalesStandsService salesStandsService = new SalesStandsService(customerRepository, salesStandsRepository, loyaltyCardRepository, movieRepository);
        MovieService movieService = new MovieService(movieRepository, salesStandsService);
        LoyaltyCardService loyaltyCardService = new LoyaltyCardService(loyaltyCardRepository,salesStandsService);
        StatisticsService statisticsService = new StatisticsService(salesStandsService, customerService, movieService);

        return new Application(customerService, movieService, salesStandsService, loyaltyCardService, statisticsService);
    }

}
