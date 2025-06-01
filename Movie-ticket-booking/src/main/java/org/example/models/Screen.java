package org.example.models;

import java.util.List;

public class Screen {
    private final String id;
    private final String name;
    private final List<Seat> seats;

    public Screen(String id, String name, List<Seat> seats) {
        this.id = id;
        this.seats = seats;
        this.name = name;
    }

    public void addSeats(List<Seat> seats) {
        this.seats.addAll(seats);
    }

    public String getId() {
        return id;
    }

    public List<Seat> getSeats() {
        return seats;
    }
}
