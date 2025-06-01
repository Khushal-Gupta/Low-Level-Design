package org.example.models;

import org.example.enums.Language;
import org.example.enums.MovieFormat;
import org.example.interfaces.pricingstrategy.PricingStrategy;

import java.time.Duration;
import java.time.LocalDateTime;

public class Show {
    private final String id;
    private final LocalDateTime startTime;
    private final Duration duration;
    private final Movie movie;
    private final Screen screen;
    private final Language language;
    private final MovieFormat movieFormat;
    private final PricingStrategy pricingStrategy;

    public Show(
            String id,
            LocalDateTime startTime,
            Duration duration,
            Movie movie,
            Screen screen,
            Language language,
            MovieFormat movieFormat,
            PricingStrategy pricingStrategy
    ) {
        this.id = id;
        this.startTime = startTime;
        this.duration = duration;
        this.movie = movie;
        this.screen = screen;
        this.language = language;
        this.movieFormat = movieFormat;
        this.pricingStrategy = pricingStrategy;
    }

    public String getId() {
        return id;
    }

    public Screen getScreen() {
        return screen;
    }

    public PricingStrategy getPricingStrategy() {
        return pricingStrategy;
    }
}
