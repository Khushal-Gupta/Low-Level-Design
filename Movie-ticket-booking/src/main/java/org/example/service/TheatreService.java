package org.example.service;

import org.example.enums.SeatType;
import org.example.exceptions.EntityNotFoundException;
import org.example.models.Location;
import org.example.models.Screen;
import org.example.models.Seat;
import org.example.models.Theatre;
import store.ScreenStore;
import store.TheatreStore;

import java.util.*;
import java.util.stream.IntStream;


public class TheatreService {

    private final TheatreStore theatreStore;
    private final ScreenStore screenStore;

    public TheatreService(TheatreStore theatreStore, ScreenStore screenStore) {
        this.theatreStore = theatreStore;
        this.screenStore = screenStore;
    }

    public Theatre addTheatre(String name, Location location) {
        return theatreStore.add(
                name,
                location
        );
    }

    public Screen addScreen(
            String theatreId,
            String screenName,
            Map<SeatType, Integer> seatTypeToNumberOfSeatsMap
    ) {
        Theatre theatre = theatreStore.get(theatreId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Theatre with " + theatreId + " not found")
                );
        List<Seat> seats = new ArrayList<>();
        for (Map.Entry<SeatType, Integer> seatTypeToNumberOfSeats : seatTypeToNumberOfSeatsMap.entrySet()) {
            IntStream.range(0, seatTypeToNumberOfSeats.getValue()).forEach(it -> {
                seats.add(new Seat(
                        UUID.randomUUID().toString(),
                        seatTypeToNumberOfSeats.getKey()
                ));
            });
        }
        Screen screen = screenStore.add(screenName, seats);
        theatre.addScreens(List.of(screen));
        return screen;
    }

    public Screen getScreen(String screenId) {
        return screenStore.getById(screenId).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("Screen not found with id: %s", screenId)
                )
        );
    }
}
