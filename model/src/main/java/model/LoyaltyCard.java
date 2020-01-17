package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class LoyaltyCard {
    private Integer id;
    private LocalDate expirationDate;
    private BigDecimal discount;
    private Integer currentMoviesQuantity;  // ile filmow aktualnie jest kupionych
    private Integer moviesQuantity;         // na ile filmow jest karta

    // kiedy klient zakupil karte to ustalana jest mu pewna znizka
    // kiedy karta straci waznosc a klient wykupuje kolejne filmy
    // prosisz go o zalozenie nowej karty i na tej karcie dostaje znizke
    // o 1% wieksza niz mial poprzednio, mozemy zalozyc ze jak sie
    // karta skonczy bo przekroczy czas to wtedy juz nie proponujesz
    // nowej karty tylko ewentualnie karte na starych warunkach
}
