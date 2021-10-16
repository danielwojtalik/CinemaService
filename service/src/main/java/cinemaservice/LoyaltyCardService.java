package cinemaservice;

import exceptions.ExceptionCode;
import exceptions.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import model.Customer;
import model.LoyaltyCard;
import repository.impl.LoyaltyCardRepository;

@Log4j
@RequiredArgsConstructor
public class LoyaltyCardService {
    private final LoyaltyCardRepository loyaltyCardRepository;
    private final SalesStandsService salesStandsService;

    public LoyaltyCard findLoyaltyCardById(Integer id) {
        if (id == null) {
            throw new CustomException("ID IS NULL", ExceptionCode.LOYALTY_CARD_SERVICE);
        }
        return loyaltyCardRepository.findById(id).orElseThrow(() -> new CustomException("LOYALTY CARD IS NOT IN DB", ExceptionCode.LOYALTY_CARD_SERVICE));
    }

    public void updateMoviesQuantity(Customer customer) {
        if (customer == null) {
            throw new CustomException("CUSTOMER IS NULL", ExceptionCode.SALES_STAND_SERVICE);
        }
        LoyaltyCard loyaltyCard = loyaltyCardRepository.findById(customer.getLoyaltyCardId()).orElseThrow(
                () -> new CustomException("LOYALTY CARD IS NOT IN DB FOR THAT CUSTOMER", ExceptionCode.SALES_STAND_SERVICE));
        loyaltyCard.setCurrentMoviesQuantity(salesStandsService.retrieveAllMoviesBoughtByCustomer(customer).size());
        loyaltyCardRepository.update(loyaltyCard);
    }
}
