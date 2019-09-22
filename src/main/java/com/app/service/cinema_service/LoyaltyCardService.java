package com.app.service.cinema_service;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Customer;
import com.app.model.LoyaltyCard;
import com.app.repository.LoyaltyCardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequiredArgsConstructor
public class LoyaltyCardService {
    private final LoyaltyCardRepository loyaltyCardRepository;

    public LoyaltyCard findLoyaltyCardById(Integer id) {
        if (id == null) {
            throw new MyException("ID IS NULL", ExceptionCode.LOYALTY_CARD_SERVICE);
        }
        return loyaltyCardRepository.findById(id).orElseThrow(() -> new MyException("LOYALTY CARD IS NOT IN DB", ExceptionCode.LOYALTY_CARD_SERVICE));
    }

    public void incrementMoviesAmount(Customer customer) {
        if (customer == null) {
            throw new MyException("CUSTOMER IS NULL", ExceptionCode.SALES_STAND_SERVICE);
        }
        LoyaltyCard loyaltyCard = loyaltyCardRepository.findById(customer.getLoyaltyCardId()).orElseThrow(
                () -> new MyException("LOYALTY CARD IS NOT IN DB FOR THAT CUSTOMER", ExceptionCode.SALES_STAND_SERVICE));
        loyaltyCard.setCurrentMoviesQuantity(loyaltyCard.getCurrentMoviesQuantity() + 1);
        loyaltyCardRepository.update(loyaltyCard);
    }
}
