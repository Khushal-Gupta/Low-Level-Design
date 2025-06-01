package store;

import org.example.models.Screen;
import org.example.models.Seat;

import java.util.*;

public class ScreenStore {

    private final Map<String, Screen> screens;

    public ScreenStore() {
        screens = new HashMap<>();
    }

    public Screen add(String screenName, List<Seat> seats) {
        Screen screen = new Screen(
                UUID.randomUUID().toString(),
                screenName,
                seats
        );
        screens.put(screen.getId(), screen);
        return screen;
    }

    public Optional<Screen> getById(String screenId) {
        return Optional.ofNullable(screens.get(screenId));
    }
}
