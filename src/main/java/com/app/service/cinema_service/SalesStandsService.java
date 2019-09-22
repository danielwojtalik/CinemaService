package com.app.service.cinema_service;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Customer;
import com.app.model.LoyaltyCard;
import com.app.model.Movie;
import com.app.model.SalesStand;
import com.app.repository.CustomerRepository;
import com.app.repository.LoyaltyCardRepository;
import com.app.repository.MovieRepository;
import com.app.repository.SalesStandsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Log4j
@RequiredArgsConstructor
public class SalesStandsService {

    private final CustomerRepository customerRepository;
    private final SalesStandsRepository salesStandsRepository;
    private final LoyaltyCardRepository loyaltyCardRepository;
    private final MovieRepository movieRepository;
    private static final String DATE_FORMAT = "HH:mm";
    private static final BigDecimal START_DISCOUNT = new BigDecimal("0.1");
    private final Integer MOVIES_LIMIT_FOR_CARD = 2;


    public Map<Integer, LocalTime> getAvailableTime() {
        LocalTime lastAvailableTime = LocalTime.now().withHour(22).withMinute(30).withSecond(0);
        LocalTime currentTime = LocalTime.now();
        LocalTime startTime = LocalTime.now();
        Map<Integer, LocalTime> availableStartTime = new LinkedHashMap<>();

        if (currentTime.getMinute() > 30) {
            startTime = startTime.plusHours(1).withMinute(0).withSecond(0);
        } else {
            startTime = startTime.withHour(currentTime.getHour()).withMinute(30).withSecond(0);
        }
        int option = 1;
        while (startTime.compareTo(lastAvailableTime) < 0) {
            availableStartTime.put(option, startTime);
            startTime = startTime.plusMinutes(30);
            option++;
        }
        return availableStartTime;
    }

    public Movie findMovie(Integer id) {
        return movieRepository.findById(id).orElseThrow(() -> new MyException("MOVIE DOES NOT EXIST",
                ExceptionCode.SALES_STAND_SERVICE));
    }

    public Customer findCustomer(Integer id) {
        return customerRepository.findById(id).orElseThrow(() -> new MyException("CUSTOMER DOES NOT EXIST"
                , ExceptionCode.SALES_STAND_SERVICE));
    }


    public static BigDecimal calculateFinalTicketPrice(Customer customer, Movie movie, BigDecimal discount) {
        return customer.getLoyaltyCardId() == null ? movie.getPrice() : movie.getPrice()
                .multiply(BigDecimal.ONE.subtract(discount));
    }

    public void offerFirstLoyaltyCard(Customer customer, boolean isWilling) {
        if (isWilling) {
            LocalDate expirationDate = LocalDate.now().plusYears(3);
            LoyaltyCard loyaltyCard = LoyaltyCard.builder()
                    .expirationDate(expirationDate)
                    .discount(START_DISCOUNT)
                    .moviesQuantity(MOVIES_LIMIT_FOR_CARD)
                    .build();

            loyaltyCardRepository.add(loyaltyCard);
            LoyaltyCard lastAddedLoyaltyCard = loyaltyCardRepository.findLastLoyaltyCard().orElseThrow(
                    () -> new MyException("LOYALTY CARD IS NULL", ExceptionCode.REPOSITORY)
            );
            customer.setLoyaltyCardId(lastAddedLoyaltyCard.getId());
            customerRepository.update(customer);
            log.info("SUCCESSFULLY ADD FIRST LOYALTY CARD");
        }
    }

    public void offerNewCardIfPossible(Customer customer, boolean isWilling) {
        LoyaltyCard loyaltyCard = loyaltyCardRepository.findById(customer.getLoyaltyCardId()).orElseThrow(
                () -> new MyException("LOYALTY CARD IS NULL", ExceptionCode.REPOSITORY)
        );
        if (isWilling) {
            BigDecimal currentDiscount = loyaltyCard.getDiscount();
            if (loyaltyCard.getExpirationDate().isAfter(LocalDate.now())) {
                currentDiscount = currentDiscount.add(new BigDecimal("0.01"));
            }
            LoyaltyCard newLoyaltyCard = LoyaltyCard.builder()
                    .discount(currentDiscount)
                    .expirationDate(LocalDate.now().plusYears(3))
                    .moviesQuantity(MOVIES_LIMIT_FOR_CARD)
                    .build();
            loyaltyCardRepository.add(newLoyaltyCard);
            LoyaltyCard loyaltyCardFromDB = loyaltyCardRepository.findLastLoyaltyCard().orElseThrow(
                    () -> new MyException("THERE IS ANY LOYALTY CARD IN DB", ExceptionCode.REPOSITORY)
            );
            customer.setLoyaltyCardId(loyaltyCardFromDB.getId());
            customerRepository.update(customer);
            log.info("SUCCESSFULLY ADD NEXT LOYALTY CARD");
        }
    }

    public void addCustomerWithTicket(Customer customer, Movie movie, LocalTime startTime) {
        if (customer == null) {
            throw new MyException("CUSTOMER IS NULL", ExceptionCode.SALES_STAND_SERVICE);
        }
        if (movie == null) {
            throw new MyException("MOVIE IS NULL", ExceptionCode.SALES_STAND_SERVICE);
        }
        if (startTime == null) {
            throw new MyException("START TIME IS NULL", ExceptionCode.SALES_STAND_SERVICE);
        }
        salesStandsRepository.addCustomerWithTicket(customer, movie, startTime);
        log.info("TICKET SUCCESSFULLY SELL");
    }

    public int getTotalTicketAmountBoughtByCustomer(Customer customer) {
        if (customer == null) {
            throw new MyException("CUSTOMER IS NULL", ExceptionCode.SALES_STAND_SERVICE);
        }
        return salesStandsRepository.getTicketQuantityBoughtByCustomer(customer);
    }

    public String prepareConfirmationMessage(Customer customer, Movie movie, LocalTime startTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        String messageContent = "Hello, %s %s!\nYou have already successfully bought ticket for movie: %s.\n" +
                "The movie starts today at: %s.\nThe ticket price is equal: %s zł.";
        BigDecimal finalPrice = calculateFinalTicketPrice(customer, movie, START_DISCOUNT);

        return String.format(messageContent, customer.getName(), customer.getSurname(), movie.getTitle(),
                formatter.format(startTime), finalPrice.setScale(2, RoundingMode.CEILING));
    }

    public List<Movie> retrieveAllMovieBoughtByCustomer(Customer customer) {
        return salesStandsRepository.findMovieByCustomerId(customer);
    }

    public List<SalesStand> retrieveAllSalesStands() {
        return salesStandsRepository.findAll();
    }
}
