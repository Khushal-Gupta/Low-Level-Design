package store;

import org.example.enums.Language;
import org.example.enums.MovieFormat;
import org.example.interfaces.pricingstrategy.PricingStrategy;
import org.example.models.Movie;
import org.example.models.Screen;
import org.example.models.Show;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ShowStore {

    private final Map<String, Show> shows;

    public ShowStore() {
        this.shows = new HashMap<>();
    }

    public Show add(
            LocalDateTime startTime,
            Duration duration,
            Movie movie,
            Screen screen,
            Language language,
            MovieFormat movieFormat,
            PricingStrategy pricingStrategy
    ) {
        Show show = new Show(
                UUID.randomUUID().toString(),
                startTime,
                duration,
                movie,
                screen,
                language,
                movieFormat,
                pricingStrategy
        );
        shows.put(show.getId(), show);
        return show;
    }

    public Optional<Show> getById(String showId) {
        return Optional.ofNullable(shows.get(showId));
    }
}
