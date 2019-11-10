package com.app.repository.impl;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Customer;
import com.app.model.Movie;
import com.app.model.SalesStand;
import com.app.repository.AbstractCrudRepository;
import lombok.extern.log4j.Log4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Log4j
public class SalesStandsRepository extends AbstractCrudRepository<SalesStand, Integer> {

//    @Override
//    public void add(SalesStand salesStand) {
//        if (salesStand == null) {
//            throw new MyException("SALES STANDS IS NULL", ExceptionCode.REPOSITORY);
//        }
//
//        jdbi.useTransaction(handle -> handle.createUpdate("insert into "));
//    }
//
//    @Override
//    public void update(SalesStand salesStand) {
//
//    }
//
//    @Override
//    public Optional<SalesStand> findById(Integer id) {
//        return Optional.empty();
//    }
//
//    @Override
//    public List<SalesStand> findAll() {
//        return jdbi.withHandle(handle -> handle.createQuery("select * from sales_stands")
//                .mapToBean(SalesStand.class)
//                .list());
//    }
//
//    @Override
//    public void deleteByID(Integer id) {
//
//    }
//
//    @Override
//    public void deleteAll() {
//
//    }

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
        if (salesStand.getStartTime() == null) {
            throw new MyException("START TIME IS NULL", ExceptionCode.REPOSITORY);
        }
        if (salesStand.getPriceWithDiscount() == null) {
            throw new MyException("DISCOUNT IS NULL", ExceptionCode.REPOSITORY);
        }

        LocalDateTime startTimeWithDate = LocalDateTime.now().with(salesStand.getStartTime());
        jdbi.useTransaction(handle -> handle.createUpdate("insert into sales_stands (customer_id, movie_id, " +
                "start_date_time, price_with_discount) values (:customer_id, :movie_id, :start_date_time, :price_with_discount)")
                .bind("customer_id", salesStand.getCustomerId())
                .bind("movie_id", salesStand.getMovieId())
                .bind("start_date_time", startTimeWithDate)
                .bind("price_with_discount", salesStand.getPriceWithDiscount())
                .execute()
        );
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
