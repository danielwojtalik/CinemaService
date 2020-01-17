package menu;

import exceptions.MyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import model.Movie;
import service.cinema_service.MovieService;
import service.criteria.SearchCriteria;
import service.utils.UserDataService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Predicate;

@RequiredArgsConstructor
@Log4j
public class MovieMenu {

    private final MovieService movieService;

    public void manageMovies() {
        boolean continueLoop = true;
        while (continueLoop) {
            try {
                int option = chooseOption();
                switch (option) {
                    case 1 -> option1();
                    case 2 -> option2();
                    case 3 -> option3();
                    case 4 -> option4();
                    case 5 -> continueLoop = false;
                }
            } catch (MyException e) {
                System.err.println(e.getExceptionInfo().getDescription());
                System.err.println(e.getExceptionInfo().getExceptionDate());
            }
        }
    }

    private int chooseOption() {
        System.out.println("\n---MOVIE MENU---");
        System.out.println("1. Delete movie by id");
        System.out.println("2. Update price of movie");
        System.out.println("3. Show all movie");
        System.out.println("4. Search movie by...");
        System.out.println("5. Back to MAIN MENU");
        return UserDataService.getIntWithValidator("Choose option:", op -> op < 6);
    }

    private void option1() {
        int movieIdToDelete = retrieveMovieIdFromUser("ID of movie to delete",
                result -> result > 0);
        movieService.deleteById(movieIdToDelete);
    }

    private int retrieveMovieIdFromUser(String message, Predicate<Integer> callback) {
        return UserDataService.getIntWithValidator(message, callback);
    }

    private void option2() {
        int movieIdToUpdatePrice = retrieveMovieIdFromUser("Write ID of movie to update its price",
                result -> result > 0);
        BigDecimal newTicketPrice = retrieveNewTicketPriceFromUser("Write new price of movie",
                p -> BigDecimal.ZERO.compareTo(p) < 0);
        movieService.updatePrice(movieIdToUpdatePrice, newTicketPrice);
    }

    private BigDecimal retrieveNewTicketPriceFromUser(String message, Predicate<BigDecimal> callback) {
        return UserDataService.getBigDecimal(message, callback);
    }

    private void option3() {
        List<Movie> movies = movieService.returnAllMovies();
        showAllMovies(movies);
    }

    private void showAllMovies(List<Movie> movies) {
        movies.forEach(System.out::println);
    }

    private void option4() {
        showFiltered();
    }


    private SearchCriteria getSearchCriterion() {
        int option = chooseCriteriaOption();
        SearchCriteria searchCriteria = null;
        switch (option) {
            case 1 -> searchCriteria = SearchCriteria.BY_TITLE;
            case 2 -> searchCriteria = SearchCriteria.BY_GENRE;
            case 3 -> searchCriteria = SearchCriteria.BY_PRICE;
            case 4 -> searchCriteria = SearchCriteria.BY_DURATION;
            case 5 -> searchCriteria = SearchCriteria.BY_RELEASE_DATE;
        }
        return searchCriteria;
    }

    private static int chooseCriteriaOption() {
        System.out.println("1. Search by title");
        System.out.println("2. Search by genre");
        System.out.println("3. Search by price");
        System.out.println("4. Search by duration");
        System.out.println("5. Search by release date");
        return UserDataService.getIntWithValidator("Choose criteria option:", cr -> cr < 6 && cr > 0);
    }

    private List<Movie> getSearchResult() {
        SearchCriteria searchCriteria = getSearchCriterion();
        MovieService.Boundary boundary;
        if (searchCriteria == SearchCriteria.BY_TITLE) {
            String title = UserDataService.getString("Write title of movie which you are looking for:...");
            return movieService.findByTitle(title);
        } else if (searchCriteria == SearchCriteria.BY_GENRE) {
            String genre = UserDataService.getString("Write genre of movie which you are looking for:...");
            return movieService.findByGenre(genre);
        } else if (searchCriteria == SearchCriteria.BY_PRICE) {
            boundary = getBoundaryOption();
            String message = "Please write the boundary price";
            BigDecimal price = UserDataService.getBigDecimal(message, p -> p.compareTo(BigDecimal.ZERO) > 0);
            return movieService.findByPrice(price, boundary);
        } else if (searchCriteria == SearchCriteria.BY_DURATION) {
            boundary = getBoundaryOption();
            String message = "Show movies with duration %s... Please write boundary duration:";
            int duration = UserDataService.getIntWithValidator(message, p -> p > 0);
            return movieService.findByDuration(duration, boundary);
        } else if (searchCriteria == SearchCriteria.BY_RELEASE_DATE) {
            boundary = getBoundaryOption();
            String message = "Please write the boundary release date of movie in format: dd-mm-yyyy";
            String stringDate = UserDataService.getString(message);
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate releaseDate = LocalDate.parse(stringDate, dateFormat);
            return movieService.findByReleaseDate(releaseDate, boundary);
        }
        return null;
    }

    private MovieService.Boundary getBoundaryOption() {
        MovieService.Boundary boundary;
        int boundaryOption = UserDataService.getInt("If you need less than boundary press 1, else press any key...");
        if (boundaryOption == 1) {
            boundary = MovieService.Boundary.LESS_THAN;
        } else {
            boundary = MovieService.Boundary.MORE_THAN;
        }
        return boundary;
    }

    private void showFiltered() {
        List<Movie> movies = getSearchResult();
        System.out.println("All movies pass the criteria:");
        movies.forEach(System.out::println);
    }
}



