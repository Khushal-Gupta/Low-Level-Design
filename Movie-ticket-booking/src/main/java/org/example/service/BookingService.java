package org.example.service;

import org.example.enums.BookingStatus;
import org.example.exceptions.*;
import org.example.interfaces.SeatLockProvider;
import org.example.interfaces.pricingstrategy.PricingStrategy;
import org.example.models.Booking;
import org.example.models.Seat;
import org.example.models.Show;
import store.BookingStore;
import store.ShowStore;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class BookingService {

    private final BookingStore bookingStore;
    private final SeatLockProvider seatLockProvider;
    private final ShowStore showStore;

    public BookingService(BookingStore bookingStore, SeatLockProvider seatLockProvider, ShowStore showStore) {
        this.bookingStore = bookingStore;
        this.seatLockProvider = seatLockProvider;
        this.showStore = showStore;
    }

    public Booking createBooking(String showId, List<String> seatIds, String userId) {
        Show show = showStore.getById(showId).orElseThrow(
                () -> new EntityNotFoundException("Show not found with id: "+ showId)
        );
        List<Seat> seats = show.getScreen().getSeats().stream()
                .filter(seat -> seatIds.contains(seat.getId())).toList();
        if (seats.size() != seatIds.size()) {
            throw new BadRequestException("Seat ids are not a part of show");
        }
        if (areSeatsAlreadyBooked(show, seats)) {
            throw new SeatAlreadyBookedException("Some seats are already booked");
        }
        seatLockProvider.lockSeats(seats, show, userId);
        Booking booking = bookingStore.add(
                show,
                seats,
                userId
        );
        return booking;
    }

    public Booking confirmBooking(String bookingId, String userId) {
        Booking booking = bookingStore.get(bookingId).orElseThrow(
                ()-> new EntityNotFoundException("Booking not found with id: "+ bookingId)
        );
        if(booking.getStatus() != BookingStatus.CREATED) {
            throw new BadRequestException("Booking is already completed");
        }

        if (!Objects.equals(booking.getUserId(), userId)) {
            throw  new UnauthorizedException("user id is not matching with booking user id");
        }

        if (!seatLockProvider.validateLock(booking.getSeats(), booking.getShow(), userId)) {
            throw new SeatUnavailableException("Some seats are not available");
        }

        booking.setStatus(BookingStatus.COMPLETED);
        return booking;
    }

//    public Optional<Booking> getBooking(String bookingId) {
//        return Optional.ofNullable(bookings.get(bookingId));
//    }
//
//    public BigDecimal getBookingAmount(String bookingId) {
//        Booking booking = bookingStore.get(bookingId).orElseThrow(
//                () -> new EntityNotFoundException("Booking store not found with id: " + bookingId)
//        );
//        return getBookingAmount(booking);
//    }

    public BigDecimal getBookingAmount(Booking booking) {
        PricingStrategy pricingStrategy = booking.getShow().getPricingStrategy();
        return booking.getSeats().stream()
                .map(seat -> pricingStrategy.getPrice(seat.getSeatType()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Seat> getBookedSeats(Show show) {
        return bookingStore.getAllByShowIdAndStatus(show.getId(), BookingStatus.COMPLETED)
                .stream()
                .flatMap(booking -> booking.getSeats().stream())
                .collect(Collectors.toList());
    }

    private boolean areSeatsAlreadyBooked(Show show, List<Seat> seats) {
        Set<Seat> bookedSeats = new HashSet<>(getBookedSeats(show));

        for(Seat seat: seats) {
            if (bookedSeats.contains(seat)) {
                return true;
            }
        }
        return false;
    }
}
