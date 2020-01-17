package converters;


import model.Movie;

public class MovieConverter extends JsonConverter<Movie> {

    public MovieConverter(String jsonFileName) {
        super(jsonFileName);
    }
}
