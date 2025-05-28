package model;

import factory.PricingStratregyFactory;
import interfaces.pricingstrategy.IPricingStrategy;
import java.time.LocalDateTime;
import java.util.UUID;

public class Ticket {
  String id;
  Vehicle vehicle;
  LocalDateTime startTime;
  IPricingStrategy pricingStrategy;
  ParkingSpot parkingSpot;

  private Ticket(
      String id,
      LocalDateTime startTime,
      Vehicle vehicle,
      IPricingStrategy pricingStrategy,
      ParkingSpot parkingSpot) {
    this.id = id;
    this.vehicle = vehicle;
    this.startTime = startTime;
    this.pricingStrategy = pricingStrategy;
    this.parkingSpot = parkingSpot;
  }

  public ParkingSpot getParkingSpot() {
    return parkingSpot;
  }

  public static Ticket generateTicket(ParkingSpot parkingSpot, Vehicle vehicle) {
    return new Ticket(
        UUID.randomUUID().toString(),
        LocalDateTime.now(),
        vehicle,
        (new PricingStratregyFactory()).getPricingStrategy(vehicle.getVehicleType()),
        parkingSpot);
  }

  public String getId() {
    return id;
  }

  public Vehicle getVehicle() {
    return vehicle;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public IPricingStrategy getPricingStrategy() {
    return pricingStrategy;
  }
}
