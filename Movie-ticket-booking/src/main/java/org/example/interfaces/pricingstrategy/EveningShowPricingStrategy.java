package org.example.interfaces.pricingstrategy;

import org.example.enums.SeatType;

import java.math.BigDecimal;

public class EveningShowPricingStrategy implements PricingStrategy {

    @Override
    public BigDecimal getPrice(SeatType seatType) {
        return switch (seatType) {
            case SILVER -> BigDecimal.valueOf(200);
            case GOLD -> BigDecimal.valueOf(400);
        };
    }
}
