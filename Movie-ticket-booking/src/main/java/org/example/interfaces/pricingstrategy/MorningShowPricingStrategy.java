package org.example.interfaces.pricingstrategy;

import org.example.enums.SeatType;

import java.math.BigDecimal;

public class MorningShowPricingStrategy implements PricingStrategy {

    @Override
    public BigDecimal getPrice(SeatType seatType) {
        return switch (seatType) {
            case SILVER -> BigDecimal.valueOf(100);
            case GOLD -> BigDecimal.valueOf(200);
        };
    }
}
