package utils;

import exceptions.ExceptionCode;
import exceptions.MyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import model.Customer;
import model.Movie;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Log4j
@RequiredArgsConstructor
public class TransactionHistoryUtil {

    public static String prepareContentOfTransactionHistory(List<Movie> movies, Customer customer) {
        String moviesFormattedList = formatMovieList(movies);
        String mainContent = new StringBuilder("Hello %s. \nYou have already " +
                "generate transaction history of yours movies. There is full list of filtered movies.\n")
                .append(moviesFormattedList).toString();
        return String.format(mainContent, customer.getName());
    }

    private static String formatMovieList(List<Movie> movies) {
        if (movies == null) {
            throw new MyException("movie list is null", ExceptionCode.TRANSACTION_HISTORY_SERVICE);
        }

        String formattedMovies = null;
        if (!movies.isEmpty()) {
            AtomicInteger index = new AtomicInteger(1);
            formattedMovies = movies.stream()
                    .map(movie -> index + ". Title: " + movie.getTitle() + ", Price: " + movie.getPrice())
                    .collect(Collectors.joining("\n"));
        }

        return formattedMovies;
    }

}
