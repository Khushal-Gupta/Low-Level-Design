package interfaces.paymentstrategy;

import java.math.BigDecimal;

public interface IPaymentStrategy {
    
    void makePayment(BigDecimal amount);
}
