package repository.impl;


import exceptions.ExceptionCode;
import exceptions.MyException;
import model.LoyaltyCard;
import repository.generic.AbstractCrudRepository;

import java.util.Comparator;
import java.util.Optional;

public class LoyaltyCardRepository extends AbstractCrudRepository<LoyaltyCard, Integer> {

    public Optional<LoyaltyCard> findLastLoyaltyCard() {
        Integer biggestId = findAll().stream().map(LoyaltyCard::getId).max(Comparator.naturalOrder()).orElseThrow(
                () -> new MyException("THERE IS NO BIGGEST ELEMENT", ExceptionCode.LOYALTY_CARD_REPOSITORY));
        return findById(biggestId);

    }
}
