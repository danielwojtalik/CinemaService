package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class SalesStand {
    private Integer customerId;
    private Integer movieId;
    private LocalTime localTime;
    private BigDecimal discount;
}
