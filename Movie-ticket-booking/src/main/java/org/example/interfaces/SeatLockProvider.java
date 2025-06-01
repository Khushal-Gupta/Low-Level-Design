package org.example.interfaces;

import org.example.models.Seat;
import org.example.models.Show;

import java.util.List;

public interface SeatLockProvider {

    public void lockSeats(List<Seat> seats, Show show, String user);

    public boolean validateLock(List<Seat> seats, Show show, String user);

    public void releaseLock(List<Seat> seats, Show show, String userId);

    public List<Seat> getLockedSeats(Show show);

}
