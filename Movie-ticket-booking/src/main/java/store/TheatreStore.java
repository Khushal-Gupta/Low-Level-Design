package store;

import org.example.models.Location;
import org.example.models.Theatre;

import java.util.*;

public class TheatreStore {

    private final Map<String, Theatre> theatres;

    public TheatreStore() {
        this.theatres = new HashMap<>();
    }

    public Theatre add(String name, Location location) {
        Theatre theatre = new Theatre(
                UUID.randomUUID().toString(),
                name,
                location
        );
        theatres.put(theatre.getId(), theatre);
        return theatre;
    }

    public Optional<Theatre> get(String theatreId) {
        return  Optional.ofNullable(theatres.get(theatreId));
    }

}
