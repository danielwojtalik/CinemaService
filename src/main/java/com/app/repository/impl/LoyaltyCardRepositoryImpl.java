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
                "discount, movies_quantity, current_movies_quantity) values (:expiration_date, :discount, :movies_quantity," +
                " :current_movies_quantity)")
                .bind("expiration_date", loyaltyCard.getExpirationDate())
                .bind("discount", loyaltyCard.getDiscount())
                .bind("movies_quantity", loyaltyCard.getMoviesQuantity())
                .bind("current_movies_quantity", loyaltyCard.getCurrentMoviesQuantity())
                .execute());
    }

    @Override
    public void update(LoyaltyCard loyaltyCard) {
        if (loyaltyCard == null) {
            throw new MyException("LOYALTY CARD IS NULL", ExceptionCode.REPOSITORY);
        }
        LoyaltyCard loyaltyCardFromDB = findById(loyaltyCard.getId()).orElseThrow((
                () -> new MyException("LOYALTY CARD IS NOT EXIST IN DB", ExceptionCode.REPOSITORY)));

        jdbi.withHandle(handle -> handle.createUpdate("update loyalty_cards set expiration_date = :expiration_date, " +
                "discount = :discount, movies_quantity = :movies_quantity, current_movies_quantity = :current_movies_quantity " +
                "where id = :id")
                .bind("expiration_date", loyaltyCard.getExpirationDate() == null
                        ? loyaltyCardFromDB.getExpirationDate()
                        : loyaltyCard.getExpirationDate())
                .bind("discount", loyaltyCard.getDiscount() == null
                        ? loyaltyCardFromDB.getDiscount()
                        : loyaltyCard.getDiscount())
                .bind("movies_quantity", loyaltyCard.getMoviesQuantity() == null
                        ? loyaltyCardFromDB.getMoviesQuantity()
                        : loyaltyCard.getMoviesQuantity())
                .bind("current_movies_quantity", loyaltyCard.getCurrentMoviesQuantity() == null
                        ? loyaltyCardFromDB.getCurrentMoviesQuantity()
                        : loyaltyCard.getCurrentMoviesQuantity())
                .bind("id", loyaltyCard.getId())
                .execute()
        );

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
        Integer biggestId = findAll().stream().map(LoyaltyCard::getId).max(Comparator.naturalOrder()).orElseThrow(
                () -> new MyException("THERE IS NO BIGGEST ELEMENT", ExceptionCode.LOYALTY_CARD_REPOSITORY));
        return findById(biggestId);


    }
}
