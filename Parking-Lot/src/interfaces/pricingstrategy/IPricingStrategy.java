package interfaces.pricingstrategy;

import java.math.BigDecimal;

public interface IPricingStrategy {
    
    BigDecimal getAmount(long durationInSeconds);
}
