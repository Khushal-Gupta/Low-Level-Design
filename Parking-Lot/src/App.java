import enums.VehicleType;
import interfaces.paymentstrategy.impl.CreditCardPaymentStrategy;
import java.util.Optional;
import model.Ticket;
import model.Vehicle;
import service.ParkingSpotManager;

/**
 * Parking Spot Properties - id: UUID vehicleType: Enum(TWO_WHEELER, FOUR_WHEELER) isOccupied:
 * Boolean
 */

/**
 * Ticket Properties - id: uuid vehicle: Vehicle startTimeInEpoch: Int; pricingStrategy:
 * PricingStrategy
 */

/** PricingStrategy interface */

/** TwoWheelerPricingStrategy implements PricingStrategy */

/** ThreeWheelerPricingStrategy implements PricingStrategy */

/**
 * Parking spot manager Properties parkingSpots: List<ParkingSpots> Functions addParkingSpot():
 * ParkingSpot bookParkingSpot(vehicleType: VehicleType): Optional<ParkingSpot>
 * exitParkingSpot(ticket: Ticket, paymentStrategy: PaymentStrategy)
 */

/** PaymentStrategy */

/** CreditCardPaymentStrategy implements PaymentStrategy */

/** UpiPaymentStrategy implements PaymentStrategy */
public class App {
  public static void main(String[] args) throws Exception {
    System.out.println();
    Vehicle fourWheeler = new Vehicle("vehicle1", VehicleType.FOUR_WHEELER);
    Vehicle twoWheeler = new Vehicle("vehicle2", VehicleType.TWO_WHEELER);
    ParkingSpotManager parkingSpotManager = new ParkingSpotManager();

    parkingSpotManager.bookParkingSpot(twoWheeler);

    parkingSpotManager.addParkingSpot(VehicleType.FOUR_WHEELER);

    Optional<Ticket> ticket = parkingSpotManager.bookParkingSpot(fourWheeler);
    Thread.sleep(2000);
    parkingSpotManager.exitParkingSpot(ticket.get(), new CreditCardPaymentStrategy());
    System.out.println("Hello, World!");
  }
}
