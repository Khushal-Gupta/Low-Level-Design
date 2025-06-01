package org.example.models;

import org.example.enums.BookingStatus;

import java.util.List;

public class Booking {
    private final String id;
    private final Show show;
    private final List<Seat> seats;
    private BookingStatus status;
    private final String userId;

    public Booking(String id, Show show, List<Seat> seats, String userId) {
        this.id = id;
        this.show = show;
        this.seats = seats;
        this.userId = userId;
        this.status = BookingStatus.CREATED;
    }

    public String getId() {
        return id;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public Show getShow() {
        return show;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public String getUserId() {
        return userId;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}
