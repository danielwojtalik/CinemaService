package com.app.service.cinema_service.impl;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Customer;
import com.app.model.LoyaltyCard;
import com.app.model.Movie;
import com.app.service.email_service.ConfirmationEmail;
import com.app.service.email_service.EmailService;
import com.app.service.cinema_service.ItemService;
import com.app.service.menu_services.SearchCriteria;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.app.service.cinema_service.SalesStandsService.*;

public class SalesStandsServiceImpl implements ItemService<SalesStandsServiceImpl> {
    private Customer customer;
    private Movie movie;


    @Override
    public void deleteById() {

    }

    @Override
    public void showAll() {

    }

    @Override
    public void showFiltered() {

    }

    @Override
    public void update() {

    }

    @Override
    public SearchCriteria getSearchCriterion() {
        return null;
    }

    @Override
    public List<SalesStandsServiceImpl> getSearchResult() {
        return null;
    }

    public void sellTicket() {
        // CUSTOMER
        customer = customerRepository.findCustomerByPersonalDateFromUser().orElseThrow(
                () -> new MyException("CUSTOMER IS NOT FOUND", ExceptionCode.SALES_STAND_SERVICE));
        //MOVIE
        findMovie();
        // START TIME
        Date startTime = getAvailableTime();
        salesStandsRepository.addCustomerWithTicket(customer, movie, startTime);
        // LOYALTY CARD OFFER
        proposeLoyaltyCard();
        // CALCULATE FINAL PRICE
        BigDecimal finalPrice = calculateFinalTicketPrice();
        // SEND EMAIL
        sendEmailToCustomer(startTime);

    }

    private Date getAvailableTime() {
        Date firstAvailableTime = new Date();
        Date lastAvailableTime = new Date();
        Date currentTime = new Date();
        Date startTime = new Date();
        Map<Integer, Date> availableStartTime = new LinkedHashMap<>();

        firstAvailableTime.setHours(6);
        firstAvailableTime.setMinutes(0);
        lastAvailableTime.setHours(22);
        lastAvailableTime.setMinutes(30);

        if (currentTime.getMinutes() > 30) {
            startTime.setHours(currentTime.getHours() + 1);
            startTime.setMinutes(0);
            startTime.setSeconds(0);
        } else {
            startTime.setHours(currentTime.getHours());
            startTime.setMinutes(30);
            startTime.setSeconds(0);
        }
        int option = 1;
        while (startTime.compareTo(lastAvailableTime) < 0) {
            availableStartTime.put(option, startTime);
            startTime = DateUtils.addMinutes(startTime, 30);
            option++;
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        availableStartTime.forEach((k, v) -> System.out.println("option: (" + k + ") ---> " + timeFormat.format(v)));
        int choseOption = dataService.getIntWithValidator("Choose time for start movie",
                op -> op > 0 && op < availableStartTime.size() + 1);

        return availableStartTime.get(choseOption);
    }

    private void findMovie() {
        MovieServiceImpl movieServiceImpl = new MovieServiceImpl();
        movieServiceImpl.showAll();
        int id = dataService.getIntWithValidator("Write movie id", idd -> idd > 0);

        movie =  movieRepository.findById(id).orElseThrow(() -> new MyException("MOVIE DOES NOT EXIST",
                ExceptionCode.SALES_STAND_SERVICE));
    }

    private void proposeLoyaltyCard() {
        int ticketsBoughtByCustomer = salesStandsRepository.getTicketQuantityBoughtByCustomer(customer);
        if (boughtTicketsForDiscount <= ticketsBoughtByCustomer && customer.getLoyaltyCardId() == null) {
            int option = dataService.getIntWithValidator("You have bought " + boughtTicketsForDiscount +
                    " times in hour cinema. Do you like get loyalty card?\n 1. YES\n 2. NO", op -> op > 0 && op < 3);
            if (option == 1) {
                Date expirationDate = new Date();
                expirationDate.setYear(expirationDate.getYear() + 3);

                LoyaltyCard loyaltyCard = LoyaltyCard.builder()
                        .expirationDate(expirationDate)
                        .discount(discount)
                        .moviesQuantity(ticketsBoughtByCustomer)
                        .build();

                loyaltyCardRepository.add(loyaltyCard);
                LoyaltyCard lastAddedLoyaltyCard = loyaltyCardRepository.findLastLoyaltyCard()
                        .orElseThrow(() -> new MyException("LOYALTY CARD IS NULL", ExceptionCode.SALES_STAND_SERVICE));
                customer.setLoyaltyCardId(lastAddedLoyaltyCard.getId());
                customerRepository.update(customer);
            }
        }
    }

    private BigDecimal calculateFinalTicketPrice() {
        return customer.getLoyaltyCardId() == null ? movie.getPrice() : movie.getPrice()
                .multiply(BigDecimal.ONE.subtract(discount));
    }

    private void sendEmailToCustomer(Date startTime) {
        EmailService emailService = new ConfirmationEmail(customer, movie, startTime);
        emailService.sendEmail();
    }
}
