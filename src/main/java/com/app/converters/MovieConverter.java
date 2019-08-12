package com.app.converters;

import com.app.model.Movie;

public class MovieConverter extends JsonConverter<Movie> {

    public MovieConverter(String jsonFileName) {
        super(jsonFileName);
    }
}
