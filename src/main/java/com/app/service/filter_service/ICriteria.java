package com.app.service.filter_service;

import java.util.List;

public interface ICriteria<T> {
    List<T> meetCriteria(List<T> values);
}
