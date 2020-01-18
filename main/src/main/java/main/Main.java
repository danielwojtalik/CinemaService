package main;


import menu.MainMenu;
import org.apache.log4j.BasicConfigurator;
import repository.impl.CustomerRepository;
import repository.impl.LoyaltyCardRepository;
import repository.impl.MovieRepository;
import repository.impl.SalesStandsRepository;
import cinema_service.*;

public class Main {

    public static void main(String[] args) {
        MainMenu mainMenu = initializeApp();
        mainMenu.runApplication();
    }

    private static MainMenu initializeApp() {
        BasicConfigurator.configure();
        MovieRepository movieRepository = new MovieRepository();
        SalesStandsRepository salesStandsRepository = new SalesStandsRepository();
        CustomerRepository customerRepository = new CustomerRepository();
        LoyaltyCardRepository loyaltyCardRepository = new LoyaltyCardRepository();
        CustomerService customerService = new CustomerService(customerRepository);
        SalesStandsService salesStandsService = new SalesStandsService(customerRepository, salesStandsRepository, loyaltyCardRepository, movieRepository);
        MovieService movieService = new MovieService(movieRepository, salesStandsService);
        LoyaltyCardService loyaltyCardService = new LoyaltyCardService(loyaltyCardRepository,salesStandsService);
        TransactionHistoryService transactionHistoryService = new TransactionHistoryService(movieRepository);
        StatisticsService statisticsService = new StatisticsService(salesStandsService, customerService, movieService);
        return new MainMenu(customerService, movieService, salesStandsService, loyaltyCardService, transactionHistoryService, statisticsService);
    }

}
