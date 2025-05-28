package model;

import enums.VehicleType;

public class ParkingSpot {
    String id;
    VehicleType vehicleType;
    boolean isOccupied;

    public ParkingSpot(String id, VehicleType vehicleType) {
        this.id  = id;
        this.vehicleType = vehicleType;
        this.isOccupied = false;
    }

    public String getId() {
        return id;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }
}
