package org.example.service;

import org.example.interfaces.SeatLockProvider;
import org.example.models.Seat;
import org.example.models.Show;

import java.util.ArrayList;
import java.util.List;

public class SeatAvailabilityService {

    private final SeatLockProvider seatLockProvider;
    private final BookingService bookingService;

    public SeatAvailabilityService(SeatLockProvider seatLockProvider, BookingService bookingService) {
        this.seatLockProvider = seatLockProvider;
        this.bookingService = bookingService;
    }

    public List<Seat> getAvailableSeats(Show show) {
        List<Seat> totalSeats = show.getScreen().getSeats();
        List<Seat> availableSeats = new ArrayList<>(totalSeats);
        List<Seat> unavailableSeats = getUnavailableSeats(show);
        unavailableSeats.forEach(availableSeats::remove);
        return availableSeats;
    }

    private List<Seat> getUnavailableSeats(Show show) {
        List<Seat> unavailableSeat = new ArrayList<>();
        unavailableSeat.addAll(bookingService.getBookedSeats(show));
        unavailableSeat.addAll(seatLockProvider.getLockedSeats(show));
        return unavailableSeat;
    }

}
