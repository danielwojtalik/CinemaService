package utils;

import j2html.tags.ContainerTag;
import model.Customer;
import model.Movie;
import model.MovieType;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static j2html.TagCreator.*;

public final class HtmlCreator {

    private static final String BEST_CLIENT_IN_CATEGORY_HTML
            = "service/src/main/resources/bestClientInCategory.html";
    private static final String NUMBER_OF_MOVIES_IN_EACH_CATEGORY_HTML
            = "service/src/main/resources/numberOfMoviesInEachCategory.html";
    private static final String NUMBER_OF_TICKETS_FOR_EACH_MOVIE_FOR_CUSTOMER_HTML
            = "service/src/main/resources/numberOfTicketsForEachMovieForCustomer.html";
    private static final String TOTAL_INCOME_HTML
            = "service/src/main/resources/totalIncome.html";

    public static String getBestClientInCategoryHtml() {
        return BEST_CLIENT_IN_CATEGORY_HTML;
    }

    public static String getNumberOfMoviesInEachCategoryHtml() {
        return NUMBER_OF_MOVIES_IN_EACH_CATEGORY_HTML;
    }

    public static String getNumberOfTicketsForEachMovieForCustomerHtml() {
        return NUMBER_OF_TICKETS_FOR_EACH_MOVIE_FOR_CUSTOMER_HTML;
    }

    public static String getTotalIncomeHtml() {
        return TOTAL_INCOME_HTML;
    }

    public static void createHtmlWithBestClientInEachCategory(Map<MovieType, Customer> bestClientInCategoryMap) {

        AtomicInteger index = new AtomicInteger(1);

        String content = body(
                h1("Best customers in each movie type:"),
                div(attrs("#bestClientInCategoryMap"),
                        bestClientInCategoryMap.entrySet().stream().map(entry ->
                                div(
                                        h2(index.getAndIncrement() + ". \"" + entry.getKey() + "\": "
                                                + entry.getValue().getName() + " " + entry.getValue().getSurname()
                                                + " (with id   " + entry.getValue().getId() + ")")
                                )
                        ).toArray(ContainerTag[]::new)
                )
        ).render();

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(BEST_CLIENT_IN_CATEGORY_HTML, false);
            PrintWriter writer = new PrintWriter(fileOutputStream);
            writer.println(content);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void createHtmlOfAllTicketSalesInEachCategoryInTimeRange(Map<MovieType, Long> numberOfMoviesInCategory,
                                                                           LocalDateTime startDate, LocalDateTime finishDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd - HH:mm");
        String startDateWithFormatter = startDate.format(formatter);
        String finishDateWithFormatter = finishDate.format(formatter);

        String content = body(
                h1("Tickets sold in each category in set time range (" + startDateWithFormatter + " - " + finishDateWithFormatter + "):"),
                div(attrs("#numberOfMoviesInCategory"),
                        numberOfMoviesInCategory.entrySet().stream().map(entry ->
                                div(
                                        h2("\"" + entry.getKey() + "\": " + entry.getValue())
                                )
                        ).toArray(ContainerTag[]::new)

                )
        ).render();

        try (FileOutputStream fileOutputStream = new FileOutputStream(NUMBER_OF_MOVIES_IN_EACH_CATEGORY_HTML, false);
             PrintWriter writer = new PrintWriter(fileOutputStream)
        ) {
            writer.println(content);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createHtmlWithNumberOfTicketSoldForEachCustomer(Map<Customer, Map<Movie, Long>> numberOfTicketsForEachMovieBoughtByCustomer) {
        String content = body(
                h1("Number of tickets for each movie sold to each Customer:"),
                div(attrs("#numberOfTicketsForEachMovieBoughtByCustomer"),
                        numberOfTicketsForEachMovieBoughtByCustomer.entrySet().stream().map(entry ->
                                div(
                                        h2("Customer: " + entry.getKey().getName() + " "
                                                + entry.getKey().getSurname() + " with id: " + entry.getKey().getId() + " bought: "),
                                        div(
                                                entry.getValue().entrySet().stream().map(entry2 ->
                                                        div(
                                                                h4(entry2.getKey().getTitle() + ": " + entry2.getValue() + " time/times"))
                                                ).toArray(ContainerTag[]::new))
                                )

                        ).toArray(ContainerTag[]::new)
                )
        ).render();

        try (FileOutputStream outputStream = new FileOutputStream(NUMBER_OF_TICKETS_FOR_EACH_MOVIE_FOR_CUSTOMER_HTML, false);
             PrintWriter writer = new PrintWriter(outputStream)) {
            writer.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createHtmlWitTotalTicketCostInCinema(BigDecimal totalTicketCost) {
        String content = body(
                h2("Total income is equal to: " + totalTicketCost + "zł")
        ).render();

        try (FileOutputStream fileOutputStream = new FileOutputStream(TOTAL_INCOME_HTML, false);
             PrintWriter writer = new PrintWriter(fileOutputStream)
        ) {
            writer.println(content);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
