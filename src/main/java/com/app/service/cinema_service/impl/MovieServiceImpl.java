package com.app.service.cinema_service.impl;

import com.app.converters.MovieConverter;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.main.ApplicationConstants;
import com.app.model.Movie;
import com.app.repository.impl.MovieRepositoryImpl;
import com.app.service.UserDataService;
import com.app.service.cinema_service.ItemService;
import com.app.service.menu_services.SearchCriteria;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MovieServiceImpl implements ItemService<Movie> {

    private static final String CRITERIA_OPTIONS = "\n---CRITERIA OPTIONS---\n" +
            "1. Search by title\n" +
            "2. Search by genre\n" +
            "3. Search by price\n" +
            "4. Search by duration\n" +
            "5. Search by release date";

    private UserDataService dataService = UserDataService.getInstance();
    private MovieRepositoryImpl movieRepository = new MovieRepositoryImpl();


    public MovieServiceImpl() {
    }

    private Movie getMovieFromJson(String fileName) {
        // TODO: 20.06.2019 Create Movie validator
        return new MovieConverter(fileName).fromJson()
                .orElseThrow(() -> new MyException("MOVIE JSON CONVERTER EXCEPTION", ExceptionCode.MOVIE_SERVICE));
    }

    private String getFullFilePath() {
        UserDataService us = UserDataService.getInstance();
        String movieTitle = us.getString("Please write movie title");
        return new StringBuilder(ApplicationConstants.PATH_TO_RESOURCES_FOLDER)
                .append(movieTitle + ".json").toString();
    }

    public void addMovie() {
        String pathToFile = getFullFilePath();
        Movie movie = getMovieFromJson(pathToFile);
        movieRepository.add(movie);
        System.out.println("Movie have been added successfully");
    }

    public void deleteById() {
        int id;
        id = dataService.getIntWithValidator("ID of movie to delete", result -> result > 0);
        movieRepository.deleteByID(id);
    }

    public void updatePrice() {
        MovieRepositoryImpl movieRepository = new MovieRepositoryImpl();
        int id;
        id = dataService.getIntWithValidator("Write ID of movie to update its price", result -> result > 0);
        BigDecimal price = dataService.getBigDecimal("Write new price of movie", p -> BigDecimal.ZERO.compareTo(p) < 0);

        Movie movieFromDb = movieRepository.findById(id).orElseThrow(
                () -> new MyException(String.format("THERE IS NO MOVIE WITH ID - %d", id), ExceptionCode.MOVIE_SERVICE));
        Movie newMovie = Movie.builder()
                .id(movieFromDb.getId())
                .price(price)
                .build();
        movieRepository.update(newMovie);
    }

    public List<Movie> findByTitle() {

        String title = dataService.getString("Write title of movie which you are looking for:...");
        return movieRepository.findByTitle(title);
    }

    public List<Movie> findByGenre() {
        String genre = dataService.getString("Write genre of movie which you are looking for:...");
        return movieRepository.findByGenre(genre);

    }

    public List<Movie> findByPrice() {
        boolean fromPrice = isFromPrice();
        String message = "Show movies with price %s... Please write boundary price:";
        BigDecimal price = dataService.getBigDecimal(String.format(message, fromPrice ? "from " : "to "),
                p -> p.compareTo(BigDecimal.ZERO) > 0);
        return movieRepository.findByPrice(price, fromPrice);
    }

    private boolean isFromPrice() {
        int mode = dataService.getInt("For price from write 1, default price is to");
        return mode == 1;
    }

    private List<Movie> findByDuration() {
        boolean isFromDuration = isFromDuration();
        String message = "Show movies with duration %s... Please write boundary duration:";
        int price = dataService.getIntWithValidator(String.format(message, isFromDuration ? "from" : "to"), p -> p > 0);
        return movieRepository.findByDuration(price, isFromDuration);
    }

    private boolean isFromDuration() {
        int mode = dataService.getInt("For duration from write 1, default duration is to");
        return mode == 1;
    }

    private List<Movie> findByReleaseDate() {
        boolean fromReleaseDate = isFromReleaseDate();
        String message = "Show movies with duration %s... Please write the boundary release date of movie in format: dd-mm-yyyy";
        String stringDate = dataService.getString(String.format(message, fromReleaseDate ? "from" : "to"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(stringDate);
        } catch (ParseException e) {
            throw new MyException("CAN NOT PARSE DATE", ExceptionCode.MOVIE_SERVICE);
        }
        return movieRepository.findByReleaseDate(date, fromReleaseDate);

    }

    private boolean isFromReleaseDate() {
        int mode = dataService.getInt("For release date from write 1, release date is to");
        return mode == 1;
    }

    @Override
    public void showAll() {
        List<Movie> movies = movieRepository.findAll();
        movies.forEach(System.out::println);
    }

    @Override
    public SearchCriteria getSearchCriterion() {

        int option = dataService.getIntWithValidator(CRITERIA_OPTIONS, op -> op > 0 && op < 6);
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

    @Override
    public List<Movie> getSearchResult() {
        SearchCriteria searchCriteria = getSearchCriterion();
        if (searchCriteria == SearchCriteria.BY_TITLE) {
            return findByTitle();
        } else if (searchCriteria == SearchCriteria.BY_GENRE) {
            return findByGenre();
        } else if (searchCriteria == SearchCriteria.BY_PRICE) {
            return findByPrice();
        } else if (searchCriteria == SearchCriteria.BY_DURATION) {
            return findByDuration();
        } else if (searchCriteria == SearchCriteria.BY_RELEASE_DATE) {
            return findByReleaseDate();
        }
        return null;
    }

    @Override
    public void showFiltered() {
        List<Movie> movies = getSearchResult();
        System.out.println("All movies pass the criteria:");
        movies.forEach(System.out::println);
    }

    @Override
    public void update() {

    }
}
