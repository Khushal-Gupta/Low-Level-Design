package factory;

import enums.VehicleType;
import interfaces.pricingstrategy.IPricingStrategy;
import interfaces.pricingstrategy.impl.FourWheelerPaymentStrategy;
import interfaces.pricingstrategy.impl.TwoWheelerPaymentStrategy;

public class PricingStratregyFactory {
    
    public IPricingStrategy getPricingStrategy(VehicleType vehicleType) {
        if(vehicleType == VehicleType.TWO_WHEELER) {
            return new TwoWheelerPaymentStrategy();
        }
        return new FourWheelerPaymentStrategy();

    }
}
