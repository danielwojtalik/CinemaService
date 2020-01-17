package service.utils;

import j2html.tags.ContainerTag;
import model.Customer;
import model.MovieType;

import java.util.Map;

import static j2html.TagCreator.body;

public final class HtmlService {

    private HtmlService() {
    }

    public static String getHtmlResponseForBestClientForMovieType(Map<MovieType, Customer> bestCustomersInMovieTypes) {
        String html = "";

        ContainerTag containerTag = body();
        return null;
    }
}
