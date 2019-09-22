package com.app.menu.criteria;

public enum SearchCriteria {
    BY_NAME("Search by name"),
    BY_SURNAME("Search by surname"),
    BY_AGE("Search by age"),
    BY_TITLE("Search by title"),
    BY_GENRE ("Search by genre"),
    BY_PRICE ("Search by price"),
    BY_DURATION("Search by Duration"),
    BY_RELEASE_DATE("Search by release date");

    private String message;
    SearchCriteria(String message) {
        this.message = message;
    }
}
