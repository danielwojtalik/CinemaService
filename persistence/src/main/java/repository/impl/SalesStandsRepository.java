package repository.impl;


import exceptions.ExceptionCode;
import exceptions.CustomException;
import lombok.extern.log4j.Log4j;
import model.Customer;
import model.Movie;
import model.SalesStand;
import repository.generic.AbstractCrudRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Log4j
public class SalesStandsRepository extends AbstractCrudRepository<SalesStand, Integer> {

    public void addSaleStand(SalesStand salesStand) {
        if (salesStand == null) {
            throw new CustomException("SALES STAND IS NULL", ExceptionCode.REPOSITORY);
        }
        if (salesStand.getCustomerId() == null) {
            throw new CustomException("CUSTOMER IS NULL", ExceptionCode.REPOSITORY);
        }
        if (salesStand.getMovieId() == null) {
            throw new CustomException("MOVIE IS NULL", ExceptionCode.REPOSITORY);
        }
        if (salesStand.getStartDateTime() == null) {
            throw new CustomException("START TIME IS NULL", ExceptionCode.REPOSITORY);
        }
        if (salesStand.getPriceWithDiscount() == null) {
            throw new CustomException("DISCOUNT IS NULL", ExceptionCode.REPOSITORY);
        }
        super.add(salesStand);
    }

    public List<SalesStand> findByStartDateTime() {
        return null;
    }

    public int getTicketQuantityBoughtByCustomer(Customer customer) {
        if (customer == null) {
            throw new CustomException("CUSTOMER IS NULL", ExceptionCode.REPOSITORY);
        }

        return jdbi.withHandle(handle -> handle.createQuery("select * from sales_stands where customer_id = :customer_id")
                .bind("customer_id", customer.getId())
                .mapToBean(Customer.class)
                .list()
                .size()
        );
    }

    public List<Movie> findAllMoviesForCustomer(Customer customer) {
        if (customer == null) {
            throw new CustomException("CUSTOMER IS NULL", ExceptionCode.REPOSITORY);
        }
        return jdbi.withHandle(handle -> handle.createQuery("select m.* " +
                "from sales_stands s join movies m on m.id = s.movie_id where s.customer_id = :id")
                .bind("id", customer.getId())
                .mapToBean(Movie.class)
                .list()
        );
    }

    public List<Movie> findMoviesInConcreteTimeRange(LocalDateTime startDate, LocalDateTime finishDate) {
        if (startDate == null || finishDate == null) {
            throw new CustomException("AT LEAST ONE OF DATE IS NULL", ExceptionCode.REPOSITORY);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd - HH:mm");
        String startDateWithFormatter = startDate.format(formatter);
        String finishDateWithFormatter = finishDate.format(formatter);

        log.info("Start date is: " + startDateWithFormatter + " and finishDate is: " + finishDateWithFormatter);
        return jdbi.withHandle(handle -> handle.createQuery("select m.* from sales_stands s join movies m on " +
                "m.id = s.movie_id where s.start_date_time between :startDate and :finishDate")
                .bind("startDate", startDate)
                .bind("finishDate", finishDate)
                .mapToBean(Movie.class)
                .list());
    }
}
