package com.app.service;

import com.app.model.Customer;
import com.app.model.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketConfiguration {
    private Customer customer;
    private Movie movie;
    private LocalTime startTime;
    private BigDecimal priceWithDiscount;
}
