package org.example.models;

import org.example.enums.SeatType;

public class Seat {
    private final String id;
    private final SeatType seatType;

    public Seat(String id, SeatType seatType) {
        this.id = id;
        this.seatType = seatType;
    }

    public String getId() {
        return id;
    }

    public SeatType getSeatType() {
        return seatType;
    }
}
