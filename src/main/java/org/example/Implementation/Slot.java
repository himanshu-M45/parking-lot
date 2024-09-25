package org.example.Implementation;

public class Slot {
    private final int slotNumber;
    private boolean isOccupied;
    private Car car;

    public Slot(int slotNumber) {
        this.slotNumber = slotNumber;
        this.isOccupied = false;
        this.car = null;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public Car getCar() {
        return car;
    }

    public void park(Car car) {
        this.car = car;
        this.isOccupied = true;
    }

    public void unpark() {
        this.car = null;
        this.isOccupied = false;
    }
}
