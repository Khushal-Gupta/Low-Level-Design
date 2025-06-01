package org.example.models;

import java.time.LocalDateTime;

public class SeatLock {
    private final String id;
    private final Seat seat;
    private final Show show;
    private String lockedBy;
    private LocalDateTime lockedUntil;

    public SeatLock(String id, Seat seat, Show show, String lockedBy, LocalDateTime lockedUntil) {
        this.id = id;
        this.seat = seat;
        this.show = show;
        this.lockedBy = lockedBy;
        this.lockedUntil = lockedUntil;
    }

    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }

    public String getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(String lockedBy) {
        this.lockedBy = lockedBy;
    }

    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }

    public String getId() {
        return id;
    }
}
