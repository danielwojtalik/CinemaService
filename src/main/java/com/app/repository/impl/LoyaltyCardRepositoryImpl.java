package com.app.repository.impl;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.LoyaltyCard;
import com.app.repository.LoyaltyCardRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class LoyaltyCardRepositoryImpl implements LoyaltyCardRepository {
    @Override
    public void add(LoyaltyCard loyaltyCard) {
        if (loyaltyCard == null) {
            throw new MyException("LOYALTY CARD IS NULL", ExceptionCode.REPOSITORY);
        }
        jdbi.useTransaction(handle -> handle.createUpdate("insert into loyalty_cards (expiration_date, " +
                "discount, movies_quantity) values (:expiration_date, :discount, :movies_quantity)")
                .bind("expiration_date", loyaltyCard.getExpirationDate())
                .bind("discount", loyaltyCard.getDiscount())
                .bind("movies_quantity", loyaltyCard.getMoviesQuantity())
                .execute());
    }

    @Override
    public void update(LoyaltyCard loyaltyCard) {

    }

    @Override
    public Optional<LoyaltyCard> findById(Integer id) {
        if (id == null) {
            throw new MyException("ID IS NULL", ExceptionCode.REPOSITORY);
        }
        return jdbi.withHandle(handle -> handle.createQuery("select * from loyalty_cards where id = :id")
                .bind("id", id)
                .mapToBean(LoyaltyCard.class)
                .findFirst()
        );
    }

    @Override
    public List<LoyaltyCard> findAll() {
        return jdbi.withHandle(handle -> handle.createQuery("select * from loyalty_cards")
                .mapToBean(LoyaltyCard.class)
                .list()
        );
    }

    @Override
    public void deleteByID(Integer id) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Optional<LoyaltyCard> findLastLoyaltyCard() {
        Integer biggestId = findAll().stream().map(lc -> lc.getId()).max(Comparator.naturalOrder()).orElseThrow(
                () -> new MyException("THERE IS NO BIGGEST ELEMENT", ExceptionCode.LOYALTY_CARD_REPOSITORY));
        return findById(biggestId);


    }
}
