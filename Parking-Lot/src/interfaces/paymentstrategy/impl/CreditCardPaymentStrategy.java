package interfaces.paymentstrategy.impl;

import java.math.BigDecimal;

import interfaces.paymentstrategy.IPaymentStrategy;

public class CreditCardPaymentStrategy implements IPaymentStrategy {

    @Override
    public void makePayment(BigDecimal amount) {
        System.out.println("Payment of Rs: "+ amount.toString() + " has been done thorugh credit card");
    }
    
}
