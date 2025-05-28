package service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import enums.VehicleType;
import interfaces.paymentstrategy.IPaymentStrategy;
import model.ParkingSpot;
import model.Ticket;
import model.Vehicle;

public class ParkingSpotManager {
    List<ParkingSpot> parkingSpots;
    Map<VehicleType, List<ParkingSpot>> freeParkingSpots;

    public ParkingSpotManager() {
        freeParkingSpots = new HashMap<>();
        freeParkingSpots.put(VehicleType.TWO_WHEELER, new ArrayList<>());
        freeParkingSpots.put(VehicleType.FOUR_WHEELER, new ArrayList<>());

        parkingSpots = new ArrayList<>();
    }

    public void addParkingSpot(VehicleType vehicleType) {
        freeParkingSpots.get(vehicleType).add(new ParkingSpot(
            UUID.randomUUID().toString(),
            vehicleType
        ));
    }

    public Optional<Ticket> bookParkingSpot(Vehicle vehicle) {
        if(freeParkingSpots.get(vehicle.getVehicleType()).isEmpty()) {
            System.out.println("Sorry, Parking slot is not empty");
            return Optional.empty();
        }
        ParkingSpot ps = freeParkingSpots.get(vehicle.getVehicleType()).getLast();
        ps.setOccupied(true);
        freeParkingSpots.get(vehicle.getVehicleType()).remove(ps);

        Ticket ticket = Ticket.generateTicket(ps, vehicle);
        System.out.println("Parking slot is booked with ticket id: " + ticket.getId());
        return Optional.of(ticket);
    }


    public void exitParkingSpot(Ticket ticket, IPaymentStrategy paymentStrategy) {
       
        long parkingDuration = Duration.between(ticket.getStartTime(), LocalDateTime.now()).toSeconds();
        BigDecimal payableAmount = ticket.getPricingStrategy().getAmount(parkingDuration);
        paymentStrategy.makePayment(payableAmount);
        ParkingSpot ps = ticket.getParkingSpot();
        ps.setOccupied(false);
        freeParkingSpots.get(ticket.getVehicle().getVehicleType()).add(ps);
    }

}
