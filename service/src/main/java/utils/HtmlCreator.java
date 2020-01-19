package utils;

import j2html.tags.ContainerTag;
import model.Customer;
import model.Movie;
import model.MovieType;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static j2html.TagCreator.*;

public final class HtmlCreator {

    private static final String bestClientInCategoryHtmlPath
            = "service/src/main/resources/bestClientInCategory.html";
    private static final String numberOfMoviesInEachCategory
            = "service/src/main/resources/numberOfMoviesInEachCategory.html";
    private static final String numberOfTicketsForEachMovieForCustomerHtmlPath
            = "service/src/main/resources/numberOfTicketsForEachMovieForCustomer.html";
    private static final String totalIncomeHtmlPath
            = "service/src/main/resources/totalIncome.html";

    public static String getBestClientInCategoryHtmlPath() {
        return bestClientInCategoryHtmlPath;
    }

    public static String getNumberOfMoviesInEachCategory() {
        return numberOfMoviesInEachCategory;
    }

    public static String getNumberOfTicketsForEachMovieForCustomerHtmlPath() {
        return numberOfTicketsForEachMovieForCustomerHtmlPath;
    }

    public static String getTotalIncomeHtmlPath() {
        return totalIncomeHtmlPath;
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
            FileOutputStream fileOutputStream = new FileOutputStream(bestClientInCategoryHtmlPath, false);
            PrintWriter writer = new PrintWriter(fileOutputStream);
            writer.println(content);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void createHtmlOfAllTicketSalesInEachCategoryInTimeRange(Map<MovieType, Long> numberOfMoviesInCategory, LocalDateTime startDate, LocalDateTime finishDate) {
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

        // with using try-with-resources
        try (FileOutputStream fileOutputStream = new FileOutputStream(numberOfMoviesInEachCategory, false);
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
                                                                h4(entry2.getKey().getTitle() + ": " + entry2.getValue() + " time/times")
                                                        )
                                                ).toArray(ContainerTag[]::new)
                                        )
                                )

                        ).toArray(ContainerTag[]::new)
                )
        ).render();

        try (FileOutputStream outputStream = new FileOutputStream(numberOfTicketsForEachMovieForCustomerHtmlPath, false);
             PrintWriter writer = new PrintWriter(outputStream)
        ) {
            writer.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createHtmlWitTotalTicketCostInCinema(BigDecimal totalTicketCost) {
        String content = body(
                h2("Total income is equal to: " + totalTicketCost + "zł")
        ).render();

        try (FileOutputStream fileOutputStream = new FileOutputStream(totalIncomeHtmlPath, false);
             PrintWriter writer = new PrintWriter(fileOutputStream)
        ) {
            writer.println(content);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
