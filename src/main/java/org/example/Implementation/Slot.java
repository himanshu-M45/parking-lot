package org.example.Implementation;

import org.example.Exceptions.CarAlreadyParkedException;
import org.example.Exceptions.InvalidTicketException;

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

    public Car unpark() {
        Car carToUnpark = this.car;
        this.car.isCarParked = false;
        this.car = null;
        return carToUnpark;
    }
}
