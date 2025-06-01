package org.example;

import org.example.dto.DataSetupResponse;
import org.example.enums.BookingStatus;
import org.example.exceptions.SeatUnavailableException;
import org.example.models.Booking;
import org.example.models.Show;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Driver extends BaseTest {


    @DisplayName("Seat getting locked successfully by a user and unavailable for other user")
    @Test
    void test_1() {
        setupStore();
        setupControllers();
        DataSetupResponse setupData = setupData();
        assert showService.getAvailableSeats(setupData.testShowId).size() == 5;
        List<String> testSeatIds = setupData.testSeatIds;
        String user1 = "user-1";
        List<String> user1Seats = List.of(testSeatIds.get(0), testSeatIds.get(1), testSeatIds.get(2));
        Booking user1Booking = bookingService.createBooking(
                setupData.testShowId,
                user1Seats,
                user1
        );
        Show show = showStore.getById(setupData.testShowId).orElseThrow();
        assert seatLockProvider.validateLock(
                show.getScreen().getSeats().stream()
                        .filter(seat->user1Seats.contains(seat.getId()))
                        .toList(),
                show,
                user1
        );
        assert  showService.getAvailableSeats(setupData.testShowId).size() == 2;
        paymentService.makePayment(user1Booking.getId(), user1);
        assert user1Booking.getStatus() == BookingStatus.COMPLETED;
    }

    @DisplayName("Exception thrown when two users try to book overlapping seats concurrently")
    @Test
    void test_2() throws InterruptedException {
        setupStore();
        setupControllers();
        DataSetupResponse setupData = setupData();
        assert showService.getAvailableSeats(setupData.testShowId).size() == 5;

        List<String> testSeatIds = setupData.testSeatIds;
        String user1 = "user-1";
        String user2 = "user-2";

        List<String> user1Seats = List.of(testSeatIds.get(0), testSeatIds.get(1), testSeatIds.get(2));
        List<String> user2Seats = List.of(testSeatIds.get(2), testSeatIds.get(3), testSeatIds.get(4));

        // Thread-safe list to collect exceptions
        List<Throwable> exceptions = Collections.synchronizedList(new ArrayList<>());

        Thread t1 = new Thread(() -> {
            try {
                bookingService.createBooking(setupData.testShowId, user1Seats, user1);
            } catch (Throwable e) {
                exceptions.add(e);
                System.err.println("User1 booking (seats 1,2,3) failed: " + e.getMessage());
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                bookingService.createBooking(setupData.testShowId, user2Seats, user2);
            } catch (Throwable e) {
                exceptions.add(e);
                System.err.println("User2 booking (seats 3,4,5) failed: " + e.getMessage());
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // Assert that exactly one SeatUnavailableException occurred
        long seatUnavailableCount = exceptions.stream()
                .filter(e -> e instanceof SeatUnavailableException)
                .count();

        assert seatUnavailableCount == 1;
    }
}
