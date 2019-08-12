package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class LoyaltyCard {
    private int id;
    private Date expirationDate;
    private BigDecimal discount;
    private int moviesQuantity;
}
