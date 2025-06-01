package store;


import org.example.enums.BookingStatus;
import org.example.models.Booking;
import org.example.models.Seat;
import org.example.models.Show;

import java.util.*;

public class BookingStore {

    private final Map<String, Booking> bookings;

    public BookingStore() {
        bookings = new HashMap<>();
    }

    public Booking add(Show show, List<Seat> seats, String userId) {
        Booking booking = new Booking(
                UUID.randomUUID().toString(),
                show,
                seats,
                userId
        );
        bookings.put(booking.getId(), booking);
        return booking;
    }

    public Optional<Booking> get(String bookingId) {
        return Optional.ofNullable(bookings.get(bookingId));
    }

    public List<Booking> getAllByShowIdAndStatus(String showId, BookingStatus bookingStatus) {
        return bookings.values().stream()
                .filter(booking -> booking.getStatus().equals(BookingStatus.COMPLETED)
                        && booking.getShow().getId().equals(showId))
                .toList();
    }
}

