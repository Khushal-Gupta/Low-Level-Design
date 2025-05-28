package interfaces.paymentstrategy.impl;

import java.math.BigDecimal;

import interfaces.paymentstrategy.IPaymentStrategy;

public class UpiPaymentStrategy implements IPaymentStrategy {

    @Override
    public void makePayment(BigDecimal amount) {
        System.out.println("Payment of Rs: "+ amount.toString() + " has been done thorugh upi");
    }
    
}
