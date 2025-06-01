package org.example.service;

import org.example.exceptions.EntityNotFoundException;
import org.example.models.Booking;
import org.example.models.Payment;
import store.BookingStore;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public class PaymentService {

    private final BookingService bookingService;
    private final BookingStore bookingStore;

    public PaymentService(BookingService bookingService, BookingStore bookingStore) {
        this.bookingService = bookingService;
        this.bookingStore = bookingStore;
    }

    public void makePayment(String bookingId, String userId) {
        Booking booking = bookingStore.get(bookingId).orElseThrow(
                () -> new EntityNotFoundException("Booking store not found with id: " + bookingId)
        );
        BigDecimal amount = bookingService.getBookingAmount(booking);
        System.out.println("Payment is successfully completed for amount: " + amount);
        bookingService.confirmBooking(bookingId, userId);
    }

}
