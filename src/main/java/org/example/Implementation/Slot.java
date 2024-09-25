package org.example.Implementation;

public class Slot {
    private final int slotNumber;
    private Car car;

    public Slot(int slotNumber) {
        this.slotNumber = slotNumber;
        this.car = null;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public boolean isOccupied() {
        return car != null;
    }

    public Car getCar() {
        return car;
    }

    public void park(Car car) {
        this.car = car;
        car.isCarParked = true;
    }

    public void unpark() {
        this.car.isCarParked = false;
        this.car = null;
    }
}
