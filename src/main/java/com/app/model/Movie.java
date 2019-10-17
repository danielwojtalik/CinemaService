package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Movie {

    private Integer id;
    private String title;
    private MovieType genre;
    private BigDecimal price;
    private Integer duration;
    private LocalDate releaseDate;
    // https://stackoverflow.com/questions/54666536/date-one-day-backwards-after-select-from-mysql-db
    // private ZonedDateTime releaseDate;

}
