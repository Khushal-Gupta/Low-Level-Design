package org.example.interfaces.pricingstrategy;

import org.example.enums.SeatType;

import java.math.BigDecimal;

public interface PricingStrategy {

    BigDecimal getPrice(SeatType seatType);
}
