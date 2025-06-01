package org.example.service;

import org.example.exceptions.EntityNotFoundException;
import org.example.models.Movie;
import store.MovieStore;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MovieService {

    private final MovieStore movieStore;

    public MovieService(MovieStore movieStore) {
        this.movieStore = movieStore;
    }

    public Movie addMovie(String movieName) {
        return movieStore.add(movieName);
    }

}
