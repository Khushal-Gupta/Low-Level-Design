package org.example.models;

import java.math.BigDecimal;

public class Payment {
    private final String id;
    private final Booking booking;
    private final BigDecimal amount;

    public Payment(String id, Booking booking, BigDecimal amount) {
        this.id = id;
        this.booking = booking;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }
}
