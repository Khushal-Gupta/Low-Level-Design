package store;

import org.example.exceptions.EntityNotFoundException;
import org.example.models.Movie;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MovieStore {

    private final Map<String, Movie> movies;

    public MovieStore() {
        movies = new HashMap<>();
    }

    public Movie add(String movieName) {
        Movie movie = new Movie(
                UUID.randomUUID().toString(),
                movieName
        );
        movies.put(movie.getId(), movie);
        return movie;
    }

    public Optional<Movie> get(String movieId) {
        return Optional.ofNullable(movies.get(movieId));
    }

}

