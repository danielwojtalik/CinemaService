package com.app.repository;

import com.app.model.LoyaltyCard;

import java.util.Optional;

public interface LoyaltyCardRepository extends CrudRepository<LoyaltyCard> {

    Optional<LoyaltyCard> findLastLoyaltyCard();
}
