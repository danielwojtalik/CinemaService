package com.app.service.cinema_service;

import com.app.service.menu_services.SearchCriteria;

import java.util.List;

public interface ItemService<T> {

    void deleteById();

    void showAll();

    void showFiltered();

    void update();

    SearchCriteria getSearchCriterion();

    List<T> getSearchResult();

}
