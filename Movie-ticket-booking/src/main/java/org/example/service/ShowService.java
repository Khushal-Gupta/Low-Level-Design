package org.example.service;

import org.example.enums.Language;
import org.example.enums.MovieFormat;
import org.example.exceptions.EntityNotFoundException;
import org.example.interfaces.pricingstrategy.PricingStrategy;
import org.example.models.*;
import store.MovieStore;
import store.ShowStore;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class ShowService {

    private final ShowStore showStore;
    private final MovieStore movieStore;
    private final TheatreService theatreService;
    private final SeatAvailabilityService seatAvailabilityService;

    public ShowService(
            ShowStore showStore,
            MovieStore movieStore,
            TheatreService theatreService,
            SeatAvailabilityService seatAvailabilityService
    ) {
        this.showStore = showStore;
        this.movieStore = movieStore;
        this.theatreService = theatreService;
        this.seatAvailabilityService = seatAvailabilityService;
    }

    public Show addShow(String movieId,
                        String screenId,
                        LocalDateTime startTime,
                        Duration duration,
                        Language language,
                        MovieFormat movieFormat,
                        PricingStrategy pricingStrategy
    ) {
        Movie movie = movieStore.get(movieId).orElseThrow(
                () -> new EntityNotFoundException("Movie not found by id: " + movieId)
        );
        Screen screen = theatreService.getScreen(screenId);
        return showStore.add(
                startTime,
                duration,
                movie,
                screen,
                language,
                movieFormat,
                pricingStrategy
        );
    }


    public List<Seat> getAvailableSeats(String showId) {
        Show show = showStore.getById(showId).orElseThrow(
                () ->  new EntityNotFoundException("Show not found for id: " + showId)
        );
        return seatAvailabilityService.getAvailableSeats(show);
    }

}
