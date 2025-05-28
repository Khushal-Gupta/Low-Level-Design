package interfaces.pricingstrategy.impl;

import java.math.BigDecimal;

import interfaces.pricingstrategy.IPricingStrategy;

public class TwoWheelerPaymentStrategy implements IPricingStrategy {

    BigDecimal basePrice = BigDecimal.ONE;

    @Override
    public BigDecimal getAmount(long durationInSeconds) {
        return basePrice.multiply(BigDecimal.valueOf(durationInSeconds));
    }

}
