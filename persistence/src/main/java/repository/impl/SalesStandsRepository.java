package repository.impl;


import exceptions.ExceptionCode;
import exceptions.MyException;
import lombok.extern.log4j.Log4j;
import model.Customer;
import model.Movie;
import model.SalesStand;
import repository.AbstractCrudRepository;

import java.time.LocalDate;
import java.util.List;

@Log4j
public class SalesStandsRepository extends AbstractCrudRepository<SalesStand, Integer> {

    public void addSaleStand(SalesStand salesStand) {
        if (salesStand == null) {
            throw new MyException("SALES STAND IS NULL", ExceptionCode.REPOSITORY);
        }
        if (salesStand.getCustomerId() == null) {
            throw new MyException("CUSTOMER IS NULL", ExceptionCode.REPOSITORY);
        }
        if (salesStand.getMovieId() == null) {
            throw new MyException("MOVIE IS NULL", ExceptionCode.REPOSITORY);
        }
        if (salesStand.getStartDateTime() == null) {
            throw new MyException("START TIME IS NULL", ExceptionCode.REPOSITORY);
        }
        if (salesStand.getPriceWithDiscount() == null) {
            throw new MyException("DISCOUNT IS NULL", ExceptionCode.REPOSITORY);
        }
        super.add(salesStand);
    }

    public List<SalesStand> findByStartDateTime() {
        return null;
    }

    public int getTicketQuantityBoughtByCustomer(Customer customer) {
        if (customer == null) {
            throw new MyException("CUSTOMER IS NULL", ExceptionCode.REPOSITORY);
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
            throw new MyException("CUSTOMER IS NULL", ExceptionCode.REPOSITORY);
        }
        return jdbi.withHandle(handle -> handle.createQuery("select m.* " +
                "from sales_stands s join movies m on m.id = s.movie_id where s.customer_id = :id")
                .bind("id", customer.getId())
                .mapToBean(Movie.class)
                .list()
        );
    }

    public List<Movie> findMoviesInConcreteTimeRange(LocalDate startDate, LocalDate finishDate) {
        if (startDate == null || finishDate == null) {
            throw new MyException("AT LEAST ONE OF DATE IS NULL", ExceptionCode.REPOSITORY);
        }
        log.info("Start date is: " + startDate + " and finishDate is: " + finishDate);
        return jdbi.withHandle(handle -> handle.createQuery("select m.* from sales_stands s join movies m on " +
                "m.id = s.movie_id where s.start_date_time between :startDate and :finishDate")
                .bind("startDate", startDate)
                .bind("finishDate", finishDate)
                .mapToBean(Movie.class)
                .list());
    }
}
